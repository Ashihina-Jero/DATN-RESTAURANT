package com.poly.goldenbamboo.controllers;

import com.poly.goldenbamboo.services.ComboDishService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/combo-dishes")
public class ComboDishController {

    @Autowired
    private ComboDishService comboDishService;

//    @GetMapping
//    public ResponseEntity<List<ComboDishFullDTO>> getAllComboDishes() {
//        return ResponseEntity.ok(comboDishService.getComboDishFullDTOs());
//    }
//
//    @PostMapping
//    public ResponseEntity<?> createComboDish(@RequestBody ComboDishFullDTO request) {
//        comboDishService.createComboDish(request.getComboId(), request.getDish1Id(), request.getDish2Id());
//        return ResponseEntity.ok().build();
//    }


//    @PutMapping("/{id}")
//    public ResponseEntity<ComboDishFullDTO> updateComboDish(
//            @PathVariable int id,
//            @RequestBody ComboDishFullDTO request) {
//        return ResponseEntity.ok(comboDishService.updateComboDish(
//            id,
//            request.getComboId(),
//            request.getDish1Id(),
//            request.getDish2Id()
//        ));
//    }


//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteComboDish(@PathVariable int id) {
//        comboDishService.deleteComboDishByDishId(id);
//        return ResponseEntity.noContent().build();
//    }

}