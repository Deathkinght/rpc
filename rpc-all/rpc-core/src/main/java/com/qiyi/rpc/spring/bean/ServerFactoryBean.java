package com.qiyi.rpc.spring.bean;

/**
 * 
 * @author qiyi
 *
 */
public class ServerFactoryBean extends QiyiFactoryBean{

	private Object refer;
	
	private String interfaceName;

	public Object getRefer() {
		return refer;
	}

	public void setRefer(Object refer) {
		this.refer = refer;
	}
	
	
	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	@Override
	public Object getObject() throws Exception {
		return refer;
	}

	@Override
	public Class<?> getObjectType() {
		return refer.getClass();
	}
	
}
