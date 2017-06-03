package com.qiyi.rpc.spring;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import com.qiyi.rpc.protocol.context.ClientBeanContext;
import com.qiyi.rpc.protocol.context.server.ServerBeanContext;

public class QiyiApplicationListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
	
		if (event instanceof ContextRefreshedEvent) {

			ContextRefreshedEvent contextEvent = (ContextRefreshedEvent) event;

			ApplicationContext context = contextEvent.getApplicationContext();

			/** 是否需要注册 **/
			if (ServerBeanContext.shouldPush()) {

				ServerBeanContext.pushBean((v)->{
					return context.getBean(v.getBeanId());
				});
				
				try {
					ServerBeanContext.init();
				} catch (Exception e) {
					e.printStackTrace();
					//TODO 异常处理
					
				}

			}
			
			/**是否需要从注册中心获取**/
			if (ServerBeanContext.shouldPoll()) {

				ClientBeanContext.initAll();
				
			}

			System.out.println("=============================finishAll=================================");
		}

	}

}
