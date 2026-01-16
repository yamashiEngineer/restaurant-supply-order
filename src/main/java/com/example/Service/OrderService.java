package com.example.Service;

import org.springframework.stereotype.Service;

import com.example.Entity.Order;
import com.example.Entity.StatusHistory;
import com.example.Repository.OrderRepository;
import com.example.Repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
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
}
