package com.example.Service;

import org.springframework.stereotype.Service;

import com.example.Entity.Order;
import com.example.Repository.OrderRepository;
import com.example.Repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;

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

}
