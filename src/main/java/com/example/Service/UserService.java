package com.example.Service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.Repository.UserRepository;

import jakarta.transaction.Transactional;

import com.example.Entity.Users;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public Users authenticate(String userId, String rawPassword) {
    // 1.DBからユーザーIDで検索
    Users user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));

    // 2.生パスワード（生のパスワード、DBのハッシュ値）で比較
    if (passwordEncoder.matches(rawPassword, user.getPassword())) {
      return user;
    } else {
      throw new RuntimeException("パスワードが間違っています");
    }
  }

  public void registerUser(Users user) {
    // パスワードをハッシュ化してセット
    String encodedPassword = passwordEncoder.encode(user.getPassword());
    user.setPassword(encodedPassword);

    userRepository.save(user);
  }

  @Transactional
  public void changePassword(String userId, String currentPassword, String newPassword) {
    // 1.ユーザーの存在確認
    Users user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません。"));

    // 2.現在のパスワードが正しいかチェック
    if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
      throw new RuntimeException("現在のパスワードが正しくありません。");
    }

    // 3.新しいパスワードをハッシュ化して保存
    user.setPassword(passwordEncoder.encode(newPassword));
    userRepository.save(user);
  }
}