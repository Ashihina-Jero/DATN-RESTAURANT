package com.poly.goldenbamboo.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.poly.goldenbamboo.entities.ComboEntity;
import com.poly.goldenbamboo.entities.DiscountComboEntity;
import com.poly.goldenbamboo.entities.DiscountEntity;

public interface DiscountComboRepository extends JpaRepository<DiscountComboEntity, Integer>{

    List<DiscountComboEntity> findByComboId(Integer comboId);
    List<DiscountComboEntity> findByDiscountId(Integer discountId);
    Optional<DiscountComboEntity> findByComboAndDiscount(ComboEntity combo, DiscountEntity discount);
	
}
