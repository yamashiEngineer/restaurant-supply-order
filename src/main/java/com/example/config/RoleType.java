package com.example.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RoleType {
  SHOP(1, "店舗スタッフ"),
  HEAD_OFFICE(2, "本部スタッフ");

  private final int code;
  private final String name;

  public static RoleType fromCode(int code) {
    for (RoleType role : values()) {
      if (role.getCode() == code) {
        return role;
      }
    }
    throw new IllegalArgumentException("不正な権限コードです: " + code);
  }
}
