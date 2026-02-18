package com.example.Entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum OrderStatus {
  UNSTARTED(10, "未着手", "bg-secondary"),
  CHECKING(20, "確認中", "bg-primary"),
  SHIPPED(30, "発送済", "bg-info"),
  COMPLETED(40, "完了", "bg-success");

  private final int code;
  private final String name;
  private final String colorClass;

  public static OrderStatus fromCode(int code) {
    for (OrderStatus status : values()) {
      if (status.getCode() == code) {
        return status;
      }
    }
    return UNSTARTED; // デフォルトは未着手
  }
}
