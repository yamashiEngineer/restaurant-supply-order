package com.example.Repository;

import com.example.Entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.EntityGraph;

import java.util.List;

/**
 * ステータス変更履歴テーブル用リポジトリー
 */
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Integer> {
  /**
   * すべてのステータス変更履歴を取得する
   * （履歴管理画面用）
   */
  @EntityGraph(attributePaths = { "changer" })
  List<StatusHistory> findAll();

  /**
   * 特定の発注IDに関連する履歴を、新しい順に取得する
   * （詳細画面のタイムライン表示用）
   */
  @EntityGraph(attributePaths = { "changer" })
  List<StatusHistory> findByOrderIdOrderByChangedAtDesc(Integer orderId);
}
