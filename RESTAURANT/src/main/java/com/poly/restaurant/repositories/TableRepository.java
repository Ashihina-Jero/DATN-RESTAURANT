package com.poly.restaurant.repositories;

import com.poly.restaurant.entities.TableEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TableRepository extends JpaRepository<TableEntity, Integer> {

    Optional<TableEntity> findByName(String name);

}