package com.tmall.controller;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmall.pojo.Category;
import com.tmall.pojo.Product;
import com.tmall.service.CategoryService;
import com.tmall.service.ProductService;
import com.tmall.util.Page;

@Controller
@RequestMapping("")
public class ProductController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;

    @RequestMapping("admin_product_add")
    public String add(Model model, Product p) {
        productService.add(p);
        return "redirect:admin_product_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_product_delete")
    public String delete(int id) {
    	int cid = productService.get(id).getCid();
        productService.delete(id);
        return "redirect:admin_product_list?cid=" + cid;
    }

    @RequestMapping("admin_product_edit")
    public String edit(Model model, int id) {
    	Product p = productService.get(id);
    	p.setCategory(categoryService.get(p.getCid()));
        model.addAttribute("p", p);
        return "admin/editProduct";
    }

    @RequestMapping("admin_product_update")
    public String update(Product p) {
        productService.update(p);
        return "redirect:admin_product_list?cid="+p.getCid();
    }

    @RequestMapping("admin_product_list")
    public String list(int cid, Model model, Page page) {
        List<Product> ps = productService.list(cid);
        Category c = categoryService.get(cid);
        model.addAttribute("c", c);
        model.addAttribute("ps", ps);
        return "admin/listProduct";
    }
}
