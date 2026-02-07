package com.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {

  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
    HttpSession session = request.getSession();

    // セッションからログインユーザー情報を取得
    Object loginUser = session.getAttribute("loginUser");

    if (loginUser == null) {
      // ログインしていない場合、ログイン画面へリダイレクト
      response.sendRedirect(request.getContextPath() + "/login");
      return false; // コントローラーの処理を実行させない
    }

    return true; // ログイン済みなら処理を続行
  }
}