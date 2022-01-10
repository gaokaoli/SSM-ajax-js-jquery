package com.manongyanjiuseng.service;

import com.manongyanjiuseng.pojo.ProductInfo;
import com.manongyanjiuseng.pojo.vo.ProductVo;
import com.github.pagehelper.PageInfo;

import java.util.List;

/**
 * @author 张阿荣
 * @version 1.0
 * @title: ProductInfoService
 * @projectName SSM_XiaoMi_5
 * @description:
 * @date 2019/11/29   9:49
 */
public interface ProductInfoService {
    //显示全部商品(不分页)
    List<ProductInfo> getAll();

    //分页
    public PageInfo splitPage(int page, int pageSize);

    //增加
    public int save(ProductInfo info);

    //根据主键查商品
    public ProductInfo getById(Integer pid);

    //修改
    public int update(ProductInfo info);

    //删除
    public int delete(Integer pid);

    //批量删除
    public int deleteBatch(String[] pids);

    //多条件分页查询
    public  List <ProductInfo> selectCondition(ProductVo vo);

    //多条件分页查询
    public PageInfo<ProductInfo> splitPageVo(ProductVo vo, int pageSize);
}
