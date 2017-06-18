package com.qiyi.rpc.transport.protocol.context.client;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException.NoNodeException;
import org.apache.zookeeper.KeeperException.NodeExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.registry.Registry;
import com.qiyi.rpc.registry.zookeeper.CuratorClient;
import com.qiyi.rpc.registry.zookeeper.bean.ZkRegistryBean;
import com.qiyi.rpc.transport.client.pool.HandlerManager;
import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;
import com.qiyi.rpc.utils.AddrUtil;


public class ClientVersionWrapper {
	
	private  final Map<String,BeanNodeWrapperDto> nodeWrapperMap = new HashMap<>();
	
	private String version ; 
	
	private  volatile boolean init = false;
	
	private static Logger logger = LoggerFactory.getLogger(ClientVersionWrapper.class);
	
	
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}
	

	public ClientVersionWrapper(String version) {
		super();
		this.version = version;
	}


	/**
	 * 初始化interface
	 * @param interfaceName
	 */
	public  BeanNodeWrapperDto initInterface(String interfaceName)
	{
		BeanNodeWrapperDto initDto = new BeanNodeWrapperDto(interfaceName);
		nodeWrapperMap.put(interfaceName,initDto);
		return initDto;
	}
	
	
//	public   int getMethodId(String interfaceName,Method method)
//	{
//		BeanNodeWrapperDto beanIdMethodBean = beanMethodMap.get(interfaceName);
//		
//		if(beanIdMethodBean == null)
//		{
//			//TODO 
//		}
//		
//		String methodName = MethodUtil.getMethodNameWithType(method);
//		
//		return beanIdMethodBean.getMethodId(methodName);
//		
//	}
	
	public   BeanNodeWrapperDto getBeanMethod(String interfaceName)
	{
		return nodeWrapperMap.get(interfaceName);
	}
	
	public  synchronized void initAll()
	{
		if(!init)
		{
			nodeWrapperMap.forEach((k,v)->{
					try {
						init(k,v,false);
					} catch (Exception e) {
						e.printStackTrace();
					}	
				
			});
			
			init = true;
		}
		
	}
	
	private  void init(String interfaceName,BeanNodeWrapperDto initDto,boolean checkProvider) throws Exception
	{
		
		Registry registry = CuratorClient.getInstance();
		
		String beanPath = "/"+version+"/"+interfaceName;
		byte[] bytes = null;
		
		try {
			bytes = registry.getData(beanPath);
		} catch (Exception e) {
			e.printStackTrace();
			if(checkProvider)
			{
				throw new Exception("no provider");
			}
			
			
			//registry.getClass().newInstance()
			registry.watch(new ZkRegistryBean(beanPath, CreateMode.PERSISTENT, ()->{
				try {
					this.init(interfaceName, initDto, checkProvider);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}));
		}
		
		if(ArrayUtils.isEmpty(bytes))
		{
			//TODO 
		}
		
		BeanNodeWrapperDto nodeWrapper = JSON.parseObject(bytes,BeanNodeWrapperDto.class);
		
		initDto.setBeanSeq(nodeWrapper.getBeanSeq());
		initDto.setMethodNameSeq(nodeWrapper.getMethodNameSeq());
		
		Collection<String> providers = null;
		
		String consumersPath = beanPath+"/consumers";
		String providersPath = beanPath+"/providers";
		boolean hasConsumer = false;
		try{
			
			providers = registry.getChildren(new ZkRegistryBean(providersPath));
			
			
			ZkRegistryBean regBean = new ZkRegistryBean(consumersPath,CreateMode.PERSISTENT);
			registry.restry(regBean);
			
			hasConsumer = true;
			
			String consumerPath = consumersPath+"/"+AddrUtil.getLocalAddr();
			ZkRegistryBean consuBean = new ZkRegistryBean(consumerPath,CreateMode.EPHEMERAL);
			registry.restry(consuBean);
			
			
		}catch(NodeExistsException e)
		{
			//consumers目录存在 
			if(!hasConsumer){
			String consumerPath = consumersPath+"/"+AddrUtil.getLocalAddr();
			try{
					ZkRegistryBean consuBean = new ZkRegistryBean(consumerPath,CreateMode.EPHEMERAL);
					registry.restry(consuBean);
				
			}catch(NodeExistsException e2)
			{
				logger.warn("consumer node is exist:{}",consumerPath);
			}
			}
			
			
		}
		catch(NoNodeException  e)
		{
			e.printStackTrace();
			if(checkProvider){
				throw new Exception("no provider");
			}
			registry.watch(new ZkRegistryBean(beanPath, CreateMode.PERSISTENT, ()->{
				try {
					this.init(interfaceName, initDto, checkProvider);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}));
		}catch (Exception e) {
			// TODO: handle exception
		}
		
		initDto.putProviders(providers);
		
		HandlerManager.init(interfaceName,providers);
		
		//init success
		
	}
	
}

