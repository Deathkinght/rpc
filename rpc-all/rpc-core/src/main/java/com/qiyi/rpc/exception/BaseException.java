package com.qiyi.rpc.exception;

public class BaseException extends Exception {

	private static final long serialVersionUID = 1L;
	
	//private String message;
	
	public BaseException(String message){
		super(message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
