package com.tmall.interceptor;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.tmall.pojo.OrderItem;
import com.tmall.pojo.User;
import com.tmall.service.OrderItemService;



public class TmallInterceptor extends HandlerInterceptorAdapter {  
	
	@Autowired
    OrderItemService orderItemService;
	
    @Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
         
    	String[] noNeedAuthPage = new String[]{
                "home",
                "checkLogin",
                "register",
                "loginAjax",
                "login",
                "product",
                "category",
                "search",
                "readyCart"};
    	String servletpath = request.getServletPath();
    	String checkitem = servletpath.replaceFirst("/fore", "");
    	for(String s : noNeedAuthPage)
    		if(s.equals(checkitem))
    	        return true;
    	HttpSession session = request.getSession();
    	if(session.getAttribute("user") == null) {
    		response.sendRedirect("loginPage");
    		return false;
    	}
    	return true;
    }  
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {  
    	User user = (User) request.getSession().getAttribute("user");
    	int cartTotalItemNumber = 0;
    	if(user != null) {
    	List<OrderItem> ois = orderItemService.listByUser(user.getId());
		for (OrderItem oi : ois) 
    		if(oi.getOid() == null) {	
    			++ cartTotalItemNumber ;
    		}
    	}
		request.setAttribute("cartTotalItemNumber", cartTotalItemNumber);
    }  
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)  throws Exception {    
    	ex.printStackTrace();
    }  
}
