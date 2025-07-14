package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.OrderDetailRequestDTO;
import com.poly.goldenbamboo.dtos.OrderDetailResponseDTO;
import com.poly.goldenbamboo.entities.OrderDetailEntity;
import com.poly.goldenbamboo.entities.MenuItemEntity;
import com.poly.goldenbamboo.entities.OrderEntity;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.OrderDetailMapper;
import com.poly.goldenbamboo.repositories.OrderDetailRepository;
import com.poly.goldenbamboo.repositories.MenuItemRepository; // Giả định bạn có MenuItemRepository (nếu có)
import com.poly.goldenbamboo.repositories.OrderRepository;    // Cần Repository cho Order

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;
    private final MenuItemRepository menuItemRepository; // Hoặc DishRepository/ComboRepository
    private final OrderRepository orderRepository;
    private final OrderDetailMapper orderDetailMapper;

    @Autowired
    public OrderDetailService(OrderDetailRepository orderDetailRepository,
                              MenuItemRepository menuItemRepository,
                              OrderRepository orderRepository,
                              OrderDetailMapper orderDetailMapper) {
        this.orderDetailRepository = orderDetailRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderRepository = orderRepository;
        this.orderDetailMapper = orderDetailMapper;
    }

    /**
     * Lấy tất cả các chi tiết đơn hàng.
     *
     * @return Danh sách OrderDetailResponseDTO.
     */
    public List<OrderDetailResponseDTO> getAllOrderDetails() {
        List<OrderDetailEntity> orderDetails = orderDetailRepository.findAll();
        return orderDetails.stream()
                .map(orderDetailMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin chi tiết đơn hàng theo ID.
     *
     * @param id ID của chi tiết đơn hàng.
     * @return OrderDetailResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public OrderDetailResponseDTO getOrderDetailById(int id) {
        OrderDetailEntity orderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + id));
        return orderDetailMapper.toResponseDTO(orderDetail);
    }

    /**
     * Tạo một chi tiết đơn hàng mới.
     * Lưu ý: Thường được gọi từ OrderService khi tạo/cập nhật Order.
     *
     * @param orderId ID của đơn hàng cha.
     * @param orderDetailRequestDTO DTO chứa thông tin chi tiết đơn hàng.
     * @return OrderDetailResponseDTO của chi tiết đơn hàng đã tạo.
     * @throws ResourceNotFoundException nếu Order hoặc MenuItem không tồn tại.
     */
    @Transactional
    public OrderDetailResponseDTO createOrderDetail(int orderId, OrderDetailRequestDTO orderDetailRequestDTO) {
        // 1. Tìm OrderEntity cha
        OrderEntity order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + orderId));

        // 2. Tìm MenuItemEntity (Combo hoặc Dish)
        MenuItemEntity menuItem = menuItemRepository.findById(orderDetailRequestDTO.getMenuItemId())
                .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + orderDetailRequestDTO.getMenuItemId()));

        // 3. Chuyển đổi DTO sang Entity và thiết lập các mối quan hệ
        OrderDetailEntity orderDetailEntity = orderDetailMapper.toEntity(orderDetailRequestDTO);
        orderDetailEntity.setOrder(order);
        orderDetailEntity.setMenuItem(menuItem);

        // 4. (Tùy chọn) Ghi đè các giá trị price, name, discountPercentage từ MenuItem thực tế
        // Đây là một best practice để đảm bảo tính nhất quán và ghi lại trạng thái tại thời điểm đặt hàng.
        orderDetailEntity.setPrice(BigDecimal.valueOf(menuItem.getPrice())); // Lấy giá hiện tại từ MenuItem
        orderDetailEntity.setName(menuItem.getName()); // Lấy tên hiện tại từ MenuItem
        // Nếu có logic phức tạp về giảm giá, thực hiện ở đây.
        // orderDetailEntity.setDiscountPercentage(calculateDiscount(menuItem));

        // 5. Lưu Entity
        OrderDetailEntity savedOrderDetail = orderDetailRepository.save(orderDetailEntity);
        return orderDetailMapper.toResponseDTO(savedOrderDetail);
    }

    /**
     * Cập nhật thông tin chi tiết đơn hàng.
     * Lưu ý: Thường được gọi từ OrderService khi tạo/cập nhật Order.
     *
     * @param id ID của chi tiết đơn hàng cần cập nhật.
     * @param orderDetailRequestDTO DTO chứa thông tin cập nhật.
     * @return OrderDetailResponseDTO của chi tiết đơn hàng đã cập nhật.
     * @throws ResourceNotFoundException nếu OrderDetail hoặc MenuItem không tồn tại.
     */
    @Transactional
    public OrderDetailResponseDTO updateOrderDetail(int id, OrderDetailRequestDTO orderDetailRequestDTO) {
        OrderDetailEntity existingOrderDetail = orderDetailRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("OrderDetail not found with id " + id));

        // Cập nhật các trường thông thường
        orderDetailMapper.updateEntityFromDTO(orderDetailRequestDTO, existingOrderDetail);

        // Xử lý cập nhật MenuItem (nếu ID thay đổi)
        if (!existingOrderDetail.getMenuItem().getId().equals(orderDetailRequestDTO.getMenuItemId())) {
            MenuItemEntity newMenuItem = menuItemRepository.findById(orderDetailRequestDTO.getMenuItemId())
                    .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + orderDetailRequestDTO.getMenuItemId()));
            existingOrderDetail.setMenuItem(newMenuItem);
            // Cập nhật lại giá, tên, giảm giá từ MenuItem mới nếu thay đổi MenuItem
            existingOrderDetail.setPrice(BigDecimal.valueOf(newMenuItem.getPrice()));
            existingOrderDetail.setName(newMenuItem.getName());
            // existingOrderDetail.setDiscountPercentage(calculateDiscount(newMenuItem));
        }

        OrderDetailEntity updatedOrderDetail = orderDetailRepository.save(existingOrderDetail);
        return orderDetailMapper.toResponseDTO(updatedOrderDetail);
    }

    /**
     * Xóa một chi tiết đơn hàng.
     * Lưu ý: Thường được gọi từ OrderService khi xóa một Order hoặc loại bỏ một item khỏi Order.
     *
     * @param id ID của chi tiết đơn hàng cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy.
     */
    public void deleteOrderDetail(int id) {
        if (!orderDetailRepository.existsById(id)) {
            throw new ResourceNotFoundException("OrderDetail not found with id " + id);
        }
        orderDetailRepository.deleteById(id);
    }
}