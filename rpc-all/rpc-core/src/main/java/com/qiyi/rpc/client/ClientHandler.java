package com.qiyi.rpc.client;

import java.util.concurrent.ConcurrentHashMap;

import org.apache.mina.core.service.IoHandlerAdapter;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.exception.BaseException;
import com.qiyi.rpc.protocol.message.Message;

/**
 * 客户端发送消息
 * 
 * @author qiyi
 *
 */
public class ClientHandler extends IoHandlerAdapter {

	/**
	 * ip port
	 */
	private String handlerKey;

	/**
	 * ip
	 */
	private String ip;

	/**
	 * 端口
	 */
	private int port;

	private IoSession session;

	private volatile boolean isActive = true;

	private ConcurrentHashMap<String, ClientFuture<Object>> futures = new ConcurrentHashMap<>();

	//private Charset charset ;
	
	
	public ClientHandler() {
		super();
		//this.charset = Charset.forName("UTF-8");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		super.sessionCreated(session);
		this.session = session;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
		super.sessionClosed(session);
		isActive = false;
	}

	@Override
	public void messageReceived(IoSession session, Object obj) throws Exception {
		// super.messageReceived(session, message);

		Message msg = (Message) obj;
	
		ClientFuture<Object> future = futures.get(msg.getMessageId());
		if(future == null)
		{
			throw new BaseException("future lossed");
		}
		
		Object reponse =  JSON.parseObject(msg.getBody(), future.getReturnType());
		
		future.setResult(reponse);
		
		future.countDown();
	}

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
		session.write(msg);
		return future;
	}

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
