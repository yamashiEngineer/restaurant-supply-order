package com.example.Service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.Repository.UserRepository;
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
}
