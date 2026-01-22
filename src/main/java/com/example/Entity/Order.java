package com.example.Entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

import org.antlr.v4.runtime.misc.NotNull;

/**
 * 発注依頼テーブルのエンティティ
 * テーブル名： t_orders
 */
@Entity
@Table(name = "t_orders")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

  /** 発注ID（自動採番） */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "order_id")
  private Integer orderId;

  /** 申請者ID（m_users.user_idを参照） */
  @Column(name = "user_id", length = 20, nullable = false)
  private String userId;

  /** 備品名 */
  @NotBlank
  @Size(max = 100)
  @Column(name = "item_name", length = 100, nullable = false)
  private String itemName;

  /** 個数 */
  @NotNull
  @Min(1)
  @Column(name = "quantity", nullable = false)
  private Integer quantity;

  /** 備考 */
  @Column(name = "remarks", length = 500)
  private String remarks;

  /** ステータスコード（m_statuses.status_codeを参照） */
  @Column(name = "status_code", nullable = false)
  private Integer statusCode;

  /** 申請日時 */
  @Column(name = "applied_at", nullable = false)
  private LocalDateTime appliedAt;

  /** 更新日時 */
  @Column(name = "updated_at")
  private LocalDateTime updatedAt;
}
