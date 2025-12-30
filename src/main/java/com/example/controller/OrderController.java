package com.example.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OrderController {

  @GetMapping("/list")
  public String showList() {
    return "list";
  }

  @GetMapping("/create")
  public String showCreate() {
    return "create";
  }

  @GetMapping("/edit")
  public String showEdit() {
    return "edit";
  }

  @GetMapping("/login")
  public String showLogin() {
    return "login";
  }
}
