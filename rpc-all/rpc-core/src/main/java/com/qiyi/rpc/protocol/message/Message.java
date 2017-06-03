package com.qiyi.rpc.protocol.message;

import java.io.Serializable;

public class Message implements  Serializable{

    private static final long serialVersionUID = 1L;
    
    /**
     * 请求id
     */
    private String messageId;

    /**
     * 方法编号
     */
    private int mseq;
    
    /**
     * 服务版本
     */
    private String version;
    
    /**
     * 服务编号
     */
    private int bseq;
    
    /**
     * 返回结果或者请求参数
     */
    private byte[] body;

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public byte[] getBody() {
		return body;
	}

	public void setBody(byte[] body) {
		this.body = body;
	}

	
	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Message(String messageId, int mseq,int bseq,String version, byte[] body) {
		super();
		this.messageId = messageId;
		this.mseq = mseq;
		this.bseq = bseq;
		this.version = version;
		this.body = body;
	}

	public Message() {
		super();
	}

	public int getMseq() {
		return mseq;
	}

	public void setMseq(int mseq) {
		this.mseq = mseq;
	}

	public int getBseq() {
		return bseq;
	}

	public void setBseq(int bseq) {
		this.bseq = bseq;
	}


}
