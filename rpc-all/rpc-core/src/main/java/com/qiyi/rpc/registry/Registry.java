package com.qiyi.rpc.registry;

import java.util.Collection;

import com.qiyi.rpc.registry.bean.RegistryBean;

public interface Registry {

	 void  restry(RegistryBean bean) throws Exception;

	<T, R> void watch(RegistryBean bean) throws Exception;

	 void update(RegistryBean bean) throws Exception;

	 void setData(RegistryBean bean) throws Exception;

	<T> T getData(String path) throws Exception;
	
	<T> T getData(RegistryBean bean) throws Exception;

	 Collection<String> getChildren(RegistryBean bean) throws Exception;
	
}
