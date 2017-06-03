package com.qiyi.rpc.demo.client.start;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javassist.ClassPool;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.qiyi.rpc.demo.client.context.TestBeanContext;
import com.qiyi.rpc.demo.client.dto.Context;
import com.qiyi.rpc.demo.client.dto.InterfaceWrapper;
import com.qiyi.rpc.demo.client.dto.MethodWrapper;
import com.qiyi.rpc.demo.client.dto.ParamWrapper;

@Component
public class DemoListener implements ApplicationListener<ApplicationEvent> {
	
	ClassPool pool = ClassPool.getDefault();
	
	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		if (event instanceof ContextRefreshedEvent) {

			ContextRefreshedEvent contextEvent = (ContextRefreshedEvent) event;

			ApplicationContext context = contextEvent.getApplicationContext();

//			String[] names = context.getBeanDefinitionNames();
//
//			for (String name : names) {
//				Object bean = context.getBean(name);
//				if(bean!=null)
//				{
//					init(name, bean, bean.getClass());
//				}
//			}
			
			TestBeanContext testBeanContext = context.getBean(com.qiyi.rpc.demo.client.context.TestBeanContext.class);
			
			Map<String, Object> testBeans = testBeanContext.getTestBeans();
			
			testBeans.forEach((k,v)->{
				init(k, v);
			});
			

		}

	}

	private void init(String interfaceName, Object bean) {

		Map<String, MethodWrapper> mrs = new HashMap<>();
		
		
//		try {
//		 CtClass ctCla = 	pool.get(bean.getClass().getName());
//		 
//		 CtMethod[] ctMethods =  ctCla.getDeclaredMethods();
//		 for(CtMethod m : ctMethods)
//		 {
//			 MethodInfo methodsInfo = m.getMethodInfo();
//			 
//			 methodsInfo.
//		 }
//		 
//		} catch (NotFoundException e) {
//			e.printStackTrace();
//		}
		
		Method[] ms =  bean.getClass().getDeclaredMethods();
		for (Method m : ms) {
			if(checkExcludeMethod(m))
			{
				continue;
			}
			MethodWrapper mr = getMethodWrapper(m);
			mrs.put(m.getName(), mr);
		}

		InterfaceWrapper ir = new InterfaceWrapper(interfaceName, bean, mrs);
		Context.put(interfaceName, ir);
	}

	private MethodWrapper getMethodWrapper(Method method) {

		Map<String, ParamWrapper> ps = new HashMap<>();
		List<ParamWrapper> peWr = new ArrayList<>();
		getMethodParameters(method,ps,peWr);
		return new MethodWrapper(method, ps,peWr, method.getName());
	}

	private void getMethodParameters(Method method,Map<String, ParamWrapper> psMap,List<ParamWrapper> peWr) {
		Parameter[] ps = method.getParameters();
		//List<String> paramNames = new ArrayList<>();
		
//		Annotation parameterAnnotations[][]  = method.getParameterAnnotations();
//		for (int i = 0; i < parameterAnnotations.length; i++) {  
//            for (Annotation annotation : parameterAnnotations[i]) {  
//                if (Param.class.equals(annotation.annotationType())) {  
//                    System.out.print(((Param) annotation).value() + ' ');  
//                    paramNames.add(((Param) annotation).value());
//                }  
//            }  
//        }  
		for (Parameter p : ps) {
			ParamWrapper pp = new ParamWrapper(p.getName(), p.getType().getName(),p.getType());
			peWr.add(pp);
			psMap.put(p.getName(), pp);
		}

	}
	
	private boolean checkExcludeMethod(Method m)
	{
		if(m.getName().equals("toString")||m.getName().equals("equals")
				||m.getName().equals("hashCode"))
		{
			return true;
		}
		return false;
		
	}

}
