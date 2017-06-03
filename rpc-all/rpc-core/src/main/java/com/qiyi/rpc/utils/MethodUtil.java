package com.qiyi.rpc.utils;

import java.lang.reflect.Method;

public class MethodUtil {


	/**
	 * 生成methodName 通过方法名、参数与返回类型组成
	 * 
	 * @param method
	 * @return
	 */
	public static String getMethodNameWithType(Method method) {

		String methodName = method.getName();

		Class<?>[] parameterTypes = method.getParameterTypes();

		for (Class<?> cla : parameterTypes) {
			methodName += cla.getName();
		}

		methodName += method.getReturnType().getName();

		return methodName;

	}
	
}
