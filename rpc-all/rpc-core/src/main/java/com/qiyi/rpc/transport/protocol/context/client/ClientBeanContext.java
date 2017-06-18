package com.qiyi.rpc.transport.protocol.context.client;

import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;

public class ClientBeanContext {

	private static  Map<String,ClientVersionWrapper> context = new HashMap<>();
	

	public static BeanNodeWrapperDto initVersions(String version,String interfaceName)
	{
		
		ClientVersionWrapper versionWrapper = context.get(version);
		
		if(versionWrapper == null)
		{
			versionWrapper = new ClientVersionWrapper(version);
			 context.put(version, versionWrapper);
			 
		}
		
		return versionWrapper.initInterface(interfaceName);
	}
	
	public static void initAll()
	{
		
		context.forEach((version,wrapper)->{
			wrapper.initAll();
		});
		
	}
	
	
}
