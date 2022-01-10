package com.text2;

import com.manongyanjiuseng.mapper.ProductInfoMapper;
import com.manongyanjiuseng.pojo.ProductInfo;
import  com.manongyanjiuseng.pojo.vo.ProductVo;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:applicationContext_dao.xml","classpath:applicationContext_service.xml"})
public class Mytest2 {

    @Autowired
    ProductInfoMapper productInfoMapper;

    @Test
    public  void  testSelectCondition(){
        ProductVo vo =new ProductVo();
        List<ProductInfo> list =productInfoMapper.selectCondition(vo);
        list.forEach(ProductInfo -> {
            System.out.println(ProductInfo);
        });
    }

}
