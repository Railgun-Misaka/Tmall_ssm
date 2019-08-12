package com.tmall.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.tmall.pojo.Product;
import com.tmall.pojo.ProductImage;
import com.tmall.service.ProductImageService;
import com.tmall.service.ProductService;
import com.tmall.util.SaveAndUpload;


@Controller
@RequestMapping("")
public class ProductImageController {
    @Autowired
    ProductService productService;

    @Autowired
    ProductImageService productImageService;


    @RequestMapping("admin_productImage_add")
    public String add(HttpServletRequest request, HttpSession session, String id) {
    	int pid = Integer.parseInt(id);
    	SaveAndUpload sau = new SaveAndUpload(request);
    	sau.save(productImageService);
    	sau.upload();
    	return "redirect:admin_productImage_list?pid=" + pid;
    }
    @RequestMapping("admin_productImage_delete")
    public String delete(int id,HttpSession session) {
    	ProductImage pi = productImageService.get(id);
    	String type = pi.getType();
    	int pid = pi.getPid();
    	productImageService.delete(id);
    	String imgname = id + ".jpg";
    	if(type.equals("type_single")) {
	    	File f1 = new File(session.getServletContext().getRealPath("img/productSingle"), imgname);
	    	File f2 = new File(session.getServletContext().getRealPath("img/productSingle_middle"), imgname);
	    	File f3 = new File(session.getServletContext().getRealPath("img/productSingle_small"), imgname);
	    	if(f1.exists())
	    		f1.delete();
	    	if(f2.exists())
	    		f2.delete();
	    	if(f3.exists())
	    		f3.delete();
    	}else {
    		File f = new File(session.getServletContext().getRealPath("img/productDetail"), imgname);
    		if(f.exists())
	    		f.delete();
    	}
        return "redirect:admin_productImage_list?pid=" + pid;
    }

    @RequestMapping("admin_productImage_list")
    public String list(int pid, Model model) {
    	Product p =productService.get(pid);
        List<ProductImage> pisSingle = productImageService.list(pid, ProductImageService.type_single);
        List<ProductImage> pisDetail = productImageService.list(pid, ProductImageService.type_detail);


        model.addAttribute("p", p);
        model.addAttribute("pisSingle", pisSingle);
        model.addAttribute("pisDetail", pisDetail);
        return "admin/listProductImage";
    }
}
