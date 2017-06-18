package com.qiyi.rpc.spring.schema;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

import com.qiyi.rpc.spring.bean.ClientFactoryBean;
import com.qiyi.rpc.spring.bean.ConfigFactoryBean;
import com.qiyi.rpc.spring.bean.ServerFactoryBean;


public class QpcNamespaceHandler extends NamespaceHandlerSupport {

	@Override
	public void init() {
		registerBeanDefinitionParser("client", new QpcBeanParser(ClientFactoryBean.class));
		registerBeanDefinitionParser("server", new QpcBeanParser(ServerFactoryBean.class));
		registerBeanDefinitionParser("config", new QpcBeanParser(ConfigFactoryBean.class));
	}

}
