package com.qiyi.rpc.constant;

import java.util.HashMap;
import java.util.Map;

/**
 * 
 * @author qiyi
 * 注册类型枚举
 *
 */
public enum RegistryMode {

    PERSISTENT (0),

    EPHEMERAL (1);
	
	private int value;
	
	private static final Map<Integer,RegistryMode> registries = new HashMap<>();

	static{
		RegistryMode[] values = RegistryMode.values();
		for(RegistryMode r : values)
		{
			registries.put(r.getValue(),r);
		}
	}
	
	private RegistryMode(int value) {
		this.value = value;
	}
	
	public int getValue()
	{
		return this.value;
	}
	
	public static RegistryMode valueOf(int key){
		return registries.get(key);
	}
	
	
	
}
