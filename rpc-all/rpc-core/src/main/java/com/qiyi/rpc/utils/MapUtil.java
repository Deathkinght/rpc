package com.qiyi.rpc.utils;

import java.util.Map;

public class MapUtil {
	
	
	public static <K, V> boolean isEmpty(Map<K, V> map) {
		return map == null || map.size() == 0;
	}
}
