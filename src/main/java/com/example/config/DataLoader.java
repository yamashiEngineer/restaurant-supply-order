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

    // 店舗スタッフ（追加分）
    List<String> tenpoNames = List.of(
        "梅田", "難波", "天王寺", "心斎橋", "京橋", "淀屋橋", "本町", "江坂", "茨木", "高槻",
        "吹田", "豊中", "枚方", "堺東", "鳳", "三宮", "元町", "西宮北口", "夙川", "芦屋",
        "尼崎", "伊丹", "宝塚", "川西", "明石", "加古川", "姫路", "京都河原町", "烏丸", "桂",
        "伏見", "宇治", "大津", "草津", "奈良", "生駒", "和歌山市", "岩出", "千里中央", "くずは");

    // 複数ユーザーの登録
    for (int i = 0; i < tenpoNames.size(); i++) {
      String cityName = tenpoNames.get(i);
      // ユーザーIDは tenpo01, tenpo02...
      String userId = String.format("tenpo%02d", i + 1);

      if (!userRepository.existsByUserId(userId)) {
        Users user = new Users();
        user.setUserId(userId);
        user.setPassword(passwordEncoder.encode("tenpo"));

        user.setUserName(cityName + "店スタッフ");

        user.setRoleType(RoleType.SHOP.getCode());
        userRepository.save(user);
      }
    }

    // 共通パスワードのハッシュ化
    String commonHash = passwordEncoder.encode("password");

    // 2. 発注データの投入
    // すでにデータがある場合は投入しない（二重登録防止）
    if (orderRepository.count() > 0) {
      return;
    }

    List<String> supplies = List.of(
        "パスタ（スパゲッティ 1.6mm）", "トマトソース（缶）", "エクストラバージンオリーブオイル", "グラナパダーノチーズ",
        "冷凍ハンバーグ（100g）", "ミラノ風ドリアソース", "辛味チキン（冷凍）", "エスカルゴ（殻なし）",
        "生ハム（プロシュート）", "モッツァレラチーズ", "ライス（千葉県産コシヒカリ）", "たまご（Lサイズ）",
        "ワイン（赤・マグナム）", "ワイン（白・デキャンタ）", "ドリンクバー用シロップ（コーラ）", "ティーバッグ（アールグレイ）",
        "ペーパーナプキン（ロゴ入り）", "割り箸（個包装）", "プラスチックスプーン", "おしぼり（業務用）",
        "食器用洗剤（中性）", "除菌用アルコール噴霧剤", "キッチンペーパー", "ゴミ袋（45L・透明）",
        "メニューブック（春・夏版）", "オーダーエントリー端末用ロール紙", "テーブルクロスクリーナー", "トイレ掃除用ブラシ",
        "サラダ用レタス", "食用油（フライヤー用）", "ポテト（フレンチフライ）", "コーン（缶詰）",
        "パン（ミニフィセル）", "デザート用ティラミス", "イタリアンプリン", "チョコレートソース",
        "テイクアウト用容器（M）", "テイクアウト用手提げ袋", "ユニフォーム（ポロシャツ L）", "名札（スペア）");

    for (int i = 0; i < supplies.size(); i++) {
      // 1. Order（注文）の作成
      Order order = new Order();
      order.setUserId("tenpo_user"); // 店舗ユーザーが申請者
      order.setItemName(supplies.get(i));
      order.setQuantity(i + 1); // 1, 2, 3...
      order.setAppliedAt(LocalDateTime.now().minusDays(40 - i)); // 40日前から順に
      order.setStatusCode(40); // 40 = 完了
      Order savedOrder = orderRepository.save(order);

      // 2. StatusHistory（履歴）の作成
      StatusHistory history = new StatusHistory();
      history.setOrderId(savedOrder.getOrderId());
      history.setBeforeStatusCode(20); // 20 = 確認中
      history.setAfterStatusCode(40); // 40 = 完了
      history.setChangedBy("honbu_user"); // 本部ユーザーがステータス変更を実行
      history.setChangedAt(savedOrder.getAppliedAt().plusHours(1));
      statusHistoryRepository.save(history);
    }

    System.out.println("--- データ投入完了 ---");
  }
}