package com.qiyi.rpc.exception;

public class QpcException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	//private String message;
	
	public QpcException(String message){
		super(message);
	}
	
	@Override
	public String getMessage() {
		return super.getMessage();
	}

}
