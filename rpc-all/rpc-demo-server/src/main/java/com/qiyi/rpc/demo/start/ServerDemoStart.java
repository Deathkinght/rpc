package com.qiyi.rpc.demo.start;

import org.springframework.context.support.ClassPathXmlApplicationContext;


public class ServerDemoStart {

	@SuppressWarnings("resource")
	public static void main(String[] args) {

		System.out.println("start");
		new ClassPathXmlApplicationContext("qiyi-server.xml");
		System.out.println("start success");
		
		try {
			Thread.sleep(Integer.MAX_VALUE);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
