//package com.poly.goldenbamboo.controllers;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.poly.goldenbamboo.entities.ReservationEntity;
//import com.poly.goldenbamboo.services.ReservationService;
//
//@RestController
//@RequestMapping("/api/reservations")
//public class ReservationController {
//	@Autowired
//	private ReservationService reservationService;
//	
//	@GetMapping
//	public List<ReservationEntity> getReservation(){
//		return reservationService.getAllReservation();
//	}
//}
