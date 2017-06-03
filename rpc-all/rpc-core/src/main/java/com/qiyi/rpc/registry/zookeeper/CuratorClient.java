package com.qiyi.rpc.registry.zookeeper;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import com.qiyi.rpc.protocol.context.Callable;
import com.qiyi.rpc.registry.bean.RegistryBean;
import com.qiyi.rpc.registry.zookeeper.bean.ZkRegistryBean;

/**
 * zookeeper客户端
 * 
 * @author qiyi
 *
 */
public class CuratorClient extends ZkRegistry {

	private static volatile CuratorFramework client = null;

	private static volatile CuratorClient curatorClient = null;

	public static CuratorClient getInstance() {

		if (curatorClient == null) {
			synchronized (obj) {
				if (curatorClient == null) {
					init();
					curatorClient = new CuratorClient();
				}
			}
		}
		return curatorClient;
	}

	private CuratorClient() {
	}

	private static void init() {
		isInit = true;
		client = CuratorFrameworkFactory.builder().connectString(zkAddress).sessionTimeoutMs(defaultSessionTimeoutMs).retryPolicy(new ExponentialBackoffRetry(defaultRetrySleepTimeMs, defaultRetryMaxRetries)).build();
		client.start();
		try {
			client.create().withMode(CreateMode.PERSISTENT).forPath(rootPath);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<String> getProviders(String path) throws Exception {

		return client.getChildren().forPath(rootPath + path);

	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] getData(String path, Stat stat) {

		try {
			byte[] bytes = null;
			if (stat != null) {
				bytes = client.getData().storingStatIn(stat).forPath(rootPath + path);
			} else {
				bytes = client.getData().forPath(rootPath + path);
			}

			return bytes;
		} catch (Exception e) {
			e.printStackTrace();
			// TODO
		}
		return null;
	}
	
	
	@Override
	public <T> T getData(String path, Stat stat, Callable call)
			throws Exception {
		
		Watcher watcher = (e) -> {

			if (e.getType() == EventType.NodeChildrenChanged || e.getType() == EventType.NodeDataChanged) {
				call.call();
			}
		};

		return (T) client.getData().usingWatcher(watcher).forPath(rootPath + path);
	}

	@Override
	public void create(String path) throws Exception {

		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(rootPath + path);

	}

	@Override
	public void create(String path, byte[] initBytes) throws Exception {
		create(path, initBytes, CreateMode.EPHEMERAL);
	}

	@Override
	public void create(String path, byte[] initBytes, CreateMode mode) throws Exception {
		client.create().creatingParentsIfNeeded().withMode(mode).forPath(rootPath + path, initBytes);
	}

	@Override
	public Stat update(String path, byte[] appendBytes) throws Exception {
		Stat oldStat = new Stat();
		byte[] oldBytes = getData(path, oldStat);
		byte[] newBytes = appendBytes(oldBytes, appendBytes);

		Stat newStat = client.setData().withVersion(oldStat.getVersion()).forPath(rootPath + path, newBytes);
		return newStat;
	}

	@Override
	public void setData(String path, byte[] data) throws Exception {

		client.setData().forPath(rootPath + path, (byte[]) obj);
	}

	@Override
	public <T, R> void watch(RegistryBean bean) throws Exception {

		ZkRegistryBean zkBean = (ZkRegistryBean) bean;
		
		Watcher watcher = (e) -> {

			if (e.getType() == EventType.NodeChildrenChanged || e.getType() == EventType.NodeDataChanged) {
				zkBean.getCall().call();
			}
		};

		client.getData().usingWatcher(watcher).forPath(rootPath + zkBean.getPath());

	}

	@Override
	public void create(String path, CreateMode mode) throws Exception {
		client.create().withMode(mode).forPath(path);
	}
	
}
