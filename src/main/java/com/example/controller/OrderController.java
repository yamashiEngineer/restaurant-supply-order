package com.example.controller;

import com.example.Entity.Order;
import com.example.Service.OrderService;
import org.springframework.ui.Model;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.data.domain.Page;

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
  public String showList(Model model,
      @RequestParam(value = "page", defaultValue = "0") int page) {
    int pageSize = 10;
    Page<Order> orderPage = orderService.findPaginatedOrders(page, pageSize);

    // 表示するページ番号の範囲を計算（スマートな表示用）
    int totalPages = orderPage.getTotalPages();
    int startPage = Math.max(0, page - 2); // 現在の2つ前、ただし0未満にはしない
    int endPage = Math.min(totalPages - 1, page + 2); // 現在の2つ後、ただし最大数を超えない

    model.addAttribute("orderPage", orderPage);
    model.addAttribute("currentPage", page);
    model.addAttribute("startPage", startPage);
    model.addAttribute("endPage", endPage);

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

  /**
   * 発注詳細画面を表示する
   */
  @GetMapping("/edit/{id}")
  public String showDetail(@PathVariable("id") Integer id, Model model) {
    // Serviceから1件取得
    Order order = orderService.findOrderById(id);
    model.addAttribute("order", order);
    return "edit";
  }
}