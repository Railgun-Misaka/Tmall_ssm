package com.tmall.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmall.pojo.Category;
import com.tmall.pojo.Property;
import com.tmall.service.CategoryService;
import com.tmall.service.PropertyService;

@Controller
@RequestMapping("")
public class PropertyController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_property_add")
    public String add(Property p) {
    	propertyService.add(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    @RequestMapping("admin_property_delete")
    public String delete(int id) {
    	Property p = propertyService.get(id);
    	propertyService.delete(id);
        return "redirect:admin_property_list?cid=" + p.getCid();
    }

    @RequestMapping("admin_property_edit")
    public String edit(Model model, int id) {
    	Property p = propertyService.get(id);
    	Category c = categoryService.get(p.getCid());
    	model.addAttribute("p", p);
    	model.addAttribute("c", c);
        return "admin/editProperty";
    }

    @RequestMapping("admin_property_update")
    public String update(Property p) {
    	propertyService.update(p);
        return "redirect:admin_property_list?cid="+p.getCid();
    }

    @RequestMapping("admin_property_list")
    public String list(int cid, Model model) {
    	Category category = categoryService.get(cid);
    	List<Property> propertys = propertyService.list(cid);
    	model.addAttribute("ps", propertys);
    	model.addAttribute("c", category);
        return "admin/listProperty";
    }
}
