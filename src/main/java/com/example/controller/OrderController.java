package com.example.controller;

import com.example.Repository.OrderRepository;
import com.example.Service.OrderService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

/**
 * 発注一覧画面の制御を行うコントローラー
 */
@Controller
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

  private final OrderService orderService;

  /**
   * 発注一覧画面を表示する
   * URL: GET /orders
   */
  @GetMapping
  public String showOrderList(Model model) {

    // 1.Serviceを通じてDBから全発注データを取得
    var orders = orderService.getAllOrders();

    // 2.取得したデータを"orders"という名前でModelに追加
    model.addAttribute("orders", orders);

    // 3.発注一覧画面のテンプレート名を返す
    return "list";
  }
}