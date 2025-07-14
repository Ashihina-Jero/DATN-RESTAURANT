package com.poly.restaurant.entities;

import java.io.Serializable;
import java.time.LocalDateTime; // Sử dụng LocalDateTime
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.poly.restaurant.entities.enums.AccountStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter; // Thay thế @Data
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter // Dùng Getter
@Setter // Dùng Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "accounts")
public class AccountEntity implements Serializable, UserDetails  {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Thêm lại username cho nhân viên
    @Column(name = "username", unique = true)
    private String username;

    @Column(name = "name", nullable = false)
    @NotBlank(message = "Tên không được để trống")
    private String name;

    @Column(name = "password") // Mật khẩu có thể null cho khách hàng dùng OTP
    private String password;

    @Column(name = "phone", unique = true, nullable = false)
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^(0[3|5|7|8|9])+([0-9]{8})$", message = "Số điện thoại không hợp lệ")
    private String phone;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private AccountStatus status;

    @CreationTimestamp
    @Column(name = "create_at", updatable = false)
    private LocalDateTime createAt; // Đổi sang LocalDateTime

    @UpdateTimestamp
    @Column(name = "update_at")
    private LocalDateTime updateAt; // Đổi sang LocalDateTime

    // Chuyển sang @ManyToMany
    @ManyToMany(fetch = FetchType.EAGER) // EAGER để lấy role ngay khi load account
    @JoinTable(
        name = "account_roles",
        joinColumns = @JoinColumn(name = "account_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<RoleEntity> roles = new HashSet<>();

    // Các mối quan hệ khác giữ nguyên...
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "branch_id")
    private BranchEntity branch;

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<OrderEntity> orders = new ArrayList<>();

    @OneToMany(mappedBy = "account", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<ReservationEntity> reservations = new ArrayList<>();
    
 // PHẦN IMPLEMENT THÊM

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // Trả về danh sách quyền của người dùng
        return this.roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        // Trả về trường dùng để đăng nhập.
        // Nếu là nhân viên, trả về username. Nếu là khách hàng, trả về số điện thoại.
        return this.username != null ? this.username : this.phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        // Tài khoản không bao giờ hết hạn
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        // Dựa vào status của bạn để quyết định tài khoản có bị khóa hay không
        return this.status != AccountStatus.LOCKED;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        // Mật khẩu không bao giờ hết hạn
        return true;
    }

    @Override
    public boolean isEnabled() {
        // Dựa vào status để quyết định tài khoản có được kích hoạt hay không
        return this.status == AccountStatus.ACTIVE;
    }
}