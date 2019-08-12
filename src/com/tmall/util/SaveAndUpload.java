package com.tmall.util;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import com.tmall.pojo.Category;
import com.tmall.pojo.ProductImage;
import com.tmall.service.CategoryService;
import com.tmall.service.ProductImageService;

public class SaveAndUpload {
	
	private DiskFileItemFactory factory = null;
	private ServletFileUpload upload = null;
	private String filename = null;
	private List<FileItem> items = null;
	private Iterator<FileItem> iter = null;
	private HttpServletRequest request = null ;
	private String type = null;
	private int pid = -1;
	private List<String> photoFolders = null;
	public SaveAndUpload(HttpServletRequest request) {
		this.request = request;
		factory = new DiskFileItemFactory();
        upload = new ServletFileUpload(factory);
        photoFolders = new ArrayList();
        try {
            this.items = upload.parseRequest(request);
        } catch (FileUploadException e) {
            e.printStackTrace();
        }
	}
	public void save(CategoryService categoryService) {
        iter = items.iterator();
		while(iter.hasNext()) {
        	FileItem item = iter.next();
        	
        	if(item.isFormField()) {
        		try {
                    String name = item.getString();
                    name = new String(name.getBytes("ISO-8859-1"), "utf-8");
                    Category c = new Category();
                    c.setName(name);
                    categoryService.add(c);
                    filename = c.getId() + ".jpg";
                } catch (Exception e) {
                    e.printStackTrace();
                }
        	}
        }
	} 
	
	public void save(ProductImageService productImageService) {
        iter = items.iterator();
		while(iter.hasNext()) {
        	FileItem item = iter.next();
        	
        	if(item.isFormField()) {
        		try {
        			switch(item.getFieldName()){
	        	        case "type":
	        	        	type = item.getString();
	        	        	type = new String(type.getBytes("ISO-8859-1"), "utf-8");
	        	            break;
	        	        case "pid":
	        	        	String id = item.getString();
	        	        	pid = Integer.parseInt(id);
	        	            break;
	        	        default:
	        	            return ;
	        	    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        	}
        	if(pid != -1 && type != null) {
	            ProductImage pi = new ProductImage();
	            pi.setPid(pid);
	            pi.setType(type);
	            productImageService.add(pi);
	            filename = pi.getId() + ".jpg";
        	}
        }
	}
	
	public void upload() {
		if(filename == null)
			return ;
		iter = items.iterator();
		while(iter.hasNext()) {
        	FileItem item = iter.next();
        	
			if(!item.isFormField()) {
				if(pid != -1 && type.equals("type_single")) {
					photoFolders.add(request.getServletContext().getRealPath("img/productSingle"));
					photoFolders.add(request.getServletContext().getRealPath("img/productSingle_middle"));
					photoFolders.add(request.getServletContext().getRealPath("img/productSingle_small"));
				}else if(pid != -1 && type.equals("type_detail")) {
					photoFolders.add(request.getServletContext().getRealPath("img/productDetail"));
				}else {
					photoFolders.add(request.getServletContext().getRealPath("img/category"));
				}
	    		for(String photoFolder : photoFolders) {
		    		File f = new File(photoFolder, filename);
		    		if(f.exists())
		    			f.delete();
		        	try(InputStream is = item.getInputStream();
		        		FileOutputStream fos = new FileOutputStream(f);){
		        		switch(f.getParentFile().getName()) {
			        		case "productSingle":
			        			changesize(800, 800, is, fos);
			        			break;
			        		case "productSingle_middle":
			        			changesize(400, 400, is, fos);
			        			break;
			        		case "productSingle_small":
			        			changesize(56, 56, is, fos);
			        			break;
			        		case "category":
			        			changesize(1.0f, 1.0f, is, fos);
			        			break;
			        		case "productDetail":
			        			changesize(null, 790, is, fos);
			        			break;
		        		}
		        	}catch(Exception e){
		        		e.printStackTrace();
		        	}
	    		}
	    	}
		}
	}
	
	public void setfilename(int id) {
		this.filename = id + ".jpg";
	}
	
	private void changesize(int width, int height, InputStream is, FileOutputStream fos) throws IOException {
		Image img =ImageIO.read(is);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img, 0, 0,width, height, null); 
		ImageIO.write(tag, "JPG", fos);
		return;
	}
	
	private void changesize(float wid, float hei, InputStream is, FileOutputStream fos) throws IOException {
		Image img =ImageIO.read(is);
		int width = (int) (img.getWidth(null) * wid);
		int height = (int) (img.getHeight(null) * hei);
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img, 0, 0,width, height, null); 
		ImageIO.write(tag, "JPG", fos);
		return;
	}
	
	private void changesize(int width, Object o, InputStream is, FileOutputStream fos) throws IOException {
		Image img =ImageIO.read(is);
		int height = (int) (img.getHeight(null) * width / img.getWidth(null));
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img, 0, 0,width, height, null); 
		ImageIO.write(tag, "JPG", fos);
		return;
	}
	
	private void changesize(Object o, int height, InputStream is, FileOutputStream fos) throws IOException {
		Image img =ImageIO.read(is);
		int width = (int) (img.getWidth(null) * height / img.getHeight(null));
		BufferedImage tag = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		tag.getGraphics().drawImage(img, 0, 0,width, height, null); 
		ImageIO.write(tag, "JPG", fos);
		return;
	}
}