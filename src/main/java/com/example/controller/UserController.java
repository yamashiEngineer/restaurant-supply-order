package com.example.controller;

import com.example.Entity.Users;
import com.example.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.bind.annotation.PostMapping;
import com.example.Service.UserService;
import jakarta.servlet.http.HttpSession;

import java.util.List;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserController {

  private final UserService userService;
  private final HttpSession session;

  // ログイン画面の表示
  @GetMapping
  public String showLogin() {
    return "login";
  }

  // ログイン実行処理
  @PostMapping("/process")
  public String processLogin(@RequestParam String userId, @RequestParam String password,
      RedirectAttributes redirectAttributes) {

    try {
      // DB連携による認証の実行
      Users user = userService.authenticate(userId, password);

      // 認証成功：セッションにユーザー情報を保存
      session.setAttribute("loginUser", user);

      // 発注リクエスト画面へリダイレクト
      return "redirect:/orders/new";

    } catch (RuntimeException e) {
      // 認証失敗（ユーザー不在、パスワード不一致）
      redirectAttributes.addFlashAttribute("error", "ユーザーIDまたはパスワードが違います");
      return "redirect:/login";
    }

  }
}
