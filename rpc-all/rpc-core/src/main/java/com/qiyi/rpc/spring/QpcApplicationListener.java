package com.qiyi.rpc.spring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.qiyi.rpc.transport.protocol.context.client.ClientBeanContext;
import com.qiyi.rpc.transport.protocol.context.server.ServerBeanContext;

public class QpcApplicationListener implements ApplicationListener<ApplicationEvent> {

	private static final Logger logger = LoggerFactory.getLogger(QpcApplicationListener.class);
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
	
		if (event instanceof ContextRefreshedEvent) {

			ContextRefreshedEvent contextEvent = (ContextRefreshedEvent) event;

			ApplicationContext context = contextEvent.getApplicationContext();

			/** 注册到注册中心 **/
			try {
				ServerBeanContext.pushBean((v) -> {
					return context.getBean(v.getBeanId());
				});
			} catch (BeansException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			/**从注册中心获取**/
			ClientBeanContext.fetchFromRegistry();
				

			logger.info("=============================qpc started success=================================");
		}

	}

}
