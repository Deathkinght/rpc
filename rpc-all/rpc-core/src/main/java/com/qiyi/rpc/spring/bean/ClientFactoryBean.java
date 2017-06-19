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
	
	private boolean check;
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public boolean isCheck() {
		return check;
	}

	public void setCheck(boolean check) {
		this.check = check;
	}

}
