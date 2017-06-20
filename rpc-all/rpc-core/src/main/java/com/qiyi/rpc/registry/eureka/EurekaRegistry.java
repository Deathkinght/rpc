package com.qiyi.rpc.registry.eureka;

import java.util.Collection;

import com.qiyi.rpc.registry.Registry;
import com.qiyi.rpc.registry.bean.RegistryBean;

public class EurekaRegistry implements Registry {

	@Override
	public void restry(RegistryBean bean) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public <T, R> void watch(RegistryBean bean) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void update(RegistryBean bean) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public void setData(RegistryBean bean) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public <T> T getData(String path) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <T> T getData(RegistryBean bean) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Collection<String> getChildren(RegistryBean bean) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

}
