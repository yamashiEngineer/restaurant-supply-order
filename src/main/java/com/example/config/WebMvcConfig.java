package com.example.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebMvcConfig implements WebMvcConfigurer {

  private final LoginInterceptor loginInterceptor;

  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(loginInterceptor)
        .addPathPatterns("/**") // すべてのURLを監視対象にする
        .excludePathPatterns(
            "/login/**", // ログイン関連は除外
            "/h2-console/**", // DBコンソールは除外
            "/css/**", "/js/**", "/images/**" // 静的ファイルは除外
        );
  }
}