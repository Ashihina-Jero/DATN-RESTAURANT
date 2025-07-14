package com.poly.goldenbamboo.controllers;

import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.services.ComboService;
import com.poly.goldenbamboo.services.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("api/combos")
public class ComboController {

    @Autowired
    private ComboService comboService;

    @Autowired
    private CloudinaryService cloudinaryService;

//    @GetMapping("/search")
//    public ResponseEntity<List<ComboEntity>> searchCombos(@RequestParam String keyword) {
//        List<ComboEntity> combos = comboService.searchCombos(keyword);
//        return ResponseEntity.ok(combos);
//    }
//
//    @GetMapping
//    public ResponseEntity<List<ComboEntity>> getAllCombos() {
//        List<ComboEntity> combos = comboService.getAllCombo();
//        return ResponseEntity.ok(combos);
//    }
//
//    @GetMapping("/{id}")
//    public ResponseEntity<ComboEntity> getComboById(@PathVariable Integer id) {
//        ComboEntity combo = comboService.getComboById(id);
//        if (combo != null) {
//            return ResponseEntity.ok(combo);
//        }
//        return ResponseEntity.notFound().build();
//    }

//    @PostMapping(consumes = {"multipart/form-data"})
//    public ResponseEntity<?> createCombo(
//            @RequestParam String name,
//            @RequestParam String description,
//            @RequestParam BigDecimal price,
//            @RequestParam String status,
//            @RequestParam MultipartFile image) {
//        
//        try {
//            ComboEntity combo = new ComboEntity();
//            combo.setName(name);
//            combo.setDescription(description);
//            combo.setPrice(price);
//            combo.setStatus("active".equals(status));
//            
//            String imageUrl = cloudinaryService.uploadFile(image);
//            combo.setImage(imageUrl);
//
//            ComboEntity savedCombo = comboService.createCombo(combo, image);
//            return ResponseEntity.status(HttpStatus.CREATED).body(savedCombo);
//            
//        } catch (IOException e) {
//            return ResponseEntity.internalServerError()
//                    .body("Error creating combo: " + e.getMessage());
//        }
//    }

//    @PatchMapping(value = "/{id}", consumes = {"multipart/form-data"})
//    public ResponseEntity<?> updateCombo(
//            @PathVariable Integer id,
//            @RequestParam(required = false) String name,
//            @RequestParam(required = false) String description,
//            @RequestParam(required = false) BigDecimal price,
//            @RequestParam(required = false) String status,
//            @RequestParam(required = false) MultipartFile image) {
//        
//        try {
//            ComboEntity existingCombo = comboService.getComboById(id);
//            if (existingCombo == null) {
//                return ResponseEntity.notFound().build();
//            }
//
//            if (name != null) existingCombo.setName(name);
//            if (description != null) existingCombo.setDescription(description);
//            if (price != null) existingCombo.setPrice(price);
//            if (status != null) existingCombo.setStatus("active".equals(status));
//
//            if (image != null && !image.isEmpty()) {
//                if (existingCombo.getImage() != null) {
//                    cloudinaryService.deleteFile(cloudinaryService.extractPublicIdFromUrl(existingCombo.getImage()));
//                }
//                
//                String newImageUrl = cloudinaryService.uploadFile(image);
//                existingCombo.setImage(newImageUrl);
//            }
//
//            ComboEntity updatedCombo = comboService.updateCombo(id, existingCombo, image);
//            return ResponseEntity.ok(updatedCombo);
//            
//        } catch (IOException e) {
//            return ResponseEntity.internalServerError()
//                    .body("Error updating combo: " + e.getMessage());
//        }
//    }

//    @DeleteMapping("/{id}")
//    public ResponseEntity<Void> deleteCombo(@PathVariable Integer id) {
//        try {
//            comboService.deleteCombo(id);
//            return ResponseEntity.noContent().build();
//        } catch (IOException e) {
//            return ResponseEntity.internalServerError().build();
//        }
//    }
}