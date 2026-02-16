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
import com.example.config.RoleType;

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

      RoleType role = RoleType.fromCode(user.getRoleType());

      // 「想定外」を弾く（ガード句）
      if (user == null || (user.getRoleType() != 1 && user.getRoleType() != 2)) {
        throw new RuntimeException("不正な権限を持つユーザーです");
      }

      if (role == RoleType.HEAD_OFFICE) {
        // 本部ユーザーの場合、発注一覧画面へリダイレクト
        return "redirect:/orders";
      }
      // 店舗ユーザーの場合、新規発注画面へリダイレクト
      return "redirect:/orders/new";

    } catch (RuntimeException e) {
      // 認証失敗（ユーザー不在、パスワード不一致）
      redirectAttributes.addFlashAttribute("error", "ユーザーIDまたはパスワードが違います");
      return "redirect:/login";
    }

  }

  @GetMapping("/logout")
  public String logout(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
  }

  /* パスワード変更画面の表示 */
  @GetMapping("/password")
  public String showChangePassword() {
    return "password_change";
  }

  /* パスワード変更の実行 */
  @PostMapping("/password")
  public String changePassword(@RequestParam String currentPassword, @RequestParam String newPassword,
      RedirectAttributes redirectAttributes) {
    // セッションから現在ログイン中のユーザーIDを取得
    Users loginUser = (Users) session.getAttribute("loginUser");

    try {
      userService.changePassword(loginUser.getUserId(), currentPassword, newPassword);
      session.invalidate(); // パスワード変更後はセッションを無効化して再ログインを促す
      redirectAttributes.addFlashAttribute("successMessage", "パスワードを変更しました。");
      return "redirect:/login";
    } catch (RuntimeException e) {
      redirectAttributes.addFlashAttribute("error", e.getMessage());
      return "redirect:/login/password";
    }
  }
}
