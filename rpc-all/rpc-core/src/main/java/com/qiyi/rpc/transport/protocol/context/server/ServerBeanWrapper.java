package com.qiyi.rpc.transport.protocol.context.server;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;

public class ServerBeanWrapper {

	private int seq;
	
	private String interfaceName;
	
	private Object bean;
	
	private Map<Integer, ServerMethodWrapper> methodWrapper = new HashMap<>();
	
	private BeanNodeWrapperDto beanNodeWrap ;
	
	public void putMethod(Integer methodSeq,ServerMethodWrapper methodWrap)
	{
		methodWrapper.put(methodSeq, methodWrap);
		
		beanNodeWrap.putMethodNameSeq(methodWrap.getMethodName(), methodSeq);
	}
	
	public Object invokeMethod(Integer methodId,Object[] args)
	{
		ServerMethodWrapper methodBean = methodWrapper.get(methodId);
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

	public ServerBeanWrapper() {
		super();
	}
	
	public ServerBeanWrapper(int seq, String interfaceName, Object bean,BeanNodeWrapperDto beanNodeWrap) {
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
