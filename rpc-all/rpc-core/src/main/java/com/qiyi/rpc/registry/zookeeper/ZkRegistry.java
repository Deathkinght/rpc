package com.qiyi.rpc.registry.zookeeper;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;

import com.qiyi.rpc.protocol.context.Callable;
import com.qiyi.rpc.registry.Registry;
import com.qiyi.rpc.registry.bean.RegistryBean;
import com.qiyi.rpc.registry.zookeeper.bean.ZkRegistryBean;

public abstract class ZkRegistry implements Registry{

	protected static String rootPath = "/qiyi_zk";

	/**
	 * 逗号
	 */
	protected static final byte COMMA_BYTES = 44;

	/**
	 * 默认回话超时时间 毫秒
	 */
	protected static volatile int defaultSessionTimeoutMs = 5000;

	/**
	 * 默认重试沉睡时间 毫秒
	 */
	protected static volatile int defaultRetrySleepTimeMs = 1000;

	/**
	 * 默认重试次数
	 */
	protected static volatile int defaultRetryMaxRetries = 3;

	protected static Object obj = new Object();

	protected static volatile String zkAddress = null;

	protected static volatile boolean isInit = false; 
	
	public static boolean getIsInit()
	{
		return isInit;
	}
	
	public static void setSessionTimeoutMs(int defaultSessionTimeoutMsInit) {
		defaultSessionTimeoutMs = defaultSessionTimeoutMsInit;
	}

	public static void setRetrySleepTimeMs(int defaultRetrySleepTimeMsInt) {
		defaultRetrySleepTimeMs = defaultRetrySleepTimeMsInt;
	}

	public static void setZkAddress(String zkAddressInit) {
		zkAddress = zkAddressInit;
	}

	public static void setZkMaxRetries(int retriesInit) {
		defaultRetryMaxRetries = retriesInit;
	}

	
	

	@Override
	public void restry(RegistryBean bean) throws Exception {
		
		ZkRegistryBean zkBean = (ZkRegistryBean) bean;
		
		if(ArrayUtils.isNotEmpty(zkBean.getDatas())&&zkBean.getCreateMode()!=null)
		{
			this.create(zkBean.getPath(),zkBean.getDatas(),zkBean.getCreateMode());
			return;
		}
		
		if(ArrayUtils.isNotEmpty(zkBean.getDatas()))
		{
			this.create(zkBean.getPath(),zkBean.getDatas());
			return;
		}
		
		if(zkBean.getCreateMode()!=null)
		{
			this.create(zkBean.getPath(),zkBean.getCreateMode());
			return;
		}
		
		this.create(zkBean.getPath());
	}

	@Override
	public void update(RegistryBean bean) throws Exception {
		this.update(((ZkRegistryBean)bean).getPath(), ((ZkRegistryBean)bean).getDatas());
	}

	@Override
	public void setData(RegistryBean bean) throws Exception {
		this.setData(((ZkRegistryBean)bean).getPath(), ((ZkRegistryBean)bean).getDatas());
	}

	@Override
	public Collection<String> getChildren(RegistryBean bean) throws Exception {
		return this.getProviders(((ZkRegistryBean)bean).getPath());
	}


	@Override
	public <T> T getData(String path)throws Exception  {
		return this.getData(path,null);
	}
	

	@Override
	public <T> T getData(RegistryBean bean) throws Exception {
		
		ZkRegistryBean zkBean = (ZkRegistryBean) bean;
		
		if(zkBean.getCall()==null){
			return this.getData(zkBean.getPath(),null);
		}
		
		return this.getData(zkBean.getPath(), null, zkBean.getCall());
	}

	public abstract void setData(String path,byte[] data) throws Exception ;

	public abstract <T> T getData(String path, Stat stat) throws Exception ;
	
	public abstract <T> T getData(String path, Stat stat,Callable call) throws Exception ;

	public abstract List<String> getProviders(String path) throws Exception;
	
	public abstract void create(String path) throws Exception;
	
	public abstract void create(String path,CreateMode mode) throws Exception;

	public abstract void create(String path, byte[] initBytes) throws Exception;
	
	public abstract void create(String path, byte[] initBytes,CreateMode mode) throws Exception;

	public abstract Stat update(String path, byte[] appendBytes)
			throws Exception;

	protected byte[] appendBytes(byte[] oldBytes, byte[] appendBytes) {

		if (oldBytes == null || oldBytes.length == 0)
			return appendBytes;

		byte[] newBytes = new byte[oldBytes.length + 1 + appendBytes.length];
		int k = 0;
		for (int i = 0; i < oldBytes.length; i++, k++) {
			newBytes[i] = oldBytes[i];
		}

		newBytes[k] = COMMA_BYTES;
		k++;

		for (int j = 0; k < newBytes.length; k++, j++) {
			newBytes[k] = appendBytes[j];
		}
		return newBytes;
	}

}
