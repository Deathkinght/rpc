package com.qiyi.rpc.transport.client;

import java.lang.reflect.Method;

import com.alibaba.fastjson.JSON;
import com.qiyi.rpc.spring.bean.QpcProxy;
import com.qiyi.rpc.transport.client.pool.HandlerManager;
import com.qiyi.rpc.transport.protocol.context.BeanNodeWrapperDto;
import com.qiyi.rpc.transport.protocol.message.Message;
import com.qiyi.rpc.utils.UUIDUtil;

public class ClientProxy extends QpcProxy {

	private BeanNodeWrapperDto wrapper;
    
	/**
	 * 调用的方法名
	 */
	
	public ClientProxy(String interfaceName,BeanNodeWrapperDto wrapper) {
		super(interfaceName);
		this.wrapper = wrapper;
	}

	public Object newInstance() throws IllegalArgumentException, ClassNotFoundException {
		return getObject();
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

		Class<?> returnType = method.getReturnType();
		
		Message request = new Message(UUIDUtil.generateUUID(),wrapper.getMethodSeq(method),wrapper.getBeanSeq(),version,JSON.toJSONBytes(args));

		ClientHandler handler = HandlerManager.getHandler(interfaceName);
		ClientFuture<Object> future = handler.writeMessage(request,returnType);

		return future.getResult();
	}

}
