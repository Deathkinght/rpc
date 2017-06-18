package com.qiyi.rpc.spring.bean;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import com.qiyi.rpc.constant.Constant;

/**
 * 代理接口类
 * @author qiyi
 *
 */
public class QpcProxy implements InvocationHandler {

	
	protected String interfaceName;
	
	protected String version = Constant.DEFAULT_VERSION;
	
	protected int beanId = -1;
	
	public QpcProxy() {
		super();
	}

	public QpcProxy(String interfaceName)
	{
		this.interfaceName = interfaceName;
	}
	
	public Object getObject() throws IllegalArgumentException, ClassNotFoundException
	{
		Object o =  Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{Class.forName(interfaceName)}, this);
	    return o;
	}
	
	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
		return null;
	}


	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public int getBeanId() {
		return beanId;
	}

	public void setBeanId(int beanId) {
		this.beanId = beanId;
	}

}
