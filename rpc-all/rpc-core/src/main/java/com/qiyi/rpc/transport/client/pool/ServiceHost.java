package com.qiyi.rpc.transport.client.pool;

import java.util.List;

public class ServiceHost{
	/**
	 * 服务名
	 */
	String serviceName;
	/**
	 * 服务的ip列表
	 */
	List<Host> hosts;
	
	//List<ClientHandler> handlerList = new ArrayList<>();
	
//	public void caculateWeight()
//	{
//		hosts.stream().filter((each)->{
//			//TODO
//			return true;
//		}).sorted((host1,host2)->{
//			return host1.getWeight()-host2.getWeight();
//		});
//	}
	
	public Host getHost()
	{
		return hosts.get(0);
	}
	
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public List<Host> getHosts() {
		return hosts;
	}
	public void setHost(List<Host> hosts) {
		this.hosts = hosts;
	}


	
}