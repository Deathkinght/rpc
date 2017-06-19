package com.qiyi.rpc.registry.zookeeper;

import java.util.List;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.Watcher.Event.EventType;
import org.apache.zookeeper.data.Stat;

import com.qiyi.rpc.registry.bean.RegistryBean;
import com.qiyi.rpc.registry.zookeeper.bean.ZkRegistryBean;
import com.qiyi.rpc.transport.protocol.context.Callable;

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
			client.create().withMode(CreateMode.PERSISTENT).forPath(ROOT_PATH);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public List<String> getProviders(String path) throws Exception {

		return client.getChildren().forPath(ROOT_PATH + path);

	}

	@SuppressWarnings("unchecked")
	@Override
	public byte[] getData(String path, Stat stat) {

		try {
			byte[] bytes = null;
			if (stat != null) {
				bytes = client.getData().storingStatIn(stat).forPath(ROOT_PATH + path);
			} else {
				bytes = client.getData().forPath(ROOT_PATH + path);
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

		return (T) client.getData().usingWatcher(watcher).forPath(ROOT_PATH + path);
	}

	@Override
	public void create(String path) throws Exception {

		client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(ROOT_PATH + path);

	}

	@Override
	public void create(String path, byte[] initBytes) throws Exception {
		create(path, initBytes, CreateMode.EPHEMERAL);
	}

	@Override
	public void create(String path, byte[] initBytes, CreateMode mode) throws Exception {
		client.create().creatingParentsIfNeeded().withMode(mode).forPath(ROOT_PATH + path, initBytes);
	}

	@Override
	public Stat update(String path, byte[] appendBytes) throws Exception {
		Stat oldStat = new Stat();
		byte[] oldBytes = getData(path, oldStat);
		byte[] newBytes = appendBytes(oldBytes, appendBytes);

		Stat newStat = client.setData().withVersion(oldStat.getVersion()).forPath(ROOT_PATH + path, newBytes);
		return newStat;
	}

	@Override
	public void setData(String path, byte[] data) throws Exception {

		client.setData().forPath(ROOT_PATH + path, (byte[]) obj);
	}

	@Override
	public <T, R> void watch(RegistryBean bean) throws Exception {

		ZkRegistryBean zkBean = (ZkRegistryBean) bean;
		
		Watcher watcher = (e) -> {

			if (e.getType() == EventType.NodeCreated ||e.getType() == EventType.NodeChildrenChanged || e.getType() == EventType.NodeDataChanged) {
				zkBean.getCall().call();
			}
		};

		client.getData().usingWatcher(watcher).forPath(ROOT_PATH + zkBean.getPath());

	}

	@Override
	public void create(String path, CreateMode mode) throws Exception {
		client.create().withMode(mode).forPath(ROOT_PATH+path);
	}
	
}
