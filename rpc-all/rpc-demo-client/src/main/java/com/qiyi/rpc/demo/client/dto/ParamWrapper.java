package com.qiyi.rpc.demo.client.dto;

public class ParamWrapper {

	private String paramName;
	
	private String paramType;
	
	private Class<?> paramTypeCla;

	public String getParamName() {
		return paramName;
	}

	public void setParamName(String paramName) {
		this.paramName = paramName;
	}

	public String getParamType() {
		return paramType;
	}

	public void setParamType(String paramType) {
		this.paramType = paramType;
	}

	public ParamWrapper(String paramName, String paramType,
			Class<?> paramTypeCla) {
		super();
		this.paramName = paramName;
		this.paramType = paramType;
		this.paramTypeCla = paramTypeCla;
	}

	public Class<?> getParamTypeCla() {
		return paramTypeCla;
	}

	public void setParamTypeCla(Class<?> paramTypeCla) {
		this.paramTypeCla = paramTypeCla;
	}
}
