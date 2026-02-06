package com.example.Service;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import com.example.Repository.UserRepository;
import com.example.Entity.Users;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;

  public Users authenticate(String userId, String password) {
    // 1.DBからユーザーIDで検索
    Users user = userRepository.findByUserId(userId)
        .orElseThrow(() -> new RuntimeException("ユーザーが存在しません"));

    // 2.パスワードの照合
    if (user.getPassword().equals(password)) {
      return user;
    } else {
      throw new RuntimeException("パスワードが間違っています");
    }
  }
}
