package com.atjx.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atjx.mapper.SpecificationMapper;
import com.atjx.mapper.WxOderMapper;
import com.atjx.mapper.WxUserMapper;
import com.atjx.mobile.pojo.WeixinUserInfo;
import com.atjx.mobile.util.HttpClientUtil;
import com.atjx.model.WxOrder;
import com.atjx.util.DateUtil;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/mobile")
public class WxLoginController {
    @Resource
    private WxUserMapper wxUserMapper;
//    @Resource//不能使用@Autowired，如果非要使用，不注明泛型类型即可
//    private RedisTemplate<String, WeixinUserInfo> redisTemplate;
    @Resource
    private WxOderMapper wxOderMapper;
    @Resource
    private SpecificationMapper specificationMapper;
    public static final String WX_APPID = "wx7738ac5a31d41ee4";
    public static final String WX_APPSECRET = "7092282a2dd53b572d39c2802b460b2f";
    @RequestMapping("/user")
    public ModelAndView wxLogin(HttpServletResponse response, HttpServletRequest request, Model model) throws IOException, ServletException {
        // 获取客户端cookie
//        request.setCharacterEncoding("utf-8");
//        Cookie[] cookies = request.getCookies();

        //查询session
            WeixinUserInfo weixinUserInfo1= (WeixinUserInfo) request.getSession().getAttribute("weixinUserInfo");

        if (weixinUserInfo1 == null) {
            //请求获取code的回调地址
            //用线上环境的域名或者用内网穿透，不能用ip
            String callBack = "http://atjx.club/mobile/callBack";
            //请求地址
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                    "?appid=" + WX_APPID +
                    "&redirect_uri=" + URLEncoder.encode(callBack, "UTF-8") +
                    "&response_type=code" +
                    "&scope=snsapi_userinfo" +
                    "&state=STATE#wechat_redirect";
            //重定向
            response.sendRedirect(url);
        } else {
            //转发请求
//                request.getRequestDispatcher("http://atjx.club/mobile/callBack").forward(request,response);
//            for (Cookie c : cookies) {
//                if ("openid".equals(c.getName())) { //查找名为uname的cookie
//                    String openid = c.getValue(); //获取这个cookie值，赋给uname
//
//                }
//            }

//            request.getRequestDispatcher("http://atjx.club/mobile/callBack").forward(request,response);

            List<WxOrder> wxOrders = wxOderMapper.findByUser(weixinUserInfo1.getOpenId());
            for (WxOrder w : wxOrders) {
                w.setCreatedStr(DateUtil.getDateStr(w.getCreat_time()));
            }
            Collections.reverse(wxOrders);
            model.addAttribute("wxOrders", wxOrders);
            return new ModelAndView("mobile/my");
        }

        return null;
    }

    //	回调方法
    @RequestMapping("/callBack")
    public String wxCallBack(HttpServletRequest request,Model model,WeixinUserInfo weixinUserInfo){

        WeixinUserInfo weixinUserInfo1= (WeixinUserInfo) request.getSession().getAttribute("weixinUserInfo");
        if(weixinUserInfo1==null){
            //获取access_token
            String code = request.getParameter("code");
            String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                    "?appid=" + WX_APPID +
                    "&secret=" + WX_APPSECRET +
                    "&code=" + code +
                    "&grant_type=authorization_code";

            String result = HttpClientUtil.doGet(url);

//        System.out.println("请求获取access_token:" + result);
            //返回结果的json对象
            JSONObject resultObject = JSON.parseObject(result);

            //请求获取userInfo
            String infoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                    "?access_token=" + resultObject.getString("access_token") +
                    "&openid=" + resultObject.getString("openid") +
                    "&lang=zh_CN";
            String resultInfo = HttpClientUtil.doGet(infoUrl);

            //此时已获取到userInfo，再根据业务进行处理

            WeixinUserInfo infoList = JSON.parseObject(resultInfo, WeixinUserInfo.class);

            weixinUserInfo.setOpenId(infoList.getOpenId());
            weixinUserInfo.setNickname(infoList.getNickname());
            weixinUserInfo.setHeadImgUrl(infoList.getHeadImgUrl());
            try{
                 weixinUserInfo=wxUserMapper.select(infoList.getOpenId());
                if ( weixinUserInfo== null) {

                    wxUserMapper.insert(weixinUserInfo);
                }else {

                    wxUserMapper.update(weixinUserInfo);
                }

                List<WxOrder> wxOrders = wxOderMapper.findByUser(infoList.getOpenId());
                for (WxOrder w : wxOrders) {
                    w.setCreatedStr(DateUtil.getDateStr(w.getCreat_time()));
                }
                request.getSession().setAttribute("weixinUserInfo",weixinUserInfo);
                Collections.reverse(wxOrders);
                model.addAttribute("wxOrders", wxOrders);
            }catch (Exception e){
                e.printStackTrace();
            }
        }else{
            List<WxOrder> wxOrders = wxOderMapper.findByUser(weixinUserInfo1.getOpenId());
            for (WxOrder w : wxOrders) {
                w.setCreatedStr(DateUtil.getDateStr(w.getCreat_time()));
            }
            Collections.reverse(wxOrders);
            model.addAttribute("wxOrders", wxOrders);
        }

        return "mobile/my";
    }
}