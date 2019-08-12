package com.tmall.test;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.tmall.pojo.*;

import com.tmall.mapper.*;

import java.io.InputStream;
import java.util.List;



@SuppressWarnings("all")
public class testMybatis {

	public static void main(String[] args) throws Exception {
		ApplicationContext context = new ClassPathXmlApplicationContext(new String[] { "applicationContext.xml"});
		SqlSessionFactory sqlSessionFactory =  (SqlSessionFactory) context.getBean("sqlSession");
		SqlSession session = sqlSessionFactory.openSession();
		
        CategoryExample example = new CategoryExample();
        example.createCriteria().andNameLike("%%");
        CategoryMapper mapper = session.getMapper(CategoryMapper.class);
        List<Category> cs= mapper.selectByExample(example);
 
        for (Category c : cs) {
            System.out.println(c.getName());
        }

	}

}
