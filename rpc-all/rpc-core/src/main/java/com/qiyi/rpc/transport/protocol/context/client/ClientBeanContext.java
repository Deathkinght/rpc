package com.qiyi.rpc.transport.protocol.context.client;

import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.transport.client.ClientProxy;
import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;

public class ClientBeanContext {

	private static  Map<String,ClientVersionWrapper> context = new HashMap<>();
	
	private static volatile boolean shouldPoll = false;

	/**
	 * 实例化bean代理
	 * @Date:2017年6月18日上午9:29:29
	 * @param version
	 * @param interfaceName
	 * @return
	 * @throws IllegalArgumentException
	 * @throws ClassNotFoundException
	 */
	public static Object initBeanProxy(String version,String interfaceName,boolean checkRegistry) throws IllegalArgumentException, ClassNotFoundException{

		shouldPoll = true;
		
		BeanNodeWrapperDto wrapperDto = ClientBeanContext.initBeanNode(version, interfaceName,checkRegistry);
		
		return new ClientProxy(interfaceName,wrapperDto).getObject();
	}
	
	/**
	 * 初始化beanNode
	 * @Date:2017年6月18日上午9:29:16
	 * @param version
	 * @param interfaceName
	 * @return
	 */
	private static BeanNodeWrapperDto initBeanNode(String version,String interfaceName,boolean checkRegistry)
	{
		
		ClientVersionWrapper versionWrapper = context.get(version);
		if(versionWrapper == null)
		{
			 versionWrapper = new ClientVersionWrapper(version);
			 context.put(version, versionWrapper);
			 
		}
		
		return versionWrapper.initBeanNode(interfaceName,checkRegistry);
	}
	
	/**
	 * 
	 * @Date:2017年6月18日上午9:30:11
	 */
	public static void fetchFromRegistry()
	{
		if(shouldPoll)
		{
			context.forEach((version,wrapper)->{
				wrapper.refreshAll();
			});			
		}
		
	}
	
	
}
