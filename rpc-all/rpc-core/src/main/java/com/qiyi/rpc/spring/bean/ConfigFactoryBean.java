package com.qiyi.rpc.spring.bean;

public class ConfigFactoryBean extends QpcFactoryBean {

	/**
	 * zookeeper ip
	 */
	private String address;
	
	/**
	 * 回话超时时间
	 */
	private Integer sessionTimeoutMs ;

	/**
	 * 重试沉睡时间 毫秒
	 */
	private Integer retrySleepTimeMs ;

	/**
	 * 重试次数
	 */
	private Integer retryMaxRetries ;

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public Integer getSessionTimeoutMs() {
		return sessionTimeoutMs;
	}

	public void setSessionTimeoutMs(Integer sessionTimeoutMs) {
		this.sessionTimeoutMs = sessionTimeoutMs;
	}

	public Integer getRetrySleepTimeMs() {
		return retrySleepTimeMs;
	}

	public void setRetrySleepTimeMs(Integer retrySleepTimeMs) {
		this.retrySleepTimeMs = retrySleepTimeMs;
	}

	public Integer getRetryMaxRetries() {
		return retryMaxRetries;
	}

	public void setRetryMaxRetries(Integer retryMaxRetries) {
		this.retryMaxRetries = retryMaxRetries;
	}
	
	
}
