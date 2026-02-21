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
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;

/**
 * ステータス変更履歴テーブルのエンティティ
 * テーブル名： t_status_histories
 */
@Entity
@Table(name = "t_status_histories")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StatusHistory {

  /** 履歴ID（自動採番） */
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "history_id")
  private Integer historyId;

  /** 発注ID（t_orders.order_idを参照） */
  @Column(name = "order_id", nullable = false)
  private Integer orderId;

  /** 変更前ステータスコード */
  @Column(name = "before_status_code", nullable = false)
  private Integer beforeStatusCode;

  /** 変更後ステータスコード */
  @Column(name = "after_status_code", nullable = false)
  private Integer afterStatusCode;

  /** 変更実行者ID（m_users.user_idを参照） */
  @Column(name = "changed_by", nullable = false, length = 20)
  private String changedBy;

  /** 変更日時 */
  @Column(name = "changed_at", nullable = false)
  private LocalDateTime changedAt;

  /** 補足コメント（必要に応じて：例「入力ミスによる修正」など） */
  @Column(name = "comment", length = 255)
  private String comment;

  @ManyToOne
  @JoinColumn(name = "changed_by", referencedColumnName = "user_id", insertable = false, updatable = false)
  private Users changer;
}
