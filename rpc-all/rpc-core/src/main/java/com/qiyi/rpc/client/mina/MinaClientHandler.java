package com.qiyi.rpc.client.mina;

import org.apache.mina.core.service.IoHandler;
import org.apache.mina.core.session.IdleStatus;
import org.apache.mina.core.session.IoSession;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.client.ClientFuture;
import com.qiyi.rpc.client.ClientHandler;
import com.qiyi.rpc.exception.BaseException;
import com.qiyi.rpc.protocol.message.Message;

/**
 * 客户端发送消息
 * 
 * @author qiyi
 *
 */
public class MinaClientHandler extends ClientHandler implements IoHandler{

	private IoSession session;

	
	public MinaClientHandler(String ip,int port) {
		super.ip = ip;
		super.port = port;
	}

	public MinaClientHandler() {
		super();
		//this.charset = Charset.forName("UTF-8");
	}

	@Override
	public void sessionCreated(IoSession session) throws Exception {
		this.session = session;
	}

	@Override
	public void sessionClosed(IoSession session) throws Exception {
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
   
	
	@Override
	public void sessionOpened(IoSession session) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void sessionIdle(IoSession session, IdleStatus status) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void exceptionCaught(IoSession session, Throwable cause) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void messageSent(IoSession session, Object message) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void doWrite(Message msg) {
		this.session.write(msg);
	}
	
	
}
