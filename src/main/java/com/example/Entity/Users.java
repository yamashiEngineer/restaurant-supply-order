package com.example.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ユーザー情報のエンティティ
 */
@Entity
@Table(name = "m_users")
@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Users {

  @Id
  @Column(name = "user_id", length = 20)
  private String userId;

  @Column(name = "password", nullable = true, length = 255)
  private String password;

  @Column(name = "user_name", nullable = true, length = 50)
  private String userName;

  @Column(name = "role_type", nullable = true)
  private Integer roleType;

  @Column(name = "store_id", length = 10)
  private String storeId;
}