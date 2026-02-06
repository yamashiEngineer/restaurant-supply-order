package com.example.Repository;

import com.example.Entity.Order;
import com.example.Entity.Users;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

/**
 * ユーザーテーブル用リポジトリー
 */
@Repository
public interface UserRepository extends JpaRepository<Users, String> {

  /*
   * ユーザーIDで絞り込む
   */
  Optional<Users> findByUserId(String userId);
}
