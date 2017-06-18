package com.qiyi.rpc.transport.protocol.message;

import java.io.Serializable;

/**
 * Created by qiyi on 2016/10/5.
 */
public class ResponseMessage<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 请求id
     */
    private String messageId;

    /**
     * 返回结果
     */
    private T result;

    /**
     * 结果码
     */
    private int resultCode;

    public ResponseMessage(){}
    
    public ResponseMessage(String messageId, T result, int resultCode) {
		super();
		this.messageId = messageId;
		this.result = result;
		this.resultCode = resultCode;
	}

    public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public T getResult() {
		return result;
	}

	public void setResult(T result) {
		this.result = result;
	}

	public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

}
