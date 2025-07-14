package com.poly.restaurant.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.poly.restaurant.dtos.OrderRequestDTO;
import com.poly.restaurant.dtos.OrderResponseDTO;
import com.poly.restaurant.dtos.PaymentRequestDTO;
import com.poly.restaurant.dtos.PaymentResponseDTO;
import com.poly.restaurant.dtos.UpdateOrderItemQuantityRequestDTO;
import com.poly.restaurant.entities.AccountEntity;
import com.poly.restaurant.entities.BranchComboEntity;
import com.poly.restaurant.entities.BranchDishEntity;
import com.poly.restaurant.entities.BranchEntity;
import com.poly.restaurant.entities.ComboEntity;
import com.poly.restaurant.entities.DishEntity;
import com.poly.restaurant.entities.MenuItemEntity;
import com.poly.restaurant.entities.OrderDetailEntity;
import com.poly.restaurant.entities.OrderEntity;
import com.poly.restaurant.entities.TableEntity;
import com.poly.restaurant.entities.enums.BranchDishStatus;
import com.poly.restaurant.entities.enums.OrderStatus;
import com.poly.restaurant.entities.enums.PaymentMethod;
import com.poly.restaurant.entities.enums.TableStatus;
import com.poly.restaurant.exceptions.ResourceNotFoundException;
import com.poly.restaurant.mappers.MenuItemMapperImpl;
import com.poly.restaurant.mappers.OrderMapper;
import com.poly.restaurant.repositories.AccountRepository;
import com.poly.restaurant.repositories.BranchComboRepository;
import com.poly.restaurant.repositories.BranchDishRepository;
import com.poly.restaurant.repositories.BranchRepository;
import com.poly.restaurant.repositories.MenuItemRepository;
import com.poly.restaurant.repositories.OrderRepository;
import com.poly.restaurant.repositories.TableRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class OrderService {

	private final MenuItemMapperImpl menuItemMapperImpl;

	private final OrderRepository orderRepository;
	private final TableRepository tableRepository;
	private final AccountRepository accountRepository;
	private final BranchRepository branchRepository;
	private final MenuItemRepository menuItemRepository; 
	private final OrderMapper orderMapper;
	
	private final BranchDishRepository branchDishRepository;
	private final BranchComboRepository branchComboRepository;

	public List<OrderResponseDTO> getAllOrders() {
		return orderRepository.findAll().stream().map(orderMapper::toResponseDTO).collect(Collectors.toList());
	}

	public OrderResponseDTO getOrderById(Long id) {
		OrderEntity order = orderRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng với ID: " + id));
		return orderMapper.toResponseDTO(order);
	}

	@Transactional
	public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
		// 1. Tìm các thực thể liên quan
		TableEntity table = tableRepository.findById(orderRequestDTO.getTableId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy bàn: " + orderRequestDTO.getTableId()));
		AccountEntity account = accountRepository.findById(orderRequestDTO.getAccountId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy tài khoản: " + orderRequestDTO.getAccountId()));
		BranchEntity branch = branchRepository.findById(orderRequestDTO.getBranchId()).orElseThrow(
				() -> new ResourceNotFoundException("Không tìm thấy chi nhánh: " + orderRequestDTO.getBranchId()));

		// 2. Quy tắc nghiệp vụ: Không cho tạo đơn hàng trên bàn đang có khách
		if (table.getStatus() == TableStatus.OCCUPIED) {
			throw new IllegalStateException("Bàn " + table.getName() + " đang được sử dụng.");
		}

		// 3. Tạo đối tượng OrderEntity
		OrderEntity order = new OrderEntity();
		order.setTable(table);
		order.setAccount(account);
		order.setBranch(branch);
		order.setStatus(OrderStatus.PENDING); // Mặc định là chưa thanh toán
		order.setPaymentMethod(PaymentMethod.CASH); // Mặc định là tiền mặt

		// Lấy tiền trả trước từ DTO, nếu client không gửi thì mặc định là 0
		BigDecimal prepayAmount = orderRequestDTO.getPrepay() != null ? orderRequestDTO.getPrepay() : BigDecimal.ZERO;
		order.setPrepay(prepayAmount);

		BigDecimal totalAmount = BigDecimal.ZERO;
		List<OrderDetailEntity> orderDetails = new ArrayList<>();

		// 4. Tạo các OrderDetail và tính tổng tiền
		for (var itemDTO : orderRequestDTO.getItems()) {
			MenuItemEntity menuItem = menuItemRepository.findById(itemDTO.getMenuItemId()).orElseThrow(
					() -> new ResourceNotFoundException("Không tìm thấy món với ID: " + itemDTO.getMenuItemId()));

			BigDecimal finalPrice = getPriceForMenuItem(menuItem, branch);
			
			OrderDetailEntity orderDetail = new OrderDetailEntity();
			orderDetail.setOrder(order);
			orderDetail.setMenuItem(menuItem);
			orderDetail.setQuantity(itemDTO.getQuantity());
			orderDetail.setName(menuItem.getName());
			 orderDetail.setPrice(finalPrice);
			orderDetails.add(orderDetail);
		}

		order.setOrderDetails(orderDetails);
		order.setTotalAmount(totalAmount);

		// 5. Cập nhật trạng thái bàn
		table.setStatus(TableStatus.OCCUPIED);
		tableRepository.save(table);

		// 6. Lưu đơn hàng
		OrderEntity savedOrder = orderRepository.save(order);
		return orderMapper.toResponseDTO(savedOrder);
	}
	
	/**
     * Phương thức private để lấy giá của một MenuItem tại một chi nhánh cụ thể.
     */
    private BigDecimal getPriceForMenuItem(MenuItemEntity menuItem, BranchEntity branch) {
        if (menuItem instanceof DishEntity) {
            // Nếu là món ăn, tìm giá trong BranchDishEntity
            DishEntity dish = (DishEntity) menuItem;
            return branchDishRepository.findByBranchAndDish(branch, dish)
                    .filter(bd -> bd.getStatus() == BranchDishStatus.AVAILABLE)
                    .map(BranchDishEntity::getPrice)
                    .orElseThrow(() -> new RuntimeException("Món ăn '" + dish.getName() + "' không có sẵn tại chi nhánh này."));
        } else if (menuItem instanceof ComboEntity) {
            // Nếu là combo, tìm giá trong BranchComboEntity
            ComboEntity combo = (ComboEntity) menuItem;
            return branchComboRepository.findByBranchAndCombo(branch, combo)
                    .filter(bc -> bc.getStatus() == com.poly.restaurant.entities.enums.BranchComboStatus.AVAILABLE)
                    .map(BranchComboEntity::getPrice)
                    .orElseThrow(() -> new RuntimeException("Combo '" + combo.getName() + "' không có sẵn tại chi nhánh này."));
        }
        throw new IllegalArgumentException("Loại sản phẩm không xác định.");
    }

	// Các phương thức khác như thanh toán, hủy đơn... sẽ được thêm ở đây

	@Transactional
	public void cancelOrder(Long orderId) {
		OrderEntity order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng: " + orderId));

		// Kiểm tra xem đơn hàng có ở trạng thái cho phép hủy không
		if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
			throw new IllegalStateException(
					"Chỉ có thể hủy đơn hàng khi đang ở trạng thái 'Chờ xử lý' hoặc 'Đã xác nhận'.");
		}

		order.setStatus(OrderStatus.CANCELED);

		// Trả lại trạng thái cho bàn ăn
		TableEntity table = order.getTable();
		if (table != null) {
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
		}

		orderRepository.save(order);
	}

	@Transactional
	public PaymentResponseDTO markAsPaid(Long orderId, PaymentRequestDTO paymentRequest) {
		OrderEntity order = orderRepository.findById(orderId)
				.orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng: " + orderId));

		if (order.getStatus() == OrderStatus.COMPLETED || order.getStatus() == OrderStatus.CANCELED) {
			throw new IllegalStateException("Đơn hàng này đã kết thúc hoặc đã bị hủy.");
		}

		BigDecimal changeDue = BigDecimal.ZERO;

		// Xử lý logic nếu thanh toán bằng tiền mặt
		if (paymentRequest.getPaymentMethod() == PaymentMethod.CASH) {
			BigDecimal cashReceived = paymentRequest.getCashReceived();
			if (cashReceived == null || cashReceived.compareTo(order.getTotalAmount()) < 0) {
				throw new IllegalArgumentException("Số tiền khách đưa không đủ để thanh toán.");
			}
			changeDue = cashReceived.subtract(order.getTotalAmount());
		}

		order.setStatus(OrderStatus.COMPLETED);
		order.setPaymentMethod(paymentRequest.getPaymentMethod());

		// Trả lại trạng thái cho bàn ăn
		TableEntity table = order.getTable();
		if (table != null) {
			table.setStatus(TableStatus.AVAILABLE);
			tableRepository.save(table);
		}

		OrderEntity savedOrder = orderRepository.save(order);

		// Map entity sang DTO để trả về
		OrderResponseDTO orderResponse = orderMapper.toResponseDTO(savedOrder);

		return new PaymentResponseDTO(orderResponse, changeDue);
	}

	// Trong file OrderService.java

	@Transactional
	public OrderResponseDTO updateOrderItemQuantity(Long orderId, Long orderDetailId, UpdateOrderItemQuantityRequestDTO request) {
	    // 1. Tìm đơn hàng
	    OrderEntity order = orderRepository.findById(orderId)
	            .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy đơn hàng: " + orderId));

	    // 2. Kiểm tra trạng thái đơn hàng
	    if (order.getStatus() != OrderStatus.PENDING && order.getStatus() != OrderStatus.CONFIRMED) {
	        throw new IllegalStateException("Không thể cập nhật đơn hàng đã hoàn thành hoặc đã hủy.");
	    }

	    // 3. Tìm chi tiết món ăn trong đơn hàng đó
	    OrderDetailEntity orderDetail = order.getOrderDetails().stream()
	            .filter(detail -> detail.getId().equals(orderDetailId))
	            .findFirst()
	            .orElseThrow(() -> new ResourceNotFoundException(
	                    "Không tìm thấy món ăn với ID " + orderDetailId + " trong đơn hàng này."));

	    int newQuantity = request.getNewQuantity();

	    if (newQuantity < 0) {
	        throw new IllegalArgumentException("Số lượng không thể là số âm.");
	    }

	    // 4. Xử lý logic cập nhật hoặc xóa món ăn
	    if (newQuantity == 0) {
	        order.getOrderDetails().remove(orderDetail);
	    } else {
	        orderDetail.setQuantity(newQuantity);
	    }

	    // 5. KIỂM TRA NẾU ĐƠN HÀNG RỖNG THÌ XÓA
	    if (order.getOrderDetails().isEmpty()) {
	        // Trả lại trạng thái cho bàn
	        TableEntity table = order.getTable();
	        if (table != null) {
	            table.setStatus(TableStatus.AVAILABLE);
	            tableRepository.save(table);
	        }
	        
	        orderRepository.delete(order);
	        return null; // Trả về null để báo hiệu đơn hàng đã bị xóa
	    } else {
	        // Nếu đơn hàng vẫn còn món, tính lại tổng tiền và lưu
	        recalculateOrderTotal(order);
	        OrderEntity updatedOrder = orderRepository.save(order);
	        return orderMapper.toResponseDTO(updatedOrder);
	    }
	}

	/**
	 * Phương thức private để tính toán lại tổng tiền cho đơn hàng.
	 * 
	 * @param order Đơn hàng cần tính toán lại.
	 */
	private void recalculateOrderTotal(OrderEntity order) {
		BigDecimal totalAmount = BigDecimal.ZERO;
		for (OrderDetailEntity detail : order.getOrderDetails()) {
			// Lấy giá đã lưu trong OrderDetail nhân với số lượng mới
			BigDecimal itemTotal = detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
			totalAmount = totalAmount.add(itemTotal);
		}
		order.setTotalAmount(totalAmount);
	}

}