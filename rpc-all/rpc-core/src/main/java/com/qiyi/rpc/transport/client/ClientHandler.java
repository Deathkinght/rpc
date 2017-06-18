package com.qiyi.rpc.transport.client;

import java.util.concurrent.ConcurrentHashMap;

import com.qiyi.rpc.transport.protocol.message.Message;

/**
 * 客户端发送消息
 * 
 * @author qiyi
 *
 */
public abstract class ClientHandler {

	/**
	 * ip port
	 */
	protected String handlerKey;

	/**
	 * ip
	 */
	protected String ip;

	/**
	 * 端口
	 */
	protected int port;

	protected volatile boolean isActive = true;

	protected ConcurrentHashMap<String, ClientFuture<Object>> futures = new ConcurrentHashMap<>();


//	public Object getResponse(String msgId,long waiteTimes) throws InterruptedException, BaseException {
//		
//		ClientFuture<Object> clientFuture = futures.get(msgId);
//		if(clientFuture == null)
//		{
//			throw new BaseException("future lossed");
//		}
//		clientFuture.await();
//		
//		return clientFuture.getResult();
//	}

	public boolean isActive() {
		return this.isActive;
	}
   
	
	public  ClientFuture<Object> writeMessage(Message msg,Class<?> returnType) {
		ClientFuture<Object> future = new ClientFuture<>(returnType);
		futures.put(msg.getMessageId(), future);
		doWrite(msg);
		return future;
	}

	protected abstract void doWrite(Message msg);
	
	public String getHandlerKey() {
		return handlerKey;
	}

	public void setHandlerKey(String handlerKey) {
		this.handlerKey = handlerKey;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

}
