package com.example.controller;

import com.example.Entity.Users;
import com.example.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class UserController {

  private final UserRepository userRepository;

  @GetMapping
  public String showLogin() {
    return "login";
  }

  @GetMapping("/users")
  public List<Users> getAllUsers() {
    var users = userRepository.findAll();
    return users;
  }
}
