package com.qiyi.rpc.demo.client.dto;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MethodWrapper {

	private transient Method method;
	
	private List<ParamWrapper> parameters = new ArrayList<>();
	
	private Map<String,ParamWrapper> parametersMap = new HashMap<>();
	
	private String methodName;

	public MethodWrapper(Method method, Map<String, ParamWrapper> parametersMap, List<ParamWrapper> parameters, String methodName) {
		super();
		this.method = method;
		this.parametersMap = parametersMap;
		this.parameters = parameters;
		this.methodName = methodName;
	}


	public List<ParamWrapper> getParameters() {
		return parameters;
	}

	public void setParameters(List<ParamWrapper> parameters) {
		this.parameters = parameters;
	}

	public Map<String, ParamWrapper> getParametersMap() {
		return parametersMap;
	}

	public ParamWrapper getParamWrapper(String paramName)
	{
		return parametersMap.get(paramName);
	}
	
	public ParamWrapper getParamWrapper(int index)
	{
		return parameters.get(index);
	}

	public void setParametersMap(Map<String, ParamWrapper> parametersMap) {
		this.parametersMap = parametersMap;
	}


	public String getMethodName() {
		return methodName;
	}

	public void setMethodName(String methodName) {
		this.methodName = methodName;
	}

	public Method getMethod() {
		return method;
	}

	public void setMethod(Method method) {
		this.method = method;
	}
	
}
