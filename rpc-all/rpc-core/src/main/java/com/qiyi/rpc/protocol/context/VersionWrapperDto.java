package com.qiyi.rpc.protocol.context;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.zookeeper.CreateMode;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.client.pool.HandlerManager;
import com.qiyi.rpc.registry.Registry;
import com.qiyi.rpc.registry.zookeeper.CuratorClient;
import com.qiyi.rpc.registry.zookeeper.bean.ZkRegistryBean;

public class VersionWrapperDto {
	
	private  final Map<String,BeanNodeWrapperDto> nodeWrapperMap = new HashMap<>();
	
	private String version ; 
	
	private  volatile boolean init = false;
	
	
	public String getVersion() {
		return version;
	}


	public void setVersion(String version) {
		this.version = version;
	}
	

	public VersionWrapperDto(String version) {
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
		
		try{
			providers = registry.getChildren(new ZkRegistryBean(beanPath));
		}catch(Exception e)
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
		}
		
		initDto.putProviders(providers);
		
		HandlerManager.init(interfaceName,providers);
		
		//init success
		
	}
	
}

