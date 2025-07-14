package com.poly.goldenbamboo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.goldenbamboo.entities.OrderDetailEntity;


@RestController
@RequestMapping("/api/order-details")
public class OrderDetailController {
//	@Autowired
//	private OrderDetailService orderDetailService;
//	
//	@GetMapping
//	public List<OrderDetailEntity> getOrderdetail(){
//		return orderDetailService.getAllOrderdetail();
//	}
}
