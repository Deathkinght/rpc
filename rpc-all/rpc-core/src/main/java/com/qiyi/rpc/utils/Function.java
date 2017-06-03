package com.qiyi.rpc.utils;

@FunctionalInterface
public interface Function<T,R> {

	R apply(T t) throws Exception;
	
}
