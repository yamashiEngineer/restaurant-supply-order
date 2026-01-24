package com.example.Repository;

import com.example.Entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * 発注依頼テーブル用リポジトリー
 */
@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

  /**
   * 最新の申請日順に全件取得する
   */
  List<Order> findAllByOrderByAppliedAtDesc();

  /**
   * 特定のステータスの注文を絞り込む
   */
  List<Order> findByStatusCode(Integer statusCode);

  /**
   * 申請者IDで絞り込む
   */
  List<Order> findByUserId(String userId);

  /**
   * ページング対応で最新の申請日順に全件取得する
   */
  Page<Order> findAllByOrderByAppliedAtDesc(Pageable pageable);
}
