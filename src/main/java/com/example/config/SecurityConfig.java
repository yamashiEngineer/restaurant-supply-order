package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/h2-console/**").permitAll() // H2コンソールを許可
            .anyRequest().permitAll())
        .csrf(csrf -> csrf.ignoringRequestMatchers("/h2-console/**")) // H2用のみCSRF無効化
        .headers(headers -> headers.frameOptions(frame -> frame.sameOrigin())) // フレーム表示を許可
        .formLogin(login -> login.loginPage("/login").permitAll());

    return http.build();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}
