package com.qiyi.rpc.demo.client.dto;

import java.util.HashMap;
import java.util.Map;

public class InterfaceWrapper {

	private transient String interfaceName ;
	
	private transient Object bean;
	
	private Map<String, MethodWrapper> methods = new HashMap<>();

	public InterfaceWrapper(String interfaceName, Object bean, Map<String, MethodWrapper> methods) {
		super();
		this.interfaceName = interfaceName;
		this.bean = bean;
		this.methods = methods;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public Map<String, MethodWrapper> getMethods() {
		return methods;
	}

	public void setMethods(Map<String, MethodWrapper> methods) {
		this.methods = methods;
	}

	public Object getBean() {
		return bean;
	}

	public void setBean(Object bean) {
		this.bean = bean;
	}
	
	public MethodWrapper getMethod(String methodName)
	{
		return this.methods.get(methodName);
	}
	
	
}
