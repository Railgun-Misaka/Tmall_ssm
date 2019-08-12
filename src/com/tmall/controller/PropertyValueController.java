package com.tmall.controller;

import com.tmall.pojo.Property;
import com.tmall.pojo.Product;
import com.tmall.pojo.PropertyValue;
import com.tmall.service.ProductService;
import com.tmall.service.PropertyService;
import com.tmall.service.PropertyValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("")
public class PropertyValueController {
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    ProductService productService;
    @Autowired
    PropertyService propertyService;

    @RequestMapping("admin_propertyValue_edit")
    public String edit(Model model,int pid) {
        Product p = productService.get(pid);
        List<PropertyValue> pvs = propertyValueService.list(pid);
        for(PropertyValue pv : pvs) {
        	Property pt = propertyService.get(pv.getPtid());
        	pv.setProperty(pt);
        }
        model.addAttribute("p", p);
        model.addAttribute("pvs", pvs);
        return "admin/editPropertyValue";
    }
    @RequestMapping("admin_propertyValue_update")
    @ResponseBody
    public String update(PropertyValue pv) {
        //propertyValueService.update(pv);
        return "success";
    }
}

