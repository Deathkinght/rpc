package com.qiyi.rpc.demo.client.dto;

import java.util.HashMap;
import java.util.Map;

public class Context {

	private static final Map<String, InterfaceWrapper> interfaces = new HashMap<>();
	
	
	public static void put(String beanDefName,InterfaceWrapper wrapper)
	{
		interfaces.put(beanDefName, wrapper);
	}
	
	public static InterfaceWrapper get(String beanDefName)
	{
		return interfaces.get(beanDefName);
	}
	
	public static Map<String, InterfaceWrapper> getAll()
	{
		return interfaces;
	}
	
}
