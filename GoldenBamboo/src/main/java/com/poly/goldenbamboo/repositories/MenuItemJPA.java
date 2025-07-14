package com.poly.goldenbamboo.repositories;

import com.poly.goldenbamboo.entities.MenuItemEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MenuItemJPA extends JpaRepository<MenuItemEntity, Integer> {
}