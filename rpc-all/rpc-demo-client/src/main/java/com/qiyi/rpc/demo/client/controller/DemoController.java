package com.qiyi.rpc.demo.client.controller;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.demo.client.dto.Context;
import com.qiyi.rpc.demo.client.dto.InterfaceWrapper;
import com.qiyi.rpc.demo.client.dto.InvokeReqDto;
import com.qiyi.rpc.demo.client.dto.MethodWrapper;
import com.qiyi.rpc.demo.client.dto.ParamWrapper;

@Controller
public class DemoController {
	
	
	
	@RequestMapping("index")
	//@ResponseBody
	public String index(HttpServletRequest req)
	{
		//m.setViewName("index");
		req.setAttribute("beans", Context.getAll());
		return "index";
		//return JSON.toJSONString(Context.getAll());
	}
	
	@RequestMapping("invoke")
	@ResponseBody
	public Object invoke(InvokeReqDto invoke) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException
	{
		InterfaceWrapper beanWrapper = Context.get(invoke.getInterfaceName());
		Object bean = beanWrapper.getBean();
		MethodWrapper methodWrapper = beanWrapper.getMethod(invoke.getMethodName());
		Method method = methodWrapper.getMethod();
		
		Object[] objs = null;
		String arg = invoke.getArg();
		if(StringUtils.isNotBlank(arg))
		{
			String[] args = arg.split(",");
			
			objs  = new Object[args.length];
			for(int i=0;i<args.length;i++)
			{
				String s = args[i];
				
				ParamWrapper pwr = methodWrapper.getParamWrapper(i);
				Class<?> paramCla = pwr.getParamTypeCla();
				
				Object obj = getObj(paramCla, s);
				objs[i] = obj;
			}
		}
		
		Object result =  method.invoke(bean,objs); 
	    return result!=null?JSON.toJSONString(result):"";
	}
	
	
	@RequestMapping("test")
	@ResponseBody
	public String test(HttpServletRequest req)
	{
		return "test";
	}
	
	public Object getObj(Class<?> cla,String s){
		
		if(cla.equals(Integer.class))
		{
			return Integer.valueOf(s);
		}
		
		if(cla.equals(int.class))
		{
			return Integer.parseInt(s);
		}
		
		if(cla.equals(double.class))
		{
			return Double.parseDouble(s);
		}
		
		if(cla.equals(Double.class))
		{
			return Double.valueOf(s);
		}
		
		if(cla.equals(float.class))
		{
			return Float.parseFloat(s);
		}
		
		if(cla.equals(Float.class))
		{
			return Float.valueOf(s);
		}
		
		
		if(cla.equals(byte.class))
		{
			return Byte.parseByte(s);
		}
		
		if(cla.equals(Byte.class))
		{
			return Byte.valueOf(s);
		}
		
		if(cla.equals(boolean.class))
		{
			return Boolean.parseBoolean(s);
		}
		
		if(cla.equals(Boolean.class))
		{
			return Boolean.valueOf(s);
		}
		
		if(cla.equals(Date.class))
		{
			
			try {
				return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(s);
			} catch (ParseException e) {
				e.printStackTrace();
			} 
		}
			
		return s;
	}
	public static void main(String[] args) {
		DemoController d = new DemoController();
		Class<?> cla = d.getClass();
		try {
			Class<?>[] tt = {int.class,String.class,String.class,boolean.class}; 
			Method  m = cla.getDeclaredMethod("aaa",tt);
			Object[] aaa = {12,"a","b",true};
			Object a = m.invoke(d, aaa);
			System.out.println(a);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
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
	
	public int aaa(int a,String b,String c,boolean d)
	{
		return a;
	}
	

}
