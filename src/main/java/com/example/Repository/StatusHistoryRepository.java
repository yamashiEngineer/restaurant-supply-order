package com.example.Repository;

import com.example.Entity.StatusHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ステータス変更履歴テーブル用リポジトリー
 */
@Repository
public interface StatusHistoryRepository extends JpaRepository<StatusHistory, Integer> {

  /**
   * 特定の発注IDに関連する履歴を、新しい順に取得する
   * （詳細画面のタイムライン表示用）
   */
  List<StatusHistory> findByOrderIdOrderByChangedAtDesc(Integer orderId);
}
