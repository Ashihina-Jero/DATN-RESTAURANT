package com.poly.goldenbamboo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.web.bind.annotation.RestController;

import com.poly.goldenbamboo.entities.MenuEntity;
import com.poly.goldenbamboo.services.MenuDishService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@RestController
@RequestMapping("/api/menu-dishes")
public class MenuDishController {
	
	@Autowired
	private MenuDishService menuDishService;
	

	
}
