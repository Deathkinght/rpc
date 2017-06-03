package com.qiyi.rpc.demo.client.dto;

public class InvokeReqDto {

	private String interfaceName;
	
	private String methodName ;
	
	private String arg;

	public String getInterfaceName() {
		return interfaceName;
	}

	public void setInterfaceName(String interfaceName) {
		this.interfaceName = interfaceName;
	}

	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public String getArg() {
		return arg;
	}

	public void setArg(String arg) {
		this.arg = arg;
	}

}
