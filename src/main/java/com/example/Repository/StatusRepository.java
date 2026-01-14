package com.example.Repository;

import com.example.Entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * ステータスマスタ用リポジトリー
 */
@Repository
public interface StatusRepository extends JpaRepository<Status, Integer> {

  /**
   * ステータス名からエンティティを取得する
   */
  Optional<Status> findByStatusName(String statusName);
}
