package com.example.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ステータス情報のエンティティ
 */

@Entity
@Table(name = "m_statuses")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Status {

  /** ステータスコード（10: 未着手, 20: 確認中, 30: 発送済, 40: 完了） */
  @Id
  @Column(name = "status_code", length = 10)
  private Integer statusCode;

  /** ステータス名 （画面表示用の名称） */
  @Column(name = "status_name", nullable = false, length = 20)
  private String statusName;
}
