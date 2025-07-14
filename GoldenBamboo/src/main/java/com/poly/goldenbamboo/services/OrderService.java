package com.poly.goldenbamboo.services;

import com.poly.goldenbamboo.dtos.OrderDetailRequestDTO;
import com.poly.goldenbamboo.dtos.OrderRequestDTO;
import com.poly.goldenbamboo.dtos.OrderResponseDTO;
import com.poly.goldenbamboo.entities.OrderEntity;
import com.poly.goldenbamboo.entities.AccountEntity;
import com.poly.goldenbamboo.entities.BranchEntity;
import com.poly.goldenbamboo.entities.TableEntity;
import com.poly.goldenbamboo.entities.OrderDetailEntity;
import com.poly.goldenbamboo.entities.MenuItemEntity; // Để lấy thông tin cho OrderDetail
import com.poly.goldenbamboo.entities.enums.OrderStatus;
import com.poly.goldenbamboo.exceptions.ResourceNotFoundException;
import com.poly.goldenbamboo.mappers.OrderMapper;
import com.poly.goldenbamboo.mappers.OrderDetailMapper; // Cần để map OrderDetailRequestDTO
import com.poly.goldenbamboo.repositories.OrderRepository;
import com.poly.goldenbamboo.repositories.AccountRepository;
import com.poly.goldenbamboo.repositories.BranchRepository;
import com.poly.goldenbamboo.repositories.TableRepository;
import com.poly.goldenbamboo.repositories.OrderDetailRepository; // Cần để quản lý OrderDetail
import com.poly.goldenbamboo.repositories.MenuItemRepository; // Hoặc ComboRepository/DishRepository

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final AccountRepository accountRepository;
    private final BranchRepository branchRepository;
    private final TableRepository tableRepository;
    private final OrderDetailRepository orderDetailRepository; // Để quản lý chi tiết đơn hàng
    private final MenuItemRepository menuItemRepository;       // Để lấy thông tin món/combo
    private final OrderMapper orderMapper;
    private final OrderDetailMapper orderDetailMapper; // Dùng để chuyển đổi OrderDetailRequestDTO

    @Autowired
    public OrderService(OrderRepository orderRepository, AccountRepository accountRepository,
                        BranchRepository branchRepository, TableRepository tableRepository,
                        OrderDetailRepository orderDetailRepository, MenuItemRepository menuItemRepository,
                        OrderMapper orderMapper, OrderDetailMapper orderDetailMapper) {
        this.orderRepository = orderRepository;
        this.accountRepository = accountRepository;
        this.branchRepository = branchRepository;
        this.tableRepository = tableRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.menuItemRepository = menuItemRepository;
        this.orderMapper = orderMapper;
        this.orderDetailMapper = orderDetailMapper;
    }

    /**
     * Lấy tất cả các đơn hàng.
     *
     * @return Danh sách OrderResponseDTO.
     */
    public List<OrderResponseDTO> getAllOrders() {
        List<OrderEntity> orders = orderRepository.findAll();
        return orders.stream()
                .map(orderMapper::toResponseDTO)
                .collect(Collectors.toList());
    }

    /**
     * Lấy thông tin đơn hàng theo ID.
     *
     * @param id ID của đơn hàng.
     * @return OrderResponseDTO nếu tìm thấy.
     * @throws ResourceNotFoundException nếu không tìm thấy đơn hàng.
     */
    public OrderResponseDTO getOrderById(int id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));
        return orderMapper.toResponseDTO(order);
    }

    /**
     * Tạo một đơn hàng mới.
     *
     * @param orderRequestDTO DTO chứa thông tin đơn hàng cần tạo.
     * @return OrderResponseDTO của đơn hàng đã tạo.
     * @throws ResourceNotFoundException nếu Account, Branch hoặc Table không tồn tại.
     */
    @Transactional
    public OrderResponseDTO createOrder(OrderRequestDTO orderRequestDTO) {
        OrderEntity orderEntity = orderMapper.toEntity(orderRequestDTO);

        // 1. Xử lý mối quan hệ Account
        AccountEntity account = accountRepository.findById(orderRequestDTO.getAccountId())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderRequestDTO.getAccountId()));
        orderEntity.setAccount(account);

        // 2. Xử lý mối quan hệ Branch
        BranchEntity branch = branchRepository.findById(orderRequestDTO.getBranchId())
                .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + orderRequestDTO.getBranchId()));
        orderEntity.setBranch(branch);

        // 3. Xử lý mối quan hệ Table (nếu có)
        if (orderRequestDTO.getTableId() != null) {
            TableEntity table = tableRepository.findById(orderRequestDTO.getTableId())
                    .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + orderRequestDTO.getTableId()));
            orderEntity.setTable(table);
            // Cập nhật trạng thái bàn nếu cần (ví dụ: đang bận)
            // table.setStatus(TableStatus.OCCUPIED);
            // tableRepository.save(table);
        } else {
            orderEntity.setTable(null);
        }

        // Đảm bảo trạng thái ban đầu
        if (orderEntity.getStatus() == null) {
            orderEntity.setStatus(OrderStatus.PENDING); // Mặc định là Pending
        }
        
        // Tính toán tổng tiền (best practice là backend tính toán)
        BigDecimal calculatedTotalAmount = BigDecimal.ZERO;
        // Tạm thời set 0, sẽ cập nhật sau khi xử lý orderDetails
        orderEntity.setTotalAmount(BigDecimal.ZERO);
        
        // Lưu Order trước để có ID
        OrderEntity savedOrder = orderRepository.save(orderEntity);

        // 4. Xử lý OrderDetails
        List<OrderDetailEntity> orderDetails = new ArrayList<>();
        if (orderRequestDTO.getOrderDetails() != null && !orderRequestDTO.getOrderDetails().isEmpty()) {
            for (OrderDetailRequestDTO detailDTO : orderRequestDTO.getOrderDetails()) {
                MenuItemEntity menuItem = menuItemRepository.findById(detailDTO.getMenuItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + detailDTO.getMenuItemId()));

                OrderDetailEntity orderDetail = orderDetailMapper.toEntity(detailDTO);
                orderDetail.setOrder(savedOrder);
                orderDetail.setMenuItem(menuItem);

                // Quan trọng: Ghi lại giá, tên, giảm giá từ MenuItem hiện tại
                orderDetail.setPrice(BigDecimal.valueOf(menuItem.getPrice()));
                orderDetail.setName(menuItem.getName());
                // orderDetail.setDiscountPercentage(calculateDiscountForMenuItem(menuItem)); // Logic phức tạp nếu có

                orderDetails.add(orderDetail);
                calculatedTotalAmount = calculatedTotalAmount.add(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            }
            orderDetailRepository.saveAll(orderDetails); // Lưu tất cả chi tiết đơn hàng
            savedOrder.setOrderDetails(orderDetails); // Cập nhật danh sách chi tiết vào Order
        }

        // Cập nhật tổng tiền sau khi đã có các chi tiết
        savedOrder.setTotalAmount(calculatedTotalAmount);
        orderRepository.save(savedOrder); // Lưu lại lần nữa để cập nhật totalAmount

        return orderMapper.toResponseDTO(savedOrder);
    }

    /**
     * Cập nhật thông tin đơn hàng.
     *
     * @param id ID của đơn hàng cần cập nhật.
     * @param orderRequestDTO DTO chứa thông tin cập nhật.
     * @return OrderResponseDTO của đơn hàng đã cập nhật.
     * @throws ResourceNotFoundException nếu Order, Account, Branch hoặc Table không tồn tại.
     */
    @Transactional
    public OrderResponseDTO updateOrder(int id, OrderRequestDTO orderRequestDTO) {
        OrderEntity existingOrder = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Order not found with id " + id));

        orderMapper.updateEntityFromDTO(orderRequestDTO, existingOrder); // Cập nhật các trường chính

        // 1. Xử lý cập nhật mối quan hệ Account
        if (!existingOrder.getAccount().getId().equals(orderRequestDTO.getAccountId())) {
            AccountEntity newAccount = accountRepository.findById(orderRequestDTO.getAccountId())
                    .orElseThrow(() -> new ResourceNotFoundException("Account not found with id " + orderRequestDTO.getAccountId()));
            existingOrder.setAccount(newAccount);
        }

        // 2. Xử lý cập nhật mối quan hệ Branch
        if (!existingOrder.getBranch().getId().equals(orderRequestDTO.getBranchId())) {
            BranchEntity newBranch = branchRepository.findById(orderRequestDTO.getBranchId())
                    .orElseThrow(() -> new ResourceNotFoundException("Branch not found with id " + orderRequestDTO.getBranchId()));
            existingOrder.setBranch(newBranch);
        }

        // 3. Xử lý cập nhật mối quan hệ Table
        if (orderRequestDTO.getTableId() != null) {
            if (existingOrder.getTable() == null || !existingOrder.getTable().getId().equals(orderRequestDTO.getTableId())) {
                // Nếu bàn thay đổi, có thể cập nhật trạng thái bàn cũ/mới
                TableEntity newTable = tableRepository.findById(orderRequestDTO.getTableId())
                        .orElseThrow(() -> new ResourceNotFoundException("Table not found with id " + orderRequestDTO.getTableId()));
                existingOrder.setTable(newTable);
                // Cập nhật trạng thái bàn mới (ví dụ: đang bận)
                // newTable.setStatus(TableStatus.OCCUPIED);
                // tableRepository.save(newTable);
            }
        } else {
            // Nếu client gửi tableId là null, tức là muốn gỡ bỏ liên kết với bàn
            // Có thể cập nhật trạng thái bàn cũ thành trống
            // if (existingOrder.getTable() != null) {
            //     existingOrder.getTable().setStatus(TableStatus.AVAILABLE);
            //     tableRepository.save(existingOrder.getTable());
            // }
            existingOrder.setTable(null);
        }
        
        // 4. Xử lý cập nhật OrderDetails
        // Cách phổ biến là xóa tất cả các chi tiết cũ và tạo lại các chi tiết mới
        orderDetailRepository.deleteAll(existingOrder.getOrderDetails()); // Xóa các chi tiết cũ
        existingOrder.getOrderDetails().clear(); // Dọn dẹp list trong entity

        BigDecimal recalculatedTotalAmount = BigDecimal.ZERO;
        if (orderRequestDTO.getOrderDetails() != null && !orderRequestDTO.getOrderDetails().isEmpty()) {
            List<OrderDetailEntity> newOrderDetails = new ArrayList<>();
            for (OrderDetailRequestDTO detailDTO : orderRequestDTO.getOrderDetails()) {
                MenuItemEntity menuItem = menuItemRepository.findById(detailDTO.getMenuItemId())
                        .orElseThrow(() -> new ResourceNotFoundException("MenuItem not found with id " + detailDTO.getMenuItemId()));

                OrderDetailEntity orderDetail = orderDetailMapper.toEntity(detailDTO);
                orderDetail.setOrder(existingOrder); // Thiết lập order cha
                orderDetail.setMenuItem(menuItem);

                // Ghi lại giá, tên, giảm giá từ MenuItem hiện tại
                orderDetail.setPrice(BigDecimal.valueOf(menuItem.getPrice()));
                orderDetail.setName(menuItem.getName());
                // orderDetail.setDiscountPercentage(calculateDiscountForMenuItem(menuItem));

                newOrderDetails.add(orderDetail);
                recalculatedTotalAmount = recalculatedTotalAmount.add(orderDetail.getPrice().multiply(BigDecimal.valueOf(orderDetail.getQuantity())));
            }
            orderDetailRepository.saveAll(newOrderDetails); // Lưu các OrderDetail mới
            existingOrder.setOrderDetails(newOrderDetails); // Cập nhật danh sách mới vào Order
        }
        // Cập nhật lại tổng tiền của đơn hàng
        existingOrder.setTotalAmount(recalculatedTotalAmount);


        OrderEntity updatedOrder = orderRepository.save(existingOrder); // Lưu lại Order đã được cập nhật
        return orderMapper.toResponseDTO(updatedOrder);
    }

    /**
     * Xóa một đơn hàng theo ID.
     *
     * @param id ID của đơn hàng cần xóa.
     * @throws ResourceNotFoundException nếu không tìm thấy đơn hàng.
     */
    public void deleteOrder(int id) {
        if (!orderRepository.existsById(id)) {
            throw new ResourceNotFoundException("Order not found with id " + id);
        }
        // CascadeType.ALL và orphanRemoval=true trên orderDetails sẽ đảm bảo các chi tiết đơn hàng cũng bị xóa.
        orderRepository.deleteById(id);
    }
}