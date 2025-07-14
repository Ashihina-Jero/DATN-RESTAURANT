package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.AccountRequestDTO;
import com.poly.goldenbamboo.dtos.AccountResponseDTO;
import com.poly.goldenbamboo.entities.AccountEntity;
import com.poly.goldenbamboo.entities.BranchEntity;
import com.poly.goldenbamboo.entities.RoleEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException; // Đảm bảo đã tạo class này
import com.poly.goldenbamboo.mappers.AccountMapper;
import com.poly.goldenbamboo.repositories.AccountRepository;
import com.poly.goldenbamboo.repositories.BranchRepository; // Đảm bảo đã tạo interface này
import com.poly.goldenbamboo.repositories.RoleRepository;     // Đảm bảo đã tạo interface này
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder; // Đảm bảo có bean này trong cấu hình Spring Security
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccountService {

    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;
    private final RoleRepository roleRepository;
    private final AccountMapper accountMapper;
    private final PasswordEncoder passwordEncoder; // Dùng để mã hóa mật khẩu

    @Autowired
    public AccountService(AccountRepository accountRepository, BranchRepository branchRepository,
                          RoleRepository roleRepository, AccountMapper accountMapper,
                          PasswordEncoder passwordEncoder) {
        this.accountRepository = accountRepository;
        this.branchRepository = branchRepository;
        this.roleRepository = roleRepository;
        this.accountMapper = accountMapper;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Lấy tất cả các tài khoản.
     *
     * @return Danh sách AccountResponseDTO.
     */
    public List<AccountResponseDTO> getAllAccounts() {
        List<AccountEntity> accounts = accountRepository.findAll();
        return accounts.stream()
                .map(accountMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin tài khoản theo ID.
     *
     * @param id ID của tài khoản.
     * @return AccountResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy tài khoản.
     */
    public AccountResponseDTO getAccountById(int id) {
        AccountEntity account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));
        return accountMapper.toResponseDTO(account);
    }

    /**
     * Tạo một tài khoản mới.
     *
     * @param accountRequestDTO DTO chứa thông tin tài khoản cần tạo.
     * @return AccountResponseDTO của tài khoản đã tạo.
     * @throws ResourceNotFoundException nếu Branch hoặc Role không tồn tại.
     * @throws IllegalArgumentException nếu Role ID là null (do role là bắt buộc).
     */
    public AccountResponseDTO createAccount(AccountRequestDTO accountRequestDTO) {
        AccountEntity accountEntity = accountMapper.toEntity(accountRequestDTO);

        // 1. Mã hóa mật khẩu trước khi lưu
        accountEntity.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));

        // 2. Xử lý mối quan hệ Branch (ManyToOne)
        if (accountRequestDTO.getBranchId() != null) {
            BranchEntity branch = branchRepository.findById(accountRequestDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + accountRequestDTO.getBranchId()));
            accountEntity.setBranch(branch);
        } else {
            // Tùy thuộc vào quy tắc nghiệp vụ: có thể set null hoặc throw exception nếu branch bắt buộc
            accountEntity.setBranch(null);
        }

        // 3. Xử lý mối quan hệ Role (ManyToOne)
        if (accountRequestDTO.getRoleId() != null) {
            RoleEntity role = roleRepository.findById(accountRequestDTO.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + accountRequestDTO.getRoleId()));
            accountEntity.setRole(role);
        } else {
            // Role là nullable=false trong Entity của bạn, nên phải có Role ID
            throw new IllegalArgumentException("Role ID is required for Account creation.");
        }

        AccountEntity savedAccount = accountRepository.save(accountEntity);
        return accountMapper.toResponseDTO(savedAccount);
    }

    /**
     * Cập nhật thông tin tài khoản.
     *
     * @param id ID của tài khoản cần cập nhật.
     * @param accountRequestDTO DTO chứa thông tin cập nhật.
     * @return AccountResponseDTO của tài khoản đã cập nhật.
     * @throws ResourceNotFoundException nếu tài khoản, Branch hoặc Role không tồn tại.
     * @throws IllegalArgumentException nếu Role ID là null (do role là bắt buộc).
     */
    public AccountResponseDTO updateAccount(int id, AccountRequestDTO accountRequestDTO) {
        AccountEntity existingAccount = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + id));

        // 1. Cập nhật các trường thông thường từ DTO (trừ mật khẩu và quan hệ)
        accountMapper.updateEntityFromDTO(accountRequestDTO, existingAccount);

        // 2. Xử lý cập nhật mật khẩu (chỉ khi mật khẩu mới được cung cấp trong DTO)
        if (accountRequestDTO.getPassword() != null && !accountRequestDTO.getPassword().isEmpty()) {
            existingAccount.setPassword(passwordEncoder.encode(accountRequestDTO.getPassword()));
        }

        // 3. Xử lý mối quan hệ Branch (ManyToOne)
        if (accountRequestDTO.getBranchId() != null) {
            BranchEntity branch = branchRepository.findById(accountRequestDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + accountRequestDTO.getBranchId()));
            existingAccount.setBranch(branch);
        } else {
            existingAccount.setBranch(null); // Nếu client gửi null, có nghĩa là muốn gỡ bỏ liên kết
        }

        // 4. Xử lý mối quan hệ Role (ManyToOne)
        if (accountRequestDTO.getRoleId() != null) {
            RoleEntity role = roleRepository.findById(accountRequestDTO.getRoleId())
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found with id " + accountRequestDTO.getRoleId()));
            existingAccount.setRole(role);
        } else {
            throw new IllegalArgumentException("Role ID cannot be null for Account update."); // Role là bắt buộc
        }

        AccountEntity updatedAccount = accountRepository.save(existingAccount);
        return accountMapper.toResponseDTO(updatedAccount);
    }

    /**
     * Xóa một tài khoản theo ID.
     *
     * @param id ID của tài khoản cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy tài khoản cần xóa.
     */
    public void deleteAccount(int id) {
        if (!accountRepository.existsById(id)) {
            throw new ResourceNotFoundException("Account not found with id " + id);
        }
        // Thêm logic kiểm tra ràng buộc ở đây (ví dụ: không cho xóa nếu account có order hoặc reservation liên quan)
        accountRepository.deleteById(id);
    }
}