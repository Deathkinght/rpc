package com.qiyi.rpc.protocol.context;

import java.util.HashMap;
import java.util.Map;

public class ClientBeanContext {

	private static  Map<String,VersionWrapperDto> context = new HashMap<>();
	

	public static BeanNodeWrapperDto initVersions(String version,String interfaceName)
	{
		
		VersionWrapperDto versionWrapper = context.get(version);
		
		if(versionWrapper == null)
		{
			versionWrapper = new VersionWrapperDto(version);
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
