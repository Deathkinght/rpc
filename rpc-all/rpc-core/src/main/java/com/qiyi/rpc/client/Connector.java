package com.qiyi.rpc.client;

import com.qiyi.rpc.annotation.Implementation;
import com.qiyi.rpc.client.mina.MinaConnector;

@Implementation(MinaConnector.class)
public abstract class Connector {

	public static volatile Connector connector = null;
	
	
	
	public abstract ClientHandler connect(String ip,int port);
	
	
	public static ClientHandler createClientHandler(String ip,int port){
		
		if(connector==null)
		{
			synchronized (Connector.class) {
				if(connector==null)
				{
					Implementation anno = Connector.class.getAnnotation(Implementation.class);
					Class<?> connectorClass = anno.value();
					try {
						connector = (Connector) connectorClass.newInstance();
					} catch (InstantiationException e) {
						e.printStackTrace();
					} catch (IllegalAccessException e) {
						e.printStackTrace();
					}
				}
			}
		}
		
		

		return connector.connect(ip, port);
	}
	
}
