package com.qiyi.rpc.spring.bean;

/**
 * 
 * @author qiyi
 *
 */
public class ClientFactoryBean extends QpcFactoryBean {

	/**
	 * 代理的接口名
	 */
	private String interfaceName;
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

}
