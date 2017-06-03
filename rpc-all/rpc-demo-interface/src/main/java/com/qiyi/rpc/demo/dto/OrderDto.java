package com.qiyi.rpc.demo.dto;

import java.io.Serializable;

public class OrderDto implements Serializable{

	private static final long serialVersionUID = 1L;

	private int orderId;
	
	private String orderNo;
	
	private UserDto user ;

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public String getOrderNo() {
		return orderNo;
	}

	public void setOrderNo(String orderNo) {
		this.orderNo = orderNo;
	}

	public UserDto getUser() {
		return user;
	}

	public void setUser(UserDto user) {
		this.user = user;
	}
	
	
	
}
