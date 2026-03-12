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

import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import org.springframework.security.crypto.password.PasswordEncoder;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

  private final UserRepository userRepository;
  private final StatusRepository statusRepository;
  private final OrderRepository orderRepository;
  private final StatusHistoryRepository statusHistoryRepository;
  private final PasswordEncoder passwordEncoder;
  private final Random random = new Random();

  @Override
  public void run(String... args) throws Exception {

    System.out.println("--- データ投入開始 ---");

    // 1. ステータスマスタ（固定値）
    var statuses = List.of(
        Status.builder().statusCode(10).statusName("未着手").build(),
        Status.builder().statusCode(20).statusName("確認中").build(),
        Status.builder().statusCode(30).statusName("発送済").build(),
        Status.builder().statusCode(40).statusName("完了").build());
    statusRepository.saveAll(statuses);

    // 1. ユーザーデータの投入
    // 本部スタッフ
    if (userRepository.findByUserName("honbu_user").isEmpty()) {
      Users honbu = new Users();
      honbu.setUserId("honbu_user");
      honbu.setUserName("本部スタッフ");
      honbu.setPassword(passwordEncoder.encode("honbu"));
      honbu.setRoleType(RoleType.HEAD_OFFICE.getCode());
      userRepository.save(honbu);
    }

    // 店舗スタッフ
    if (userRepository.findByUserName("tenpo_user").isEmpty()) {
      Users tenpo = new Users();
      tenpo.setUserId("tenpo_user");
      tenpo.setUserName("店舗スタッフ");
      tenpo.setPassword(passwordEncoder.encode("tenpo"));
      tenpo.setRoleType(RoleType.SHOP.getCode());
      userRepository.save(tenpo);
    }

    // 共通パスワードのハッシュ化
    String commonHash = passwordEncoder.encode("password");

    // 2. 発注データの投入 (A-2, D系テスト用)
    // 「再発注」のテスト(A-2)で「既に存在する備品」として認識させるための初期データ
    // ※ repositoryやEntityのメソッド名はご自身の環境に合わせて調整してください
    if (orderRepository.count() == 0) {
      Order initialOrder = new Order();
      initialOrder.setItemName("A4コピー用紙"); // A-2テストでこれと同じ名前を入力する
      initialOrder.setQuantity(1);
      initialOrder.setUserId("test_user"); // 店舗ユーザーが申請者
      initialOrder.setAppliedAt(LocalDateTime.now());
      initialOrder.setStatusCode(20); // 20 = 発注済み
      orderRepository.save(initialOrder);

      // 1. 履歴オブジェクトを組み立てる
      StatusHistory history = StatusHistory.builder()
          .orderId(1) // 対象の発注ID
          .beforeStatusCode(10) // 変更前：未着手
          .afterStatusCode(20) // 変更後：確認中
          .changedBy("honbu_user") // 実行者のユーザーID
          .changedAt(LocalDateTime.now()) // 現在日時
          .comment("備品の在庫を確認したためステータスを更新しました。")
          .build();

      // 2. リポジトリを使ってDBへ保存
      statusHistoryRepository.save(history);
    }
    System.out.println("--- データ投入完了 ---");
  }
}