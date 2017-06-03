package com.qiyi.rpc.registry.zookeeper;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;

public class DistributedLock implements Watcher {

	private CountDownLatch countDown;

	private static Object obj = new Object();
	
	private String lockRoot = null;

	/** 上一个等待锁 **/
	private String beforeLock;

	private static volatile ZooKeeper zk;

	/** 本节点 **/
	private String selfLock;

	private static Logger logger = LoggerFactory
			.getLogger(DistributedLock.class);

	@Override
	public void process(WatchedEvent event) {
		logger.info("节点:{}收到通知", selfLock);
		if (this.countDown != null) {
			this.countDown.countDown();
		}
	}

	public void lock() {
		if (tryLock()) {
			unlock();
		} else {
			if (wait(beforeLock, null)) {
				unlock();
			}
		}
	}
	
	public void unlock() {
		try {
			Thread.sleep(1000);
			logger.info("thread:{},释放锁,删除节点:{}",
					Thread.currentThread().getId(), selfLock);
			zk.delete(lockRoot + "/" + selfLock, -1);
			selfLock = null;
			// zk.close();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (KeeperException e) {
			e.printStackTrace();
		}
	}

	public boolean tryLock() {

		String lockName = "qiyiLocks-";

		try {
			/** 创建本节点 **/
			selfLock = zk.create(lockRoot + "/" + lockName, new byte[0],
					ZooDefs.Ids.OPEN_ACL_UNSAFE,
					CreateMode.EPHEMERAL_SEQUENTIAL);
			selfLock = selfLock.substring(selfLock.lastIndexOf("/") + 1);
			logger.info("thread:{},创建锁节点:{}", Thread.currentThread().getId(),
					selfLock);
			/** 获取根节点下子节点列表 **/
			List<String> childNodes = zk.getChildren(lockRoot, false);

			/** 将锁下面的子节点列表排序 **/
			Collections.sort(childNodes);
			logger.info("thread:{},获取到子节点列表:{}",
					Thread.currentThread().getId(),
					JSONObject.toJSONString(childNodes));

			int selfIndex = childNodes.indexOf(selfLock);
			if (selfIndex == 0) {
				logger.info("thread:{},当前节点是锁节点:{}", Thread.currentThread()
						.getId(), selfLock);
				return true;
			} else if (selfIndex < 0) {
				return false;
			}

			beforeLock = childNodes.get(selfIndex - 1);
			logger.info("获取到上一个节点:{},本节点:{}", beforeLock, selfLock);

		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return false;
	}

	public boolean wait(String beforeLock, Long timeout) {

		/** 添加watcher **/
		try {
			logger.info("监控节点:{},本节点:{}", lockRoot + "/" + beforeLock, selfLock);
			Stat stat = zk.exists(lockRoot + "/" + beforeLock, this);
			if (stat != null) {
				this.countDown = new CountDownLatch(1);
				if (timeout != null) {
					this.countDown.await(timeout, TimeUnit.MILLISECONDS);
				} else {
					this.countDown.await();
				}
			}
			return true;
		} catch (KeeperException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		return false;
	}

	private synchronized static void initConnect() {
		try {
			zk = new ZooKeeper("192.168.1.111:2181", 5000, null);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void createRoot(String lockRoot)
	{
		if(zk==null)
		{
			synchronized (obj) {
				if(zk==null)
				{
					initConnect();
					
					try {

						this.lockRoot = lockRoot;
						/** 创建锁节点 **/
						logger.info("创建根节点:{}", lockRoot);
						zk.create(lockRoot, new byte[0], ZooDefs.Ids.OPEN_ACL_UNSAFE,
								CreateMode.PERSISTENT);
					} catch (KeeperException e) {
						e.printStackTrace();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}

	public byte[] getRootBytes() throws KeeperException, InterruptedException
	{
		return zk.getData(this.lockRoot, false, null);
	}
	
	public static void main(String[] args) throws InterruptedException {
		ExecutorService e = Executors.newFixedThreadPool(100);
		new DistributedLock().createRoot("/qiyiLock");
		for (int i = 0; i < 10; i++) {
			Runnable r = new Runnable() {

				@Override
				public void run() {

					DistributedLock d = new DistributedLock();
					d.lock();
				}
			};
			e.execute(r);
		}

		Thread.sleep(Long.MAX_VALUE);

	}

}
