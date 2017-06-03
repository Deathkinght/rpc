package com.qiyi.rpc.client.pool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceHostContext {

	private static final Map<String,ServiceHost> service = new HashMap<>();
	
	private static volatile boolean hasInit = false;
	
	
	public static ServiceHost getServiceHost(String serviceName)
	{
		return service.get(serviceName);
	}
	
	public static synchronized void init(String zkIp) {

		if (hasInit)
			return;

	}
	
	public static void  setServiceHost(String serviceName,Iterable<String> hosts)
	{
		
		ServiceHost serviceHost = new ServiceHost();
		
		List<Host> hostList = new ArrayList<>();
		
		hosts.forEach(h->{
			Host host = new Host();
			String[] hs = h.split(":");
			host.setIp(hs[0]);
			host.setPort(Integer.valueOf(hs[1]));
			hostList.add(host);
		});
		
		serviceHost.setHost(hostList);
		serviceHost.setServiceName(serviceName);
		service.put(serviceName, serviceHost);
	}
	
}
