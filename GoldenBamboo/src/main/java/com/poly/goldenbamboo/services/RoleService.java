package com.poly.goldenbamboo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.poly.goldenbamboo.entities.RoleEntity;
import com.poly.goldenbamboo.repositories.RoleRepository;

import jakarta.persistence.EntityNotFoundException;

@Service
public class RoleService {
	@Autowired
	private RoleRepository roleJPA;

	public List<RoleEntity> getAllRole() {
		return roleJPA.findAll();
	}

	public RoleEntity addRole(RoleEntity role) {
		return roleJPA.save(role);
	}

	public boolean deleteRoleById(Integer id) {
		if (roleJPA.existsById(id)) {
			roleJPA.deleteById(id);
			return true;
		}
		return false;
	}


}
