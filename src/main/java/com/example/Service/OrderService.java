package com.example.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Entity.Order;
import com.example.Repository.OrderRepository;
import com.example.Repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

  private final OrderRepository orderRepository;
  private final StatusHistoryRepository statusHistoryRepository;

  /**
   * すべての発注一覧を取得する
   */
  public List<Order> getAllOrders() {
    return orderRepository.findAllByOrderByAppliedAtDesc();
  }

  /**
   * 新規発注を作成する
   */
  @Transactional
  public void createOrder(Order order) {
    order.setStatusCode(10);
    order.setUserId("1");
    order.setAppliedAt(LocalDateTime.now());
    orderRepository.save(order);
  }

  @Transactional(readOnly = true)
  public Page<Order> findPaginatedOrders(int page, int size) {

    return orderRepository.findAllByOrderByAppliedAtDesc(PageRequest.of(page, size));
  }

  /**
   * 指定したIDの発注を取得する
   */
  public Order findOrderById(Integer id) {
    return orderRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("指定したIDが見つかりません: " + id));
  }
}
