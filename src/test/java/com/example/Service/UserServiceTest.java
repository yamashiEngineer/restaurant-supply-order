package com.example.Service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.example.Entity.Users;
import com.example.Repository.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private PasswordEncoder passwordEncoder;

  @InjectMocks
  private UserService userService;

  @Test
  @DisplayName("authenticate: パスワードが一致する場合にユーザーを返すこと")
  void authenticate_Success() {
    // Arrange
    String userId = "user1";
    String rawPassword = "password";
    Users user = new Users();
    user.setPassword("encoded_password");

    when(userRepository.findByUserId(userId)).thenReturn(Optional.of(user));
    when(passwordEncoder.matches(rawPassword, "encoded_password")).thenReturn(true);

    // Act
    Users result = userService.authenticate(userId, rawPassword);

    // Assert
    assertNotNull(result);
    verify(passwordEncoder).matches(rawPassword, "encoded_password");
  }

  @Test
  @DisplayName("registerUser: パスワードをハッシュ化して保存すること")
  void registerUser_Success() {
    // Arrange
    Users user = new Users();
    user.setPassword("raw_password");
    when(passwordEncoder.encode("raw_password")).thenReturn("hashed_password");

    // Act
    userService.registerUser(user);

    // Assert
    assertEquals("hashed_password", user.getPassword());
    verify(userRepository).save(user);
  }
}