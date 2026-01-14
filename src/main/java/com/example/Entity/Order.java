package com.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

/**
 * 発注依頼テーブルのエンティティ
 * テーブル名： t_orders
 */
@Entity
@Table(name = "t_orders")
@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Order {

  /** 発注ID（自動採番） */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Integer orderId;

  /** 申請者ID（m_users.user_idを参照） */
  @Column(name = "user_id", length = 20, nullable = true)
  private String userId;

  /** 備品名 */
  @Column(name = "item_name", length = 100, nullable = true)
  private String itemName;

  /** 個数 */
  @Column(name = "quantity", nullable = true)
  private Integer quantity;

  /** ステータスコード（m_statuses.status_codeを参照） */
  @Column(name = "status_code", nullable = true)
  private Integer statusCode;

  /** 申請日時 */
  @Column(name = "applied_at", nullable = true)
  private LocalDateTime appliedAt;

  /** 更新日時 */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
