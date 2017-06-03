package com.qiyi.rpc.client.pool;

import java.net.InetSocketAddress;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.qiyi.rpc.client.ClientHandler;
import com.qiyi.rpc.protocol.codec.ProtocolFactory;

/**
 * 
 * @author qiyi
 *
 */
public class HandlerManager {

	private static final String SYMBOL = ":";
	private static Hashtable<String, HandlerPool> handlerPool = new Hashtable<>();

	public static ClientHandler getHandler(String serviceName) {

		ServiceHost serviceHost = ServiceHostContext.getServiceHost(serviceName);
		Host host = serviceHost.getHost();

		HandlerPool pool = getHandlerPool(host.getIp(), host.getPort());
		return pool.getHandler();
	}

	private static HandlerPool getHandlerPool(String ip, int port) {
		return handlerPool.get(ip + SYMBOL + port);
	}

	public synchronized static void init(String interfaceName, Iterable<String> hosts) {

		ServiceHostContext.setServiceHost(interfaceName, hosts);

		hosts.forEach(host -> {
			handlerPool.put(host, HandlerPool.initPool(host));
		});

	}
}

/**
 * 
 * @author qiyi
 *
 */
class HandlerPool {

	int activeNum = 0;

	int maxConn = 0;

	int minNum = 1;

	String ip;

	int port;

	List<ClientHandler> freeHandlers = new LinkedList<>();

	// private static final String REGEX = ",";

	private HandlerPool(int activeNum, int maxConn, String ip, int port) {
		super();
		this.activeNum = activeNum;
		this.maxConn = maxConn;
		this.ip = ip;
		this.port = port;

	}

	protected static HandlerPool initPool(String hosts) {
		String[] h = hosts.split(":");
		HandlerPool pool = new HandlerPool(0, 1, h[0], Integer.valueOf(h[1]));
		synchronized (pool) {
			for (int i = 0; i < pool.minNum; i++) {
				ClientHandler clientHandler = pool.createClientHandler();
				pool.freeHandlers.add(clientHandler);
			}
		}
		return pool;
	}

	/**
	 * 获取handler
	 * 
	 * @return
	 */
	public  ClientHandler getHandler() {
		// TODO
		// ClientHandler clientHandler = null;

		// if (freeHandlers.size() > 0) {
		// clientHandler = freeHandlers.get(0);
		// freeHandlers.remove(clientHandler);
		// if (!clientHandler.isActive()) {
		// if (activeNum < maxConn) {
		// clientHandler = createClientHandler();
		// activeNum++;
		// }
		// }
		// return clientHandler;
		//
		// } else {
		// if (activeNum < maxConn) {
		// clientHandler = createClientHandler();
		// activeNum++;
		// }
		// }
		if (freeHandlers.isEmpty()) {
			freeHandlers.add(createClientHandler());
		}

		return freeHandlers.get(0);
	}

	private ClientHandler createClientHandler() {

		ClientHandler clientHandler = new ClientHandler();

		IoConnector connector = new NioSocketConnector();
		connector.getSessionConfig().setBothIdleTime(3000);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolFactory()));
		connector.setHandler(clientHandler);
		connector.connect(new InetSocketAddress(this.ip, this.port));
		// connector.
		return clientHandler;
	}
}
