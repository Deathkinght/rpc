package com.qiyi.rpc.client;

import java.util.concurrent.CountDownLatch;

public class ClientFuture<T> {

	private CountDownLatch countDown = new CountDownLatch(1);
	
	private T result ;
	
	private Class<?> returnType;
	
	
	
	public ClientFuture(Class<?> returnType) {
		super();
		this.returnType = returnType;
	}

	public  void wrap(T result)
	{
		this.result = result ;
	}
	
	public T getResult() throws InterruptedException
	{
		this.countDown.await();
		return result ;
	}
	
	public void await() throws InterruptedException
	{
		this.countDown.await();
	}
	
	public void countDown()
	{
		this.countDown.countDown();
	}

	public void setResult(T result) {
		this.result = result;
	}

	public Class<?> getReturnType() {
		return returnType;
	}

	public void setReturnType(Class<?> returnType) {
		this.returnType = returnType;
	}
	
}
