package com.tmall.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.tmall.pojo.User;
import com.tmall.service.UserService;


@Controller
@RequestMapping("")
public class UserController {
    @Autowired
    UserService userService;
 
    @RequestMapping("admin_user_list")
    public ModelAndView handleRequest(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("admin/listuser");
		List<User> users = userService.list();
        mav.addObject("users", users);
        return mav;
    }
}