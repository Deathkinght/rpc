package com.qiyi.rpc.transport.protocol.message;

import java.io.Serializable;

/**
 * Created by qiyi on 2016/10/5.
 */
public class RequestMessage implements  Serializable{

    private static final long serialVersionUID = 1L;

    /**
     * 请求id
     */
    private String messageId;

    /**
     * 调用的接口的类名
     */
    private String className ;

    /**
     * 调用的接口的方法名
     */
    private String methodName ;

    /**
     *参数的类型
     */

    private  Class<?>[] parameterTypes;

    /**
     * 调用的接口传入的参数
     */
    private Object[] args;


    public RequestMessage() {
    }

    public RequestMessage(String className, String methodName, Class<?>[] parameterTypes, Object[] args) {
        this.className = className;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(Class<?>[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

}
