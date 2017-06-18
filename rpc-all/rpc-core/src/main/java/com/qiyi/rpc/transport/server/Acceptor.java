package com.qiyi.rpc.transport.server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.qiyi.rpc.annotation.Implementation;
import com.qiyi.rpc.transport.server.mina.MinaAcceptor;

/**
 * 端口监听启动
 * @author qiyi
 *
 */
@Implementation(MinaAcceptor.class)
public abstract class Acceptor {

	private static Logger logger = LoggerFactory.getLogger(Acceptor.class);
	
	protected static int port = 2200;
	
	protected static  String ip;
	
	private static String provider ;
	
	private static volatile Acceptor acceptor = null;
	
	
	public static void acceptor()
	{
		
		if(acceptor==null)
		{
			
			synchronized (Acceptor.class) {
				
				if(acceptor==null)
				{
					try {
						
						Implementation anno =	Acceptor.class.getAnnotation(Implementation.class);
						Class<?> imp = anno.value();
						
						acceptor = (Acceptor) imp.newInstance();
						
						acceptor.start();						
					}catch (SecurityException e) {
						e.printStackTrace();
					} catch (InstantiationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (IllegalArgumentException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			}
		}
		
		
	}
	
	
	public  void start()
	{
		try{
			doStart();
		}catch(Exception e)
		{
			e.printStackTrace();
			logger.error("server start error",e);
			System.exit(-1);
		}
		
	}
	
	
	protected abstract void doStart();
	
	public static void setIp(String ipIn) {
		ip = ipIn;
	}
	
	public static String getProvider()
	{
		return provider;
	}
	
	
	public static void main(String[] args) {
		Acceptor.acceptor();
	}
	
}
