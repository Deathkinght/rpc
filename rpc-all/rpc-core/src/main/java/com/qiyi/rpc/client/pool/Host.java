package com.qiyi.rpc.client.pool;

public class Host {
	/**
	 * 服务地址
	 */
	private String ip;
	/**
	 * 端口
	 */
	private int port;
	/**
	 * 权重
	 */
	private int weight;

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getWeight() {
		return weight;
	}

	public void setWeight(int weight) {
		this.weight = weight;
	}
}