package com.tmall.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmall.pojo.Order;
import com.tmall.service.OrderItemService;
import com.tmall.service.OrderService;
import com.tmall.util.Page;


@Controller
@RequestMapping("")
public class OrderController {
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;

    @RequestMapping("admin_order_list")
    public String list(Model model, Page page){
    	List<Order> os = orderService.list();
    	orderItemService.fill(os);
    	model.addAttribute("os", os);
        return "admin/listOrder";
    }

    @RequestMapping("admin_order_delivery")
    public String delivery(Order o) throws IOException {
        
        return "redirect:admin_order_list";
    }
}
