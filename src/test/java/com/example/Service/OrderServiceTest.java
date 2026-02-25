package com.example.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.Entity.Order;
import com.example.Entity.StatusHistory;
import com.example.Entity.Users;
import com.example.Repository.OrderRepository;
import com.example.Repository.StatusHistoryRepository;

import jakarta.servlet.http.HttpSession;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

  @Mock
  private OrderRepository orderRepository;

  @Mock
  private StatusHistoryRepository statusHistoryRepository;

  @Mock
  private HttpSession session;

  @InjectMocks
  private OrderService orderService;

  @Test
  @DisplayName("getAllOrders: 全ての発注データが日付降順で取得できること")
  void getAllOrders_Success() {

    List<Order> expectedOrders = Arrays.asList(new Order(), new Order(), new Order());
    when(orderRepository.findAllByOrderByAppliedAtDesc()).thenReturn(expectedOrders);

    List<Order> actualOrders = orderService.getAllOrders();

    assertEquals(expectedOrders, actualOrders);
    verify(orderRepository, times(1)).findAllByOrderByAppliedAtDesc();
  }

  @Test
  @DisplayName("createOrder: 初期値（ステータス10）がセットされて保存されること")
  void createOrder_Success() {
    Order order = new Order();

    orderService.createOrder(order);

    assertEquals(10, order.getStatusCode(), "初期ステータスは10であること");
    assertEquals("1", order.getUserId(), "初期ユーザーIDは1であること");
    assertNotNull(order.getAppliedAt(), "申請日時がセットされていること");
    verify(orderRepository).save(order);
  }

  @Test
  @DisplayName("findPaginatedOrders: 指定したページサイズでデータが取得できること")
  void findPaginatedOrders_Success() {
    int page = 0;
    int size = 5;
    Page<Order> expectedPage = new PageImpl<>(Arrays.asList(new Order()));
    when(orderRepository.findAllByOrderByAppliedAtDesc(any(PageRequest.class))).thenReturn(expectedPage);

    Page<Order> actualPage = orderService.findPaginatedOrders(page, size);

    assertEquals(expectedPage, actualPage);
    verify(orderRepository).findAllByOrderByAppliedAtDesc(PageRequest.of(page, size));
  }

  @Test
  @DisplayName("findOrderById: 存在するIDの場合、発注データが返ること")
  void findOrderById_Success() {
    Integer id = 1;
    Order order = new Order();
    when(orderRepository.findById(id)).thenReturn(Optional.of(order));

    Order result = orderService.findOrderById(id);

    assertEquals(order, result);
  }

  @Test
  @DisplayName("findOrderById: 存在しないIDの場合、例外がスローされること")
  void findOrderById_NotFound() {
    Integer id = 99999;
    when(orderRepository.findById(id)).thenReturn(Optional.empty());

    assertThrows(IllegalArgumentException.class, () -> orderService.findOrderById(id));
  }
}