package com.atjx.mobile.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.atjx.mapper.WxUserMapper;
import com.atjx.mobile.pojo.WeixinUserInfo;
import com.atjx.mobile.util.HttpClientUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.URLEncoder;

@RestController
@RequestMapping("/mobile")
public class WxLoginController {
    @Autowired
    private WxUserMapper wxUserMapper;
    public static final String WX_APPID = "wx7738ac5a31d41ee4";
    public static final String WX_APPSECRET = "7092282a2dd53b572d39c2802b460b2f";
    private WeixinUserInfo weixinUserInfo;
    @RequestMapping("/user")
    public void wxLogin(HttpServletResponse response, HttpServletRequest request, WeixinUserInfo weixinUserInfo,HttpSession session) throws IOException {
        //查询session

        Object object =session.getAttribute("weixinUserInfo");
        if(object==null){
            //请求获取code的回调地址
            //用线上环境的域名或者用内网穿透，不能用ip
            System.out.println("session为空");
            String callBack = "http://atjx.club/mobile/callBack/";

            //请求地址
            String url = "https://open.weixin.qq.com/connect/oauth2/authorize" +
                    "?appid=" + WX_APPID +
                    "&redirect_uri=" + URLEncoder.encode(callBack,"UTF-8") +
                    "&response_type=code" +
                    "&scope=snsapi_userinfo" +
                    "&state=STATE#wechat_redirect";
            //重定向
            response.sendRedirect(url);
        }else{
            System.out.println("跳转页面");
            response.sendRedirect("http://atjx.club/mobile/my");
        }






    }

    //	回调方法
    @RequestMapping("/callBack")
    public ModelAndView wxCallBack(HttpServletRequest request, HttpServletResponse response, Model model) throws IOException {
        String code = request.getParameter("code");

        //获取access_token
        String url = "https://api.weixin.qq.com/sns/oauth2/access_token" +
                "?appid=" + WX_APPID +
                "&secret=" + WX_APPSECRET +
                "&code=" + code +
                "&grant_type=authorization_code";

        String result = HttpClientUtil.doGet(url);

        System.out.println("请求获取access_token:" + result);
        //返回结果的json对象
        JSONObject resultObject = JSON.parseObject(result);

        //请求获取userInfo
        String infoUrl = "https://api.weixin.qq.com/sns/userinfo" +
                "?access_token=" + resultObject.getString("access_token") +
                "&openid=" + resultObject.getString("openid") +
                "&lang=zh_CN";
        String resultInfo = HttpClientUtil.doGet(infoUrl);

        //此时已获取到userInfo，再根据业务进行处理
        System.out.println("请求获取userInfo:" + resultInfo);
        WeixinUserInfo infoList = JSON.parseObject(resultInfo, WeixinUserInfo.class);
        if(wxUserMapper.select(infoList.getOpenId())!=null){
            weixinUserInfo.setOpenId(infoList.getOpenId());
            weixinUserInfo.setNickname(infoList.getNickname());
            weixinUserInfo.setHeadImgUrl(infoList.getHeadImgUrl());
            wxUserMapper.insert(weixinUserInfo);
        }else{
            wxUserMapper.update(infoList);
        }
        HttpSession session=request.getSession();
        session.setAttribute("weixinUserInfo", infoList);
        System.out.println(session);
//        model.addAttribute("user",infoList);
        return new ModelAndView("mobile/my");
    }
}