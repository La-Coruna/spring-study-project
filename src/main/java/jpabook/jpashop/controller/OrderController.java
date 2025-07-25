package jpabook.jpashop.controller;

import jpabook.jpashop.domain.Item;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderSearch;
import jpabook.jpashop.service.ItemService;
import jpabook.jpashop.service.MemberService;
import jpabook.jpashop.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class OrderController {
    private final ItemService itemService;
    private final MemberService memberService;
    private final OrderService orderService;

    @GetMapping("/orders")
    public String list(@ModelAttribute("orderSearch") OrderSearch orderSearch, Model model){
        List<Order> orders = orderService.findOrders(orderSearch);
        model.addAttribute("orders",orders);
        return "/order/orderList";
    }

    @GetMapping("/order")
    public String createOrderForm(Model model){
        List<Member> members = memberService.findMembers();
        List<Item> items = itemService.findItems();

        model.addAttribute("members", members);
        model.addAttribute("items", items);

        return "/order/createOrderForm";
    }

    @PostMapping("/order")
    public String order(@RequestParam("memberId") Long memberId, @RequestParam("itemId") Long itemId, @RequestParam("count") int count){
        orderService.order(memberId, itemId, count);
        return "redirect:/orders";
    }

    @PostMapping("/orders/{itemId}/cancel")
    public String cancel(@PathVariable("itemId") Long itemId){
        orderService.cancelOrder(itemId);
        return "redirect:/orders";
    }
}
