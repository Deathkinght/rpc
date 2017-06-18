package com.qiyi.rpc.spring.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.qiyi.rpc.constant.Constant;

/**
 * 
 * @author qiyi
 *
 */
public class QpcFactoryBean implements FactoryBean<Object>, ApplicationContextAware, InitializingBean, DisposableBean {

	/**
	 * 实际返回的bean
	 */
	protected Object proxy;
	
	
	/**
	 * 服务分组
	 */
	protected String group ;
	
	/**
	 * 服务版本
	 */
	protected String version = Constant.DEFAULT_VERSION;
	
	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Object getProxy() {
		return proxy;
	}

	public void setProxy(Object proxy) {
		this.proxy = proxy;
	}

	@Override
	public Object getObject() throws Exception {
		return proxy;
	}

	@Override
	public Class<?> getObjectType() {
		return proxy.getClass();
	}

	@Override
	public boolean isSingleton() {
		return false;
	}

	@Override
	public void destroy() throws Exception {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		
	}

	public String getGroup() {
		return group;
	}

	public void setGroup(String group) {
		this.group = group;
	}

}
