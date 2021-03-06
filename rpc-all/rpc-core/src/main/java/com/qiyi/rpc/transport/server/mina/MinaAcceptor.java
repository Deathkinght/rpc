package com.qiyi.rpc.transport.server.mina;

import java.io.IOException;
import java.net.BindException;
import java.net.InetSocketAddress;

import org.apache.commons.lang3.StringUtils;
import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiyi.rpc.transport.protocol.codec.mina.ProtocolFactory;
import com.qiyi.rpc.transport.server.Acceptor;
import com.qiyi.rpc.utils.AddrUtil;

/**
 * 端口监听启动 mina
 * @author qiyi
 *
 */
public class MinaAcceptor extends Acceptor {

	private static Logger logger = LoggerFactory.getLogger(MinaAcceptor.class);
	
	private static int port = 2200;
	
	private static  String ip;
	
	private static String provider ;
	
	@Override
	public  void doStart()
	{
		IoAcceptor acceptor = new NioSocketAcceptor();
		acceptor.getSessionConfig().setBothIdleTime(3000);
		acceptor.getSessionConfig().setReadBufferSize(64);
		acceptor.getFilterChain().addLast("codec", new ProtocolCodecFilter(new ProtocolFactory()));
		acceptor.setHandler(new MinaDispatcherHandler());
		
		if(StringUtils.isBlank(ip))
		{
			ip = AddrUtil.getLocalAddr();
		}
		try {
			bind(acceptor);
			provider = ip+":"+port;
		} catch (IOException e) {
			e.printStackTrace();
			System.exit(-1);//exit
		}
	}
	
	private static void bind(IoAcceptor acceptor) throws IOException
	{
		try {
			acceptor.bind(new InetSocketAddress(ip, port));
		}
		catch(BindException e)
		{
			//e.printStackTrace();
			logger.info("port in use choose another,nowL{},next:{}",port++,port);
			bind(acceptor);
		}catch (IOException e) {
			e.printStackTrace();
			throw e;
		}
	}

	public static void setIp(String ipIn) {
		ip = ipIn;
	}
	
	public static String getProvider()
	{
		return provider;
	}
	
	
}
