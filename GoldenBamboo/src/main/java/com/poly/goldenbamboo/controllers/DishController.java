package com.poly.goldenbamboo.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.poly.goldenbamboo.dtos.FoodDTO;
import com.poly.goldenbamboo.entities.DishEntity;
import com.poly.goldenbamboo.entities.MenuEntity;
import com.poly.goldenbamboo.services.DishService;

@RestController
@RequestMapping("/api/dishes")
public class DishController {
	@Autowired
	private DishService dishService;

	@GetMapping("Dish")
	public List<DishEntity> getAllDish() {
		return dishService.getAllDish();
	}

	@GetMapping("Dish/{id}")
	public DishEntity getDishById(@PathVariable("id") Integer id) {
		return dishService.getDishById(id);
	}
	
//	@GetMapping("/DishByBranch/{branchId}")
//	public List<DishEntity> getAllDishByBranch(@PathVariable("branchId") Integer branchId	) {
//        return dishService.getDishByBranchId(branchId);
//	}
//	
//	@PostMapping("Food/Create")
//	public FoodDTO createFoodDTO(@RequestBody FoodDTO dto) {
//		return dishService.createDish(dto);
//	}
