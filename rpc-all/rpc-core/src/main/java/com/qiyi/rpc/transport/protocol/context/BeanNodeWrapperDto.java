package com.qiyi.rpc.transport.protocol.context;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.utils.MethodUtil;

public class BeanNodeWrapperDto {

	private int beanSeq;
	
	private transient String interfaceName ;

	private Map<String, Integer> methodNameSeq = new HashMap<>();

	private transient  Collection<String> providers = new ArrayList<>();
	
	private transient boolean checkRegistry;
	
	public void putProviders(Collection<String> providers_)
	{
		providers.addAll(providers_);
	}
	
	public int getMethodId(String methodName) {
		return methodNameSeq.get(methodName);
	}

	public int getMethodSeq(Method method) {
		String methodName = MethodUtil.getMethodNameWithType(method);

		return methodNameSeq.get(methodName);
	}
	
	public void putMethodNameSeq(String methodName,Integer methodId)
	{
		methodNameSeq.put(methodName, methodId);
	}

	public BeanNodeWrapperDto() {
		super();
	}
	
	public BeanNodeWrapperDto(String interfaceName) {
		super();
		this.interfaceName = interfaceName;
	}

	public BeanNodeWrapperDto(String interfaceName,boolean checkRegistry) {
		super();
		this.interfaceName = interfaceName;
		this.checkRegistry = checkRegistry;
	}

	public BeanNodeWrapperDto(int beanSeq, Map<String, Integer> methodNameSeq) {
		super();
		this.beanSeq = beanSeq;
		this.methodNameSeq = methodNameSeq;
	}

	public int getBeanSeq() {
		return beanSeq;
	}

	public void setBeanSeq(int beanSeq) {
		this.beanSeq = beanSeq;
	}

	public Map<String, Integer> getMethodNameSeq() {
		return methodNameSeq;
	}

	public void setMethodNameSeq(Map<String, Integer> methodNameSeq) {
		this.methodNameSeq = methodNameSeq;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public boolean isCheckRegistry() {
		return checkRegistry;
	}
	

}
