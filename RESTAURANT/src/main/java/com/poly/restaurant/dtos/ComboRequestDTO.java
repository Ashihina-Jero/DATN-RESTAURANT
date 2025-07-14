package com.poly.restaurant.dtos;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ComboRequestDTO {
	@NotBlank(message = "Tên combo không được để trống")
    private String name;
	
    private String description;
    
    private String image;
    @NotNull(message = "Giá combo không được để trống")
    private BigDecimal price; 

    // Chỉ giữ lại trường này để nhận danh sách món ăn kèm số lượng
    @NotEmpty(message = "Combo phải có ít nhất một món ăn")
    @Valid // Để kiểm tra các item bên trong
    private List<ComboItemDTO> dishes;
}