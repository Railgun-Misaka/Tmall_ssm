package com.tmall.service.impl;

import com.tmall.mapper.PropertyMapper;
import com.tmall.pojo.Category;
import com.tmall.pojo.Product;
import com.tmall.pojo.Property;
import com.tmall.pojo.PropertyExample;
import com.tmall.service.CategoryService;
import com.tmall.service.PropertyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@SuppressWarnings("all")
@Service
public class PropertyServiceImpl implements PropertyService {
    @Autowired
    PropertyMapper propertyMapper;

    @Override
    public void add(Property p) {
        propertyMapper.insert(p);
    }

    @Override
    public void delete(int id) {
        propertyMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void update(Property p) {
        propertyMapper.updateByPrimaryKeySelective(p);
    }

    @Override
    public Property get(int id) {
        return propertyMapper.selectByPrimaryKey(id);
    }

    @Override
    public List list(int cid) {
        PropertyExample example =new PropertyExample();
        example.createCriteria().andCidEqualTo(cid);
        example.setOrderByClause("id desc");
        return propertyMapper.selectByExample(example);
    }



}
