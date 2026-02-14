package com.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.example.Entity.Users;

@Component
public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();

    // セッションからログインユーザー情報を取得
    Users loginUser = (Users) session.getAttribute("loginUser");

    // リクエストされたURLを取得
    String uri = request.getRequestURI();

    if (loginUser == null) {
      // ログインしていない場合、ログイン画面へリダイレクト
      response.sendRedirect(request.getContextPath() + "/login");
      return false; // コントローラーの処理を実行させない
    }

    // 1. 本部専用画面（発注・編集）へのアクセス制限
    if (uri.contains("/orders/edit")) {
      if (loginUser.getRoleType() != 2) {
        response.sendRedirect(request.getContextPath() + "/orders?error=denied");
        return false;
      }
    }

    // 2. 店舗専用画面（新規登録）へのアクセス制限
    if (uri.contains("/orders/new")) {
      if (loginUser.getRoleType() != 1) {
        response.sendRedirect(request.getContextPath() + "/orders?error=denied");
        return false;
      }
    }
    return true; // ログイン済みなら処理を続行
  }
}