package com.example.Service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import com.example.Entity.Order;
import com.example.Entity.StatusHistory;
import com.example.Entity.Users;
import com.example.Repository.OrderRepository;
import com.example.Repository.StatusHistoryRepository;
import lombok.RequiredArgsConstructor;
import java.time.LocalDateTime;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

  private final OrderRepository orderRepository;
  private final StatusHistoryRepository statusHistoryRepository;
  private final HttpSession session;

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

  /**
   * ステータスの更新と履歴の保存
   */
  @Transactional
  public void updateOrderStatus(Integer orderId, Integer newStatusCode, String comment) {
    // 1.現在の発注情報を取得
    Order order = findOrderById(orderId);
    Integer oldStatusCode = order.getStatusCode();

    // セッションから現在ログイン中のユーザーIDを取得
    Users loginUser = (Users) session.getAttribute("loginUser");

    // 2.ステータスを更新して保存
    order.setStatusCode(newStatusCode);
    order.setUpdatedAt(LocalDateTime.now());
    orderRepository.save(order);

    // 3.履歴を作成して保存
    StatusHistory history = StatusHistory.builder()
        .orderId(orderId)
        .beforeStatusCode(oldStatusCode)
        .afterStatusCode(newStatusCode)
        .changedAt(LocalDateTime.now())
        .changedBy(loginUser.getUserId())
        .comment(comment)
        .build();

    statusHistoryRepository.save(history);
  }
}
