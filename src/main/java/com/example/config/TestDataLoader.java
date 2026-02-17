package com.example.config;

import com.example.Entity.Order;
import com.example.Entity.Status;
import com.example.Entity.StatusHistory;
import com.example.Entity.Users;
import com.example.Repository.OrderRepository;
import com.example.Repository.StatusHistoryRepository;
import com.example.Repository.StatusRepository;
import com.example.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Component
@RequiredArgsConstructor
public class TestDataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final StatusRepository statusRepository;
  private final OrderRepository orderRepository;
  private final StatusHistoryRepository statusHistoryRepository;
  private final PasswordEncoder passwordEncoder;
  private final Random random = new Random();

  @Override
  @Transactional // 一括処理のためにトランザクションを貼る
  public void run(String... args) {
    System.out.println("--- テストデータ投入開始 ---");

    // 1. ステータスマスタ（固定値）
    var statuses = List.of(
        Status.builder().statusCode(10).statusName("未着手").build(),
        Status.builder().statusCode(20).statusName("確認中").build(),
        Status.builder().statusCode(30).statusName("発送済").build(),
        Status.builder().statusCode(40).statusName("完了").build());
    statusRepository.saveAll(statuses);

    // 2. 利用者マスタ: 1000件
    String commonHash = passwordEncoder.encode("password");

    var userList = IntStream.rangeClosed(1, 1000).mapToObj(i -> {
      // 偶数なら店舗、奇数なら本部
      RoleType role = (i % 2 == 0) ? RoleType.SHOP : RoleType.HEAD_OFFICE;

      return Users.builder()
          .userId("user_id" + String.format("%04d", i))
          .password(commonHash)
          .userName("テスト太郎" + i)
          .roleType(role.getCode()) // Enumから数字を取得
          .build();
    }).toList();
    userRepository.saveAll(userList);

    // 3. オーダーテーブル: 500件
    var orderList = IntStream.rangeClosed(1, 500).mapToObj(i -> Order.builder()
        .userId(userList.get(random.nextInt(1000)).getUserId()) // ランダムなユーザー
        .itemName("備品名称-" + i)
        .quantity(random.nextInt(10) + 1)
        .statusCode(10) // 初期状態は未着手
        .appliedAt(LocalDateTime.now().minusDays(random.nextInt(30))) // 過去30日以内
        .build()).toList();
    var savedOrders = orderRepository.saveAll(orderList);

    // 4. ステータス変更履歴: 3000件 (ステータス変更)
    // ※ご要望に合わせて「ステータス変更/履歴」として一括で3000件作成
    var historyList = IntStream.rangeClosed(1, 3000).mapToObj(i -> {
      var targetOrder = savedOrders.get(random.nextInt(savedOrders.size()));
      return StatusHistory.builder()
          .orderId(targetOrder.getOrderId())
          .beforeStatusCode(10)
          .afterStatusCode(20)
          .changedBy(userList.get(random.nextInt(1000)).getUserId())
          .changedAt(LocalDateTime.now().minusHours(random.nextInt(100)))
          .comment("自動生成テストデータ-" + i)
          .build();
    }).toList();
    statusHistoryRepository.saveAll(historyList);

    System.out.println("--- テストデータ投入完了 ---");
  }
}