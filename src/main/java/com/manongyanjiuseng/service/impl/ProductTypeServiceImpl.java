package com.manongyanjiuseng.service.impl;


import com.manongyanjiuseng.mapper.ProductTypeMapper;
import com.manongyanjiuseng.pojo.ProductType;
import com.manongyanjiuseng.pojo.ProductTypeExample;
import com.manongyanjiuseng.service.ProductTypeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("ProductTypeServiceImpl")
public class ProductTypeServiceImpl implements ProductTypeService {
    //必有数据访问层的mapper对象

    @Autowired
    private ProductTypeMapper typeMapper;

    @Override
    public List<ProductType> getAllType() {
        return typeMapper.selectByExample(new ProductTypeExample());
    }
}
