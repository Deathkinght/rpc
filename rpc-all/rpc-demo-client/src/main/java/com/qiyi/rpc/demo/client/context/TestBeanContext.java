package com.qiyi.rpc.demo.client.context;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试类 
 * @author qiyi
 *
 */
public class TestBeanContext {

	
	private Map<String, Object> testBeans = new HashMap<>();

	public Map<String, Object> getTestBeans() {
		return testBeans;
	}

	public void setTestBeans(Map<String, Object> testBeans) {
		this.testBeans = testBeans;
	}
	
	
}
