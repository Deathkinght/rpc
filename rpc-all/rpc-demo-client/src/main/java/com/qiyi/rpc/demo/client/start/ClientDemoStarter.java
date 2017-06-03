package com.qiyi.rpc.demo.client.start;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Scanner;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.alibaba.fastjson.JSON;


public class ClientDemoStarter {

	@SuppressWarnings("resource")
	public static void main(String[] args) throws InterruptedException {

		//String[] hosts = { "localhost:8088" };
		//System.out.println(UserService.class.getName());
		//ServiceHostContext.setServiceHost(UserService.class.getName(), hosts);
		//HandlerManager.init(hosts);
		
		ApplicationContext ac = new ClassPathXmlApplicationContext(  
                "classpath:qiyi-client.xml");  
  
		//Thread.sleep(Integer.MAX_VALUE);
		
//		UserService user = (UserService) ac.getBean("userService");  
//        System.out.println(JSON.toJSONString(user.getUser(12)));
        
		
		while(true)
		{
			 	Scanner input = new Scanner(System.in);
		        String serviceName = input.nextLine();
		        if("stop".equalsIgnoreCase(serviceName))
		        {
		        	break;
		        }
		        Object bean = ac.getBean(serviceName);
				String methodName = input.nextLine();
				Method[] ms = bean.getClass().getDeclaredMethods();
				Method method = null;
		        for(Method m : ms)
		        {
		        	if(m.getName().equals(methodName))
		        	{
		        		method = m;
		        	}
		        }
		        String jsonArgs = input.nextLine();
		        jsonArgs = "["+jsonArgs+"]";
		        try {
					Object result = method.invoke(bean, JSON.parseObject(jsonArgs, Object[].class));
					System.out.println(JSON.toJSONString(result));
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		
	}

}
