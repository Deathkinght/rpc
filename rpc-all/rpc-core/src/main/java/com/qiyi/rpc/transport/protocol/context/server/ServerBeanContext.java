package com.qiyi.rpc.transport.protocol.context.server;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NodeExistsException;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.registry.Registry;
import com.qiyi.rpc.registry.zookeeper.CuratorClient;
import com.qiyi.rpc.registry.zookeeper.bean.ZkRegistryBean;
import com.qiyi.rpc.spring.bean.VersionBean;
import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;
import com.qiyi.rpc.transport.server.Acceptor;
import com.qiyi.rpc.utils.Function;
import com.qiyi.rpc.utils.MethodUtil;

/**
 * 
 * @author qiyi 用于临时存储bean与方法
 */
public class ServerBeanContext {

	private static volatile List<VersionBean> versionBeans = new ArrayList<>();

	private static final Map<String, ServerVersionWrapper> beanVersionMap = new HashMap<>();

	private static int methodSeq = 0;

	private static int beanSeq = 0;

	private static Registry registry = null;
	
	private static boolean shouldPush = false;
	
	private static boolean shouldPoll = false;
	
	/**分布式锁 抢到锁的节点发布本节点信息到service目录下的Node下 其它的节点读取service目录的data值**/
	private static final String LOCK_NAME = "/service_lock";
	
	/**发布者节点父目录 所以的发布者全部在此目录下创建子节点**/
	private static final String PROVIDER_PATH = "/providers";
	
	//private static final LinkedBlockingQueue<BeanNodeWrapperDto> publishQueue = new LinkedBlockingQueue<>();
	
	private static AtomicInteger beanSize = new AtomicInteger(0);

	private ServerBeanContext() {
	}

	private static volatile boolean isDone = false;


	/**
	 * 
	 * @Date:2017年6月3日上午9:25:13
	 * @param version
	 * @param beanName
	 * @param beanId
	 */
	public static synchronized void pubVersionBean(String version,String beanName,String beanId) {
		versionBeans.add(new VersionBean(version,beanName,beanId));
		beanSize.incrementAndGet();
	}
	
	public static synchronized <T>  void pushBean(Function<VersionBean, Object> fun)
	{
		versionBeans.forEach(v->{
			Object bean = null;
			try {
				bean = fun.apply(v);
			} catch (Exception e) {
				e.printStackTrace();
			}
			v.setBean(bean);
		});
	}
	
	public static void shouldPush(boolean push)
	{
		shouldPush = push;
	}
	
	public static boolean shouldPush()
	{
		return shouldPush;
	}
	
	public static boolean shouldPoll()
	{
		return shouldPoll;
	}
	
	public static void shouldPoll(boolean poll)
	{
		shouldPoll = poll;
	}
	

	public static synchronized void init() throws Exception {

		if (isDone) {
			return;
		}

		registry = CuratorClient.getInstance();

		versionBeans.forEach(v->{
			generateSeq(v);
		});
		

		//开启端口监听
		Acceptor.acceptor();
		
		//发布到注册中心
		pushToRegistry();
		
		isDone = true;

	}
	
	public static Object invokeMethod(String version,Integer beanSeq,Integer methodSeq,Object[] args)
	{
		ServerVersionWrapper wrap = beanVersionMap.get(version);
		if(wrap == null)
		{
			//TODO 
		}
		return wrap.invokeMethod(beanSeq,methodSeq, args);
	}

//	private static void createVersionPath(String versionPath) {
//		registryClient.create(versionPath);
//	}

	private static void createServicePath(String servicePath,BeanNodeWrapperDto dto) throws Exception {
		
		byte[] bs = JSON.toJSONBytes(dto);
		
		//distributedLock(servicePath,dto);
		
		try {
			if(lock(servicePath, dto)){
				registry.restry(new ZkRegistryBean(servicePath,bs,CreateMode.PERSISTENT));
			}
			
		} catch (NodeExistsException e) {
			e.printStackTrace();
			try {
				registry.setData(new ZkRegistryBean(servicePath,bs));
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}catch (Exception e) {
			throw new Exception(e.fillInStackTrace());
		}
	}

	
	private static synchronized void generateSeq(VersionBean v) {

		Class<?> beanCla = v.getBean().getClass();

		ServerVersionWrapper vWrap = new ServerVersionWrapper(v.getVersion());
		beanVersionMap.put(v.getVersion(), vWrap);
		
		
		ServerBeanWrapper wrap = vWrap.initVersionBean(beanSeq, v.getInterfaceName(), v.getBean());
		beanSeq++;
		
		/**
		 * 获取公共方法
		 */
		Method[] methods = beanCla.getDeclaredMethods();
		
		for (Method method : methods) {
			String methodName = MethodUtil.getMethodNameWithType(method);
			
			wrap.putMethod(methodSeq, new ServerMethodWrapper(methodSeq, methodName, method));

			methodSeq++;
		}

	}
	
	private static void pushToRegistry() throws Exception{
		
		
		for(Entry<String, ServerVersionWrapper> entry:beanVersionMap.entrySet())
		{
			String version = entry.getKey();
			ServerVersionWrapper vBeanWrap = entry.getValue();
			
			vBeanWrap.pushBeanNodeDatas((nodeWrap)->{
				String servicePath = "/"+version+"/"+nodeWrap.getInterfaceName();
				/*发布bean与方法编号数据*/
				createServicePath(servicePath,nodeWrap);
				/*添加服务提供者*/
				pushProvider(servicePath);
				return null;
			});
		}
		
		beanVersionMap.forEach((version,vBeanWrap)->{
			
		});
		
	}
	
	private static void pushProvider(String servicePath)
	{
		String providerPath = servicePath+PROVIDER_PATH+"/"+Acceptor.getProvider();
		try {
			registry.restry(new ZkRegistryBean(providerPath));
		} catch (Exception e) {
			e.printStackTrace();//TODO
		}
	}
	
	private  static boolean lock(String servicePath,BeanNodeWrapperDto dto) throws Exception
	{
	//	publishQueue.peek()
		
		String lockPath = servicePath+LOCK_NAME;
		try {
			registry.restry(new ZkRegistryBean(lockPath));
			return true;
		}catch(NodeExistsException e)
		{
			e.printStackTrace();
			
			byte[] datas = registry.getData(new ZkRegistryBean(lockPath, null, CreateMode.EPHEMERAL, ()->{
				
				try {
					 byte[] datas_ = registry.getData(servicePath);
					 BeanNodeWrapperDto nodeWrapperDto = JSON.parseObject(datas_, BeanNodeWrapperDto.class);
					 dto.setBeanSeq(nodeWrapperDto.getBeanSeq());
					 dto.setMethodNameSeq(nodeWrapperDto.getMethodNameSeq());
					 
					 beanSize.decrementAndGet();
					 
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				
			}));
			
			if(ArrayUtils.isNotEmpty(datas))
			{
				 BeanNodeWrapperDto nodeWrapperDto = JSON.parseObject(datas, BeanNodeWrapperDto.class);
				 dto.setBeanSeq(nodeWrapperDto.getBeanSeq());
				 dto.setMethodNameSeq(nodeWrapperDto.getMethodNameSeq());
				 
				 beanSize.decrementAndGet();
			}
			return false;
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		throw new Exception();
	}

}
