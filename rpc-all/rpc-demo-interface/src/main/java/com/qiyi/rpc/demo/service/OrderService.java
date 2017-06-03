package com.qiyi.rpc.demo.service;

import java.util.List;

import com.qiyi.rpc.demo.dto.OrderDto;

public interface OrderService {

	 void createOrder(OrderDto order);
	
	 OrderDto getOrder(int orderId);
	
	 List<OrderDto> getOrders(int userId);
	
}
