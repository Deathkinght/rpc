package com.qiyi.rpc.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.qiyi.rpc.spring.bean.ClientFactoryBean;
import com.qiyi.rpc.spring.bean.ConfigFactoryBean;
import com.qiyi.rpc.spring.bean.ServerFactoryBean;


public class QiyiNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("client", new QiyiBeanParser(ClientFactoryBean.class));
		registerBeanDefinitionParser("server", new QiyiBeanParser(ServerFactoryBean.class));
		registerBeanDefinitionParser("config", new QiyiBeanParser(ConfigFactoryBean.class));
	}

}
