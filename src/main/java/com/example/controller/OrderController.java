package com.example.controller;

import com.example.Entity.Order;
import com.example.Service.OrderService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;

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

  /**
   * 新規発注作成画面を表示する
   * URL: GET /orders/new
   */
  @GetMapping("/new")
  public String showCreateForm(Model model) {

    model.addAttribute("orderForm", new Order());

    return "create";
  }

  /**
   * 新規発注作成処理を行う
   * URL: POST /create
   */
  @PostMapping("/create")
  public String create(@Validated @ModelAttribute("orderForm") Order order, BindingResult bindingResult,
      RedirectAttributes redirectAttributes) {

    if (bindingResult.hasErrors()) {
      return "create";
    }
    orderService.createOrder(order);

    redirectAttributes.addFlashAttribute("message", "発注リクエストを登録しました。");
    return "redirect:/orders";
  }
}