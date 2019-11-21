package com.atjx.mobile.controller;

import com.atjx.mapper.ItemMapper;
import com.atjx.mapper.SpecificationMapper;
import com.atjx.mapper.WxOderMapper;
import com.atjx.mapper.WxUserMapper;
import com.atjx.mobile.pojo.WeixinUserInfo;
import com.atjx.model.WxOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static com.atjx.mobile.util.HttpRequest.HttpGet;

/**
 * @Classname SupplierController
 * @Description 核销控制器
 * @Date 2019/11/19 10:46
 * @Created by Administrator
 */

@Controller
@RequestMapping("/mobile")
public class SupplierController {

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private WxOderMapper wxOderMapper;
    @Resource
    private SpecificationMapper specificationMapper;
    @Resource
    private WxUserMapper wxUserMapper;
    @RequestMapping("/getSupplier")
    public String getOpenid(HttpServletRequest request, HttpServletResponse response, PrintWriter out, WxOrder wxOrder) throws IOException {

        String code = request.getParameter("code");
        String order_id = request.getParameter("order_id");
        System.out.println(order_id);
        String appId = "wx7738ac5a31d41ee4";
        String appSecret = "7092282a2dd53b572d39c2802b460b2f";
        String result = null;
        wxOrder.setWxorder_id(Integer.parseInt(order_id));
        try {
            String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";
            String getDataStr = "&appid=" + appId + "&secret=" + appSecret+"&code="+code;
            String str = HttpGet(URL, getDataStr);
            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(str);
            String openid = (String)json.get("openid");
            if(openid!=null){

                result=openid;
                WeixinUserInfo weixinUserInfo1=new WeixinUserInfo();
                request.getSession().setAttribute("weixinUserInfo",weixinUserInfo1);
                response.setContentType("text/html; charset=utf-8");
                //查询用户是否为核销商
                WeixinUserInfo weixinUserInfo=wxUserMapper.select(result);
                if (Integer.parseInt(weixinUserInfo.getSupplyer())==0){
//                    out = response.getWriter();

                    out.print("<script language='javascript' type='text/javascript'>alert('非核销商户！');</script>");
                }else if(Integer.parseInt(weixinUserInfo.getSupplyer())==1){
                    wxOrder=wxOderMapper.findById(wxOrder);
                    //102已完成
                    wxOrder.setStatus_code("102");
                    //更新状态码
                    wxOderMapper.update(wxOrder);
                    out.print("<script language='javascript' type='text/javascript'>alert('核销成功！');</script>");
                }else {
                    out.print("<script language='javascript' type='text/javascript'>alert('非法扫码！');</script>");
                }

//        out.flush();//有了这个，下面的return就不会执行了
            }else{
                //获取失败的处理
                System.out.println("获取openid失败");
            }
        } catch (Exception e) {
            // 异常的处理
            e.printStackTrace();
            System.out.println("获取openid异常");
        }finally {
            assert out != null;
            out.close();
        }
//
        return "redirect:http://atjx.club/mobile/user";
    }

}
