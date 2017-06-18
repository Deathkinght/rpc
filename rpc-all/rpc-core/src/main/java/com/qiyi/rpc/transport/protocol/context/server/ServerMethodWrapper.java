package com.qiyi.rpc.transport.protocol.context.server;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ServerMethodWrapper {
	
	private int seq;

	private String methodName;

	private Method method;

	public int getSeq() {
		return seq;
	}

	public void setSeq(int seq) {
		this.seq = seq;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}

	public ServerMethodWrapper() {
	}

	public ServerMethodWrapper(int seq, String methodName, Method method) {
		this.seq = seq;
		this.methodName = methodName;
		this.method = method;
	}

	public Object invoke(Object bean, Object[] args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		return this.method.invoke(bean, args);
	}
}
