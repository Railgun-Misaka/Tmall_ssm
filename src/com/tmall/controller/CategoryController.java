package com.tmall.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.tmall.pojo.Category;
import com.tmall.service.CategoryService;
import com.tmall.util.*;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

@SuppressWarnings("all")
@Controller
@RequestMapping("")
public class CategoryController {
    @Autowired
    CategoryService categoryService;
  
    @RequestMapping("admin_category_list")
    public String list(Model model, Page page) throws Exception {
    	if(page == null) {
    		page = new Page();
    		page.setStart(0);
    	}
		PageHelper.offsetPage(page.getStart(), page.getCount());
		List<Category> categorys = categoryService.list();
		int total = (int) new PageInfo<>(categorys).getTotal();
		page.setTotal(total);
        model.addAttribute("cs", categorys);
        model.addAttribute("page", page);
        return "admin/listCategory";
    }

    @RequestMapping("admin_category_add")
    public String add(HttpSession session, HttpServletRequest request) throws IOException {
    	SaveAndUpload sau = new SaveAndUpload(request);
    	sau.save(categoryService);
    	sau.upload();
        return "redirect:admin_category_list";
    }
    
    @RequestMapping("admin_category_delete")
    public ModelAndView delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		ModelAndView mav = new ModelAndView("redirect:admin_category_list");
		int id = Integer.parseInt(request.getParameter("id"));
		categoryService.delete(id);
		File f = new File(request.getServletContext().getRealPath("img/category/") + id + ".jpg");
		if(f.exists())
			f.delete();
		return mav;
    }

    @RequestMapping("admin_category_edit")
    public String edit(int id,Model model) throws IOException {
    	model.addAttribute("c", categoryService.get(id));
        return "admin/editCategory";
    }

    @RequestMapping("admin_category_update")
    public String update(Category c, HttpSession session, HttpServletRequest request) throws IOException {
    	categoryService.update(c);
    	SaveAndUpload sau = new SaveAndUpload(request);
    	sau.setfilename(c.getId());
    	sau.upload();
        return "redirect:/admin_category_list";
    }

}

