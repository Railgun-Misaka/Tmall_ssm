package com.tmall.controller;

import com.tmall.pojo.*;
import com.tmall.service.*;

import comparator.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("")
public class ForeController {
    @Autowired
    CategoryService categoryService;
    @Autowired
    ProductService productService;
    @Autowired
    UserService userService;
    @Autowired
    ProductImageService productImageService;
    @Autowired
    PropertyService propertyService;
    @Autowired
    PropertyValueService propertyValueService;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderItemService orderItemService;
    @Autowired
    ReviewService reviewService;
    @RequestMapping("forehome")
    public String home(Model model, HttpSession session) {
    	List<Category> cs = categoryService.list();
    	productService.fill(cs);
    	productService.fillByRow(cs);
    	model.addAttribute("cs", cs);
        return "fore/home";
    }

    @RequestMapping("foreregister")
    public String register(Model model,@RequestParam("name") String name, @RequestParam("password") String password) {
    	User user = new User();
    	user.setName(name); user.setPassword(password);
    	try {
    		userService.add(user);
    	}catch(Exception e) {
    		String msg = "用户名已被注册！";
            model.addAttribute("msg", msg);
    		return "fore/login";
    	}
        return "redirect:registerSuccessPage";
    }
    @RequestMapping("forelogin")
    public String login(@RequestParam("name") String name, @RequestParam("password") String password, Model model, HttpSession session) {
        User u = userService.get(name, password);
        if(u != null) {
    		session.setAttribute("user", u);
    		session.setMaxInactiveInterval(6000);
    		return "redirect:forehome";
        }
        String msg = "用户名或密码错误！请重新输入";
        model.addAttribute("msg", msg);
		return "fore/login";
    }
    @RequestMapping("forelogout")
    public String logout( HttpSession session) {
        session.removeAttribute("user");
        return "redirect:forehome";
    }

    @RequestMapping("foreproduct")
    public String product( int pid, Model model) {
    	Product p = productService.get(pid);
    	p.setProductSingleImages(productImageService.list(pid, ProductImageService.type_single));
    	p.setProductDetailImages(productImageService.list(pid, ProductImageService.type_detail));
    	productService.setFirstProductImage(p);
    	p.setReviewCount(reviewService.getCount(pid));
    	p.setSaleCount(orderItemService.getSaleCount(pid));
    	model.addAttribute("p", p);
    	
    	List<PropertyValue> pvs = propertyValueService.list(pid);
    	for(PropertyValue pv : pvs) {
    		Property pt = propertyService.get(pv.getPtid());
    		pv.setProperty(pt);
    	}
    	model.addAttribute("pvs", pvs);
    	
    	List<Review> reviews = reviewService.list(pid);
    	for(Review r : reviews) {
    		User u = userService.get(r.getUid());
    		r.setUser(u);
    	}
    	model.addAttribute("reviews", reviews);
        return "fore/product";
    }

    @RequestMapping("forecheckLogin")
    @ResponseBody
    public String checkLogin( HttpSession session) {
        if(session.getAttribute("user") == null)
        	return "fail";
        return "success";
    }
    @RequestMapping("foreloginAjax")
    @ResponseBody
    public String loginAjax(@RequestParam("name") String name, @RequestParam("password") String password,HttpSession session) {
    	List<User> users = userService.list();
    	for(User user : users)
    		if(user.getName().equals(name) && user.getPassword().equals(password)) {
    			session.setAttribute("user", user);
    			return "success";
    		}
        return "false";
    }
    @RequestMapping("forecategory")
    public String category(int cid,String sort, Model model) {
        
    	Category c = categoryService.get(cid);
    	List<Product> ps = productService.list(cid);
    	if(sort != null) {
    		switch(sort) {
    			case "price":
    				ProductPriceComparator pc = new ProductPriceComparator();
    				ps.sort(pc);
    				break;
    		}
    	}
    	for(Product p : ps) {
    		int pid = p.getId();
        	productService.setFirstProductImage(p);
        	p.setReviewCount(reviewService.getCount(pid));
        	p.setSaleCount(orderItemService.getSaleCount(pid));
    	}
    	c.setProducts(ps);
    	model.addAttribute("c", c);
        return "fore/category";
    }

    @RequestMapping("foresearch")
    public String search( String keyword,Model model){
    	List<Product> ps = productService.search(keyword);
    	for(Product p : ps) {
    		int pid = p.getId();
        	productService.setFirstProductImage(p);
        	p.setReviewCount(reviewService.getCount(pid));
        	p.setSaleCount(orderItemService.getSaleCount(pid));
    	}
    	model.addAttribute("ps", ps);
        return "fore/searchResult";
    }
    @RequestMapping("forebuyone")
    public String buyone(int pid, int num, HttpSession session) {
	    OrderItem oi = new OrderItem();	
    	oi.setPid(pid);
    	oi.setNumber(num);
    	oi.setUid(((User)session.getAttribute("user")).getId());
    	orderItemService.add(oi);
        return "redirect:forebuy?oiid=" + oi.getId();
    }

    @RequestMapping("forebuy")
    public String buy( Model model,String[] oiids, String oiid){
    	if(oiids == null)
    		oiids = oiid.split(",");
    	List<OrderItem> ois = new ArrayList<OrderItem>();
    	float total = 0;
    	for(String oiid1 : oiids) {
	    	OrderItem oi = orderItemService.get(Integer.parseInt(oiid1));
	    	Product p = productService.get(oi.getPid());
	    	oi.setProduct(p);
	    	ois.add(oi);
	    	total += p.getPromotePrice() * oi.getNumber();
    	}
    	model.addAttribute("total", total);
    	model.addAttribute("ois", ois);
        return "fore/buy";
    }
    @RequestMapping("foreaddCart")
    @ResponseBody
    public String addCart(int pid, int num, Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null)
        	return "false" ;
        int uid = user.getId();
        List<OrderItem> ois = orderItemService.listByUser(uid);
        for(OrderItem orderitem : ois) 
        	if(orderitem.getPid() == pid)
        		return "success";
        OrderItem oi = new OrderItem();
        oi.setUid(uid);
        oi.setPid(pid);
        oi.setNumber(num);
        orderItemService.add(oi);
        return "success";
    }
    @RequestMapping("forecart")
    public String cart( Model model,HttpSession session) {
    	User user = (User) session.getAttribute("user");
    	List<OrderItem> ois = orderItemService.listByUser(user.getId());
    	Product p ;
    	for(OrderItem oi : ois) {
    		if(oi.getOid() == null){
    			p = productService.get(oi.getPid());
    			productService.setFirstProductImage(p);
    			productService.setSaleAndReviewNumber(p);
    			oi.setProduct(p);
    		}else {
    			ois.remove(oi);
    		}
    	}
    	model.addAttribute("ois", ois);
        return "fore/cart";
    }

    @RequestMapping("forechangeOrderItem")
    @ResponseBody
    public String changeOrderItem( Model model,HttpSession session, int pid, int number) {
        
        return "success";
    }
    @RequestMapping("foredeleteOrderItem")
    @ResponseBody
    public String deleteOrderItem( Model model,HttpSession session,int oiid){
    	if(session.getAttribute("user") == null)
    		return "false";
    	orderItemService.delete(oiid);
        return "success";
    }

    @RequestMapping("forecreateOrder")
    public String createOrder( Model model,Order order, HttpSession session){
    	List<OrderItem> ois = (List) session.getAttribute("ois");
    	Date date = new Date();
    	order.setCreateDate(date);
    	order.setUid(((User)session.getAttribute("user")).getId());
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
    	String ordercode = sdf.format(date);
    	order.setOrderCode(ordercode);
    	order.setStatus("waitPay");
    	orderService.add(order);
        int oid = order.getId();
        float total = 0;
        for(OrderItem oi : ois) {
        	oi.setOid(oid);
        	orderItemService.update(oi);
        	total += oi.getProduct().getPromotePrice() * oi.getNumber();
        }
        session.removeAttribute("ois");
        order.setTotal(total);
        model.addAttribute("order", order);
        return "fore/alipay";
    }


    @RequestMapping("forepayed")
    public String payed(HttpSession session, Model model) {
        Order order = (Order) session.getAttribute("order");
        session.removeAttribute("order");
        order.setPayDate(new Date());
        order.setStatus("waitDelivery");
        orderService.update(order);
        model.addAttribute("order", order);
        return "fore/payed";
    }

    @RequestMapping("forebought")
    public String bought( Model model,HttpSession session) {
    	try {
    		User user = (User) session.getAttribute("user");
	        int uid = user.getId();
	        List<Order> os = orderService.list(uid, "delete");
	        orderItemService.fill(os);
	        model.addAttribute("os", os);
	        return "fore/bought";
    	}catch(Exception e) {
    		return "fore/login";
    	}
    }

    @RequestMapping("foreconfirmPay")
    public String confirmPay( Model model,int oid) {
    	Order order = orderService.get(oid);
    	orderItemService.fill(order);
    	model.addAttribute("o", order);
        return "fore/confirmPay";
    }
    @RequestMapping("foreorderConfirmed")
    public String orderConfirmed( Model model,int oid) {
        Order order = orderService.get(oid);
        order.setConfirmDate(new Date());
        order.setStatus("waitReview");
        orderService.update(order);
        return "fore/orderConfirmed";
    }
    @RequestMapping("foredeleteOrder")
    @ResponseBody
    public String deleteOrder( Model model,int oid){
    	try{
	        Order o = orderService.get(oid);
	        o.setStatus("delete");
	        orderService.update(o);
	        return "success";
    	}catch(Exception e){
    		return "false";
    	}
    }
    @RequestMapping("forereview")
    public String review( Model model,int oid, String showonly) {
    	Order o = orderService.get(oid);
    	orderItemService.fill(o);
    	List<OrderItem> ois = o.getOrderItems();
    	Product p = ois.get(0).getProduct();
    	if(showonly == null)
    		showonly = "false";
    	if(showonly.equals("true")){
    		List<Review> reviews = reviewService.list(p.getId());
    		p.setReviewCount(reviews.size());
    		model.addAttribute("reviews", reviews);
    	}
    	model.addAttribute("p", p);
    	model.addAttribute("o", o);
        return "fore/review";
    }
    @RequestMapping("foredoreview")
    public String doreview( Model model,HttpSession session,@RequestParam("oid") int oid,@RequestParam("pid") int pid,String content) {
    	Review r = new Review();
    	r.setPid(pid);
    	r.setUid(((User)session.getAttribute("user")).getId());
    	r.setContent(content);
    	r.setCreateDate(new Date());
    	reviewService.add(r);
    	Order order = orderService.get(oid);
    	order.setStatus("finish");
    	orderService.update(order);
        return "redirect:forereview?oid="+oid+"&showonly=true";
    }
    @RequestMapping("registerAjax")
    @ResponseBody
    public String checknameexist(@RequestParam("name") String name, Model model) {
    	for(User u : userService.list())
    		if(u.getName().equals(name)) 
    			return "fales";
        return "success";
    }
    @RequestMapping("urge")
    @ResponseBody
    public String urgesend(String id) {
    	Order order = orderService.get(Integer.parseInt(id));
    	order.setDeliveryDate(new Date());
    	order.setStatus("waitConfirm");
    	orderService.update(order);
    	return "success";
    }
    @RequestMapping("forereadyCart")
    @ResponseBody
    public String readyCart(int pid, int num, Model model,HttpSession session) {
        User user = (User) session.getAttribute("user");
        if(user == null)
        	return "false" ;
        int uid = user.getId();
        List<OrderItem> ois = orderItemService.listByUser(uid);
        for(OrderItem orderitem : ois) 
        	if(orderitem.getPid() == pid)
        		return "success";
        return "false";
    }
}
