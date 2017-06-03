package com.qiyi.rpc.protocol.context.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.protocol.context.BeanNodeWrapperDto;

public class BeanWrapper {

	private int seq;
	
	private String interfaceName;
	
	private Object bean;
	
	private Map<Integer, MethodWrapper> methodWrapper = new HashMap<>();
	
	private BeanNodeWrapperDto beanNodeWrap ;
	
	public void putMethodBean(Integer methodSeq,MethodWrapper methodBean)
	{
		methodWrapper.put(methodSeq, methodBean);
		
		beanNodeWrap.putMethodNameSeq(methodBean.getMethodName(), methodSeq);
	}
	
	public Object invokeMethod(Integer methodId,Object[] args)
	{
		MethodWrapper methodBean = methodWrapper.get(methodId);
		if(methodBean == null)
		{
			//TODO
		}
		
		try {
			return methodBean.invoke(this.bean, args);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;//TODO 
		
	}

	public BeanWrapper() {
		super();
	}
	
	public BeanWrapper(int seq, String interfaceName, Object bean,BeanNodeWrapperDto beanNodeWrap) {
		super();
		this.seq = seq;
		this.interfaceName = interfaceName;
		this.bean = bean;
		this.beanNodeWrap = beanNodeWrap;
	}

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}


}
