package com.poly.restaurant.services;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.AuthResponseDTO;
import com.poly.restaurant.dtos.StaffRegistrationRequestDTO;
import com.poly.restaurant.dtos.UserRegistrationRequestDTO;
import com.poly.restaurant.entities.AccountEntity;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.RoleEntity;
import com.poly.restaurant.entities.enums.AccountStatus;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.repositories.AccountRepository;
import com.poly.restaurant.repositories.BranchRepository;
import com.poly.restaurant.repositories.RoleRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AccountService {

    private final AccountRepository accountRepository;
    private final RoleRepository roleRepository;
    private final BranchRepository branchRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    /**
     * Dùng để đăng ký tài khoản cho nhân viên (STAFF, MANAGER)
     */
    @Transactional
    public AuthResponseDTO registerStaff(StaffRegistrationRequestDTO request) {
        if (accountRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalStateException("Username đã được sử dụng.");
        }

        RoleEntity role = roleRepository.findByName(request.getRoleName())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy vai trò: " + request.getRoleName()));

        BranchEntity branch = null;
        if (request.getBranchId() != null) {
            branch = branchRepository.findById(request.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy chi nhánh: " + request.getBranchId()));
        }

        AccountEntity account = new AccountEntity();
        account.setUsername(request.getUsername());
        account.setName(request.getName());
        account.setPhone(request.getPhone());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);
        account.setBranch(branch);
        account.setRoles(Collections.singleton(role));

        AccountEntity savedAccount = accountRepository.save(account);
        String jwtToken = jwtService.generateToken(savedAccount);
        return new AuthResponseDTO(jwtToken, savedAccount.getUsername());
    }

    /**
     * Sửa lại lỗi cú pháp và trả về token
     */
    @Transactional
    public AuthResponseDTO registerUser(UserRegistrationRequestDTO request) {
        if (accountRepository.findByPhone(request.getPhone()).isPresent()) {
            throw new IllegalStateException("Số điện thoại đã được sử dụng.");
        }

        RoleEntity userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new ResourceNotFoundException("Vai trò 'ROLE_USER' không tồn tại."));

        AccountEntity account = new AccountEntity();
        account.setPhone(request.getPhone());
        account.setName(request.getName());
        account.setPassword(passwordEncoder.encode(request.getPassword()));
        account.setStatus(AccountStatus.ACTIVE);
        account.setRoles(Collections.singleton(userRole));

        AccountEntity savedAccount = accountRepository.save(account);
        String jwtToken = jwtService.generateToken(savedAccount);
        return new AuthResponseDTO(jwtToken, savedAccount.getUsername());
    }
}