package com.manongyanjiuseng.controller;

import com.manongyanjiuseng.pojo.ProductInfo;
import com.manongyanjiuseng.pojo.vo.ProductVo;
import com.manongyanjiuseng.service.ProductInfoService;
import com.manongyanjiuseng.utils.*;

import com.github.pagehelper.PageInfo;

//import com.sun.org.apache.xpath.internal.operations.Mod;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.util.Date;
import java.util.List;


@Controller

@RequestMapping("/prod")
public class ProductInfoController {

    public static final int PAGE_SIZE = 5;

    public String saveFileName = "";//异步上传存文件名，增加和更新用这个文件名

    @Autowired
    ProductInfoService productInfoService;

    //显示全部商品不分页
    @RequestMapping("/getAll")
    public String getAll(Model model) {
        List<ProductInfo> list = productInfoService.getAll();
        model.addAttribute("list", list);
        return "product";
    }

    //显示第一页到第五页
    //分页显示
/*    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo info = productInfoService.splitPage(1, PAGE_SIZE);
        request.setAttribute("info", info);

        return "product";
    }*/

    //分页显示
    @ResponseBody
    @RequestMapping("/ajaxSplit")
    public void ajaxSplit(int page, HttpSession session) {
        PageInfo info = productInfoService.splitPage(page, PAGE_SIZE);
        session.setAttribute("info", info);

    }

    //带有页面信息的分页显示（适合于多条件查询）
    //分页显示
    @RequestMapping("/split")
    public String split(HttpServletRequest request) {
        PageInfo info =null;
        Object vo= request.getSession().getAttribute("prodvo");
        if(vo!=null){
            info=productInfoService.splitPageVo((ProductVo)vo,PAGE_SIZE);
            request.getSession().removeAttribute("prodvo");//用完之后清理掉
        }else{
             info = productInfoService.splitPage(1, PAGE_SIZE);
        }

        request.setAttribute("info", info);
        return "product";

    }

    @ResponseBody
    @RequestMapping("/ajaxImg")
    public Object ajaxImg(MultipartFile pimage,HttpServletRequest request){
        //要进行文件上传操作
        //异步上传存文件名，增加和更新用这个文件名
        //取文件名
        saveFileName  = FileNameUtil.getUUIDFileName() + FileNameUtil.getFileType(pimage.getOriginalFilename());
        //取路径
        try {
            String path = request.getServletContext().getRealPath("/image_big");
            //转存
            pimage.transferTo(new File(path + File.separator + saveFileName));
        } catch (Exception e) {
            e.printStackTrace();
        }

        //为了在客户端显示图片，要将存储的文件名回传下去，由于是自定义的上传插件，所以此处要手工处理JSON
        JSONObject object = new JSONObject();
        object.put("imgurl",saveFileName);
        //切记切记：JSON对象一定要toString()回到客户端
        return object.toString();
    }

    //执行增加操作
    @RequestMapping("/save")
    public String save(ProductInfo info, HttpServletRequest request) {

        //图片处理好后，设置到商品对象中
        info.setpImage(saveFileName);
        info.setpDate(new Date());

        //到此为止，商品对象构建完毕，有自动从表单元素注入的，有上传图片的，有上架日期
        //完成数据库增加操作
        int num = -1;
        try {
            num = productInfoService.save(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0)
            request.getSession().setAttribute("msg", "增加成功！");
        else
            request.getSession().setAttribute("msg", "增加失败!");

        saveFileName="";
        //增删改后用重定向跳转
        return "redirect:/prod/split.action";
    }

/*    //根据id查询回显显示商品信息
    @RequestMapping("/one")
    public String one(Integer pid,Integer page, Model model) {
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod", info);
        model.addAttribute("page",page);
        return "update";
    }*/

    //根据id查询回显显示商品信息
    @RequestMapping("/one")
    public String one(Integer pid,ProductVo vo, Model model,HttpSession session) {
        ProductInfo info = productInfoService.getById(pid);
        model.addAttribute("prod", info);
        //将多条件及页码放入session中，更新处理结束后分页时读取条件和页码处理
        session.setAttribute("prodvo",vo);
        return "update";
    }

    //更新操作
    @RequestMapping("/update")
    public String update(ProductInfo info,HttpServletRequest request) {

        //因为是修改，所以要判断有没有点文件上传的铵钮
        if(!saveFileName.equals("")){
            info.setpImage(saveFileName);
        }

        boolean flag = false;
        //有没有时间改变
        //此处要进行info对象的上架时间的更新
        //首先要判断当前新更新上来的日期不能大于当前日期
        if (info.getpDate() != null) {
            if (info.getpDate().getTime() > System.currentTimeMillis()) {
                //日期不正常了，则置为空，底层做更新处理时不做更改
                info.setpDate(null);
                flag=true;
            }
        }
        //完成对象更新
        int num = -1;
        try {
            num = productInfoService.update(info);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (num > 0) {
            if (flag) {
                request.getSession().setAttribute("msg", "日期不能大于当前日期");
            } else {
                request.getSession().setAttribute("msg", "更新成功！");
            }
        } else {
            request.getSession().setAttribute("msg", "更新失败");
        }
        saveFileName="";
        return "redirect:/prod/split.action";
    }

/*    //执行删除操作
    @RequestMapping("/delete")
    public String delete(Integer pid, HttpSession session) {
        int num = productInfoService.delete(pid);
        if (num > 0)
            session.setAttribute("msg", "删除成功！");
        else
            session.setAttribute("msg", "删除失败！");

        //增删改后用重定向跳转
        return "forward:/prod/dajaxsplit.action";
    }*/

    //执行删除操作
    @RequestMapping("/delete")
    public String delete(Integer pid,ProductVo vo, HttpSession session) {
        int num=-1;
        try{
            num = productInfoService.delete(pid);
        }catch(Exception e){
            e.printStackTrace();
        }

        if (num > 0) {
            session.setAttribute("msg", "删除成功！");
            session.setAttribute("deleteProdVo", vo);
        }
        else{
            session.setAttribute("msg", "删除失败！");
        }


        //增删改后用重定向跳转
        return "forward:/prod/dajaxsplit.action";
    }


/*    //删除后分页显示,切记切记切记：坑：使用@ResponseBody注解，返回值不能是String,
    // 如果一定要使用String,则手工封装成JSON格式
    @ResponseBody
    @RequestMapping(value = "/dajaxsplit",produces = "text/html;charset=UTF-8")
    public Object dajaxsplit(ProductInfo info, HttpSession session) {

        session.setAttribute("prod", info);
        saveFileName = "";

        //手工封装返回删除成功或删除失败字符串为JSON格式
        String s=session.getAttribute("msg").toString();
        JSONObject object=new JSONObject();
        object.put("msg",s);
        return object.toString();
    }*/

    //删除后分页显示,切记切记切记：坑：使用@ResponseBody注解，返回值不能是String,
    // 如果一定要使用String,则手工封装成JSON格式
    @ResponseBody
    @RequestMapping(value = "/dajaxsplit",produces = "text/html;charset=UTF-8")
    public Object dajaxsplit(HttpServletRequest request) {
        PageInfo info=null;
        Object vo= request.getSession().getAttribute("deleteProdVo");
        if(vo!=null){
            info=productInfoService.splitPageVo((ProductVo)vo,PAGE_SIZE);
        }else{
            info = productInfoService.splitPage(1, PAGE_SIZE);
        }
        request.getSession().setAttribute("info", info);
        return  request.getAttribute("msg");

    }


    //执行批量删除操作
    @RequestMapping("/deleteBatch")
    public String deleteBatch(String pids, HttpSession session) {
        String []ps=pids.split(",");
        try {
            int num = productInfoService.deleteBatch(ps);
            if (num > 0)
                session.setAttribute("msg", "删除成功！");
            else
                session.setAttribute("msg", "删除失败！");
        } catch (Exception e) {

            session.setAttribute("msg","商品不可删除！");
        }

        //增删改后用重定向跳转
        return "forward:/prod/dajaxsplit.action";
    }

    //多条件的查询功能实现+不分页
    @ResponseBody
    @RequestMapping("/condition")
    public void condition(ProductVo vo,HttpSession session){
        List<ProductInfo> list = productInfoService.selectCondition(vo);
        session.setAttribute("list", list); //table中的list表格，一一对应

    }

    //多条件的查询功能+分页显示
    @ResponseBody
    @RequestMapping("/ajaxSplitpaging")
    public void ajaxSplitpaging(ProductVo vo, HttpSession session) {
        PageInfo info = productInfoService.splitPageVo(vo, PAGE_SIZE);
        session.setAttribute("info", info);

    }



}