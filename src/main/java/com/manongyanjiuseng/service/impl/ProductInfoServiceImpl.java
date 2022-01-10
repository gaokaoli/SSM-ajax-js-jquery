package com.manongyanjiuseng.service.impl;

import com.manongyanjiuseng.mapper.ProductInfoMapper;
import com.manongyanjiuseng.pojo.ProductInfo;
import com.manongyanjiuseng.pojo.ProductInfoExample;
import com.manongyanjiuseng.pojo.vo.ProductVo;
import com.manongyanjiuseng.service.ProductInfoService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 张阿荣
 * @version 1.0
 * @title: ProductInfoServiceImpl
 * @projectName SSM_XiaoMi_5
 * @description:
 * @date 2019/11/29   9:56
 */
@Service
@Transactional
public class ProductInfoServiceImpl implements ProductInfoService {
    //切记:业务逻辑层中一定有数据访问层的对象
    @Autowired
    ProductInfoMapper productInfoMapper;

    @Override
    public List<ProductInfo> getAll() {
        return productInfoMapper.selectByExample(new ProductInfoExample());
    }

    @Override
    public PageInfo splitPage(int page, int pageSize) {
        //商品分页一定会借助于PageHelper类，还要借助于ProductInfoExample
        ProductInfoExample example=new ProductInfoExample();
        //设置的字符串是字段名称和排序规则
        example.setOrderByClause("p_id desc");
        //切记切记：在取集合之前，使用分页工具设置当前页和每页的记录数
        PageHelper.startPage(page,pageSize);
        //取集合
        List<ProductInfo> list=productInfoMapper.selectByExample(example);

        //将查到的集合封装进pageInfo
        PageInfo<ProductInfo> pageInfo=new PageInfo<>(list);

        return pageInfo;
    }

    @Override
    public int save(ProductInfo info) {
        return productInfoMapper.insert(info);
    }

    @Override
    public ProductInfo getById(Integer pid) {
        return productInfoMapper.selectByPrimaryKey(pid);
    }

    @Override
    public int update(ProductInfo info) {
        return productInfoMapper.updateByPrimaryKeySelective(info);
    }

    @Override
    public int delete(Integer pid) {
        int num=0;
        try {
            num = productInfoMapper.deleteByPrimaryKey(pid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return num;
    }

    @Override
    public int deleteBatch(String[] pids) {
        return productInfoMapper.deleteBatch(pids);
    }

    @Override
    public List<ProductInfo> selectCondition(ProductVo vo){
        return productInfoMapper.selectCondition(vo);

    }

    @Override
    public PageInfo<ProductInfo> splitPageVo(ProductVo vo, int pageSize) {
        //切记切记：在取集合之前，使用分页插件一定要先设置当前页和每页的个数
        PageHelper.startPage(vo.getPage(),pageSize);
        //取集合
        List<ProductInfo> list=productInfoMapper.selectCondition(vo);

        return new PageInfo<>(list);
    }
}