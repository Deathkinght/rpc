package com.qiyi.rpc.client.mina;

import java.net.InetSocketAddress;

import org.apache.mina.core.service.IoConnector;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketConnector;

import com.qiyi.rpc.client.ClientHandler;
import com.qiyi.rpc.client.Connector;
import com.qiyi.rpc.protocol.codec.ProtocolFactory;

public class MinaConnector extends Connector {

	
	@Override
	public ClientHandler connect(String ip, int port) {

		MinaClientHandler clientHandler = new MinaClientHandler();

		IoConnector connector = new NioSocketConnector();
		connector.getSessionConfig().setBothIdleTime(3000);
		connector.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolFactory()));
		connector.setHandler(clientHandler);
		connector.connect(new InetSocketAddress(ip, port));
		// connector.
		return clientHandler;
	}

	
}
