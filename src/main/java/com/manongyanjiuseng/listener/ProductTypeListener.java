package com.manongyanjiuseng.listener;


import com.manongyanjiuseng.pojo.ProductType;
import com.manongyanjiuseng.service.ProductTypeService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.List;


@WebListener
public class ProductTypeListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        //取出所有的商品类型，便于在增加和更新的页面上使用，或者前端的类型查询中使用
        //切记切记：只能手工从spring的Bean工厂中按名称取出类型的service
        ApplicationContext context=new ClassPathXmlApplicationContext("applicationContext_*.xml");
        ProductTypeService productTypeService= (ProductTypeService) context.getBean("ProductTypeServiceImpl");
        List<ProductType> typeList=productTypeService.getAllType();
        //放入全局应用作用域中，共新增页面，修改页面，前台的查询功能提供全部商品的类别集合
        servletContextEvent.getServletContext().setAttribute("typeList",typeList);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {

    }
}