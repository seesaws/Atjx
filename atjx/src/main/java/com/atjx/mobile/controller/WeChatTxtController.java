package com.atjx.mobile.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * @Title: controller
 * @Description: 访问微信认证 txt 文件
 * @Version: v1.0
 * @Date:2019-04-08 11:36
 */
@Controller
public class WeChatTxtController{

    /**
     * 微信访问的url 类似于 https://域名/项目名/MP_verify_demodemodemo.txt
     * 文件内容只是一个字符串
     * 这里我们将文件内容保存到统一配置中
     * @param txtName
     * @return
     */
    @RequestMapping("{txtName}.txt")
    @ResponseBody
    public String getTxtContent(@PathVariable("txtName") String txtName){
        //出于安全到考虑，可以对访问该路径对请求进行白名单限制
        //比如只允许来自微信的域名的请求
        //当然这部分限制可以放到拦截器或者过滤器中

        /**
         * map 的内容如下，可以存储多个搭配：
         * txt文件名：文件值
         * txt文件名：文件值
         */
        Map map=null;//这里需要你自己实现，内容存在数据库或者缓存里都行
        if(map==null){
            return "error";
        }else{
            //查询到以此文件名保存到内容，即查询到正确到正确到文件
            if( StringUtils.isNotBlank((String)map.get(txtName))){
                return (String)map.get(txtName);
            }
        }

        return "error";
    }
}

