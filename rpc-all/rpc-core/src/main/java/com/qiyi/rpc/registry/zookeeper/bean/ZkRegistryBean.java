package com.qiyi.rpc.registry.zookeeper.bean;

import org.apache.zookeeper.CreateMode;

import com.qiyi.rpc.registry.bean.RegistryBean;
import com.qiyi.rpc.transport.protocol.context.Callable;

public class ZkRegistryBean extends RegistryBean{

	private String path ;
	
	private byte[] datas;
	
	private CreateMode createMode;
	
	private Callable call;

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public byte[] getDatas() {
		return datas;
	}

	public void setDatas(byte[] datas) {
		this.datas = datas;
	}

	public CreateMode getCreateMode() {
		return createMode;
	}

	public void setCreateMode(CreateMode createMode) {
		this.createMode = createMode;
	}

	public Callable getCall() {
		return call;
	}

	public void setCall(Callable call) {
		this.call = call;
	}

	

	public ZkRegistryBean(String path) {
	this(path, null, null, null);
	}

	public ZkRegistryBean(String path, byte[] datas) {
		this(path, datas, null, null);
	}

	public ZkRegistryBean(String path, CreateMode createMode) {
		this(path, null, createMode, null);
	}
	
	

	public ZkRegistryBean(String path, CreateMode createMode, Callable call) {
		this(path, null, createMode, call);
	}

	public ZkRegistryBean(String path, byte[] datas, Callable call) {
		this(path, datas, null, call);
	}

	public ZkRegistryBean(String path, byte[] datas, CreateMode createMode) {
		this(path, datas, createMode, null);
	}

	public ZkRegistryBean(String path, byte[] datas, CreateMode createMode,
			Callable call) {
		super();
		this.path = path;
		this.datas = datas;
		this.createMode = createMode;
		this.call = call;
	}
	
	
	
}
