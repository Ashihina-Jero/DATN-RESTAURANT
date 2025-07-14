package com.poly.restaurant.services;

import com.poly.restaurant.dtos.DiscountRequestDTO;
import com.poly.restaurant.dtos.DiscountResponseDTO;
import com.poly.restaurant.entities.*;
import com.poly.restaurant.mappers.DiscountMapper;
import com.poly.restaurant.repositories.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DiscountService {

    private final DiscountRepository discountRepository;
    private final BranchRepository branchRepository;
    private final DishRepository dishRepository;
    private final ComboRepository comboRepository;
    private final DiscountDishRepository discountDishRepository;
    private final DiscountComboRepository discountComboRepository;
    private final DiscountMapper discountMapper;

    @Transactional
    public DiscountResponseDTO createDiscount(DiscountRequestDTO request) {
        DiscountEntity discount = new DiscountEntity();
        discount.setName(request.getName());
        discount.setType(request.getType());
        discount.setValue(request.getValue());
        discount.setStartDate(request.getStartDate());
        discount.setEndDate(request.getEndDate());
        discount.setStatus(com.poly.restaurant.entities.enums.DiscountStatus.SCHEDULED);

        if (request.getBranchIds() != null && !request.getBranchIds().isEmpty()) {
            List<BranchEntity> branches = branchRepository.findAllById(request.getBranchIds());
            discount.setBranches(new HashSet<>(branches));
        }

        DiscountEntity savedDiscount = discountRepository.save(discount);

        if (request.getDishIds() != null && !request.getDishIds().isEmpty()) {
            List<DiscountDishEntity> discountDishes = new ArrayList<>();
            List<DishEntity> dishes = dishRepository.findAllById(request.getDishIds());
            for (DishEntity dish : dishes) {
                DiscountDishEntity discountDish = new DiscountDishEntity();
                discountDish.setDiscount(savedDiscount);
                discountDish.setDish(dish);
                discountDishes.add(discountDish);
            }
            discountDishRepository.saveAll(discountDishes);
        }

        if (request.getComboIds() != null && !request.getComboIds().isEmpty()) {
            List<DiscountComboEntity> discountCombos = new ArrayList<>();
            List<ComboEntity> combos = comboRepository.findAllById(request.getComboIds());
            for (ComboEntity combo : combos) {
                DiscountComboEntity discountCombo = new DiscountComboEntity();
                discountCombo.setDiscount(savedDiscount);
                discountCombo.setCombo(combo);
                discountCombos.add(discountCombo);
            }
            discountComboRepository.saveAll(discountCombos);
        }

        return discountMapper.toResponseDTO(discountRepository.findById(savedDiscount.getId()).get());
    }
}