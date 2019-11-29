package com.atjx.mobile.controller;

import com.alibaba.fastjson.JSONObject;
import com.atjx.mobile.model.JsApiTicket;
import com.atjx.mobile.thread.AccessTokenThread;
import com.atjx.mobile.util.RedisTokenHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Formatter;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
/**
 * @Classname WxJSAPIController
 * @Description TODO
 * @Date 2019/11/22 11:09
 * @Created by Administrator
 */

@Controller
public class WxJSAPIController {
    private static Logger log = LoggerFactory.getLogger(AccessTokenThread.class);
    //获取JSSDK的接口地址
    public static final String GET_TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=ACCESS_TOKEN&type=jsapi";
    public static final String appId = "wx7738ac5a31d41ee4";
    public static final String appSecret = "7092282a2dd53b572d39c2802b460b2f";
    @Resource
    private  RedisTokenHelper redisTokenHelper;


        @RequestMapping("/getJSAPIConfig")
        @ResponseBody
        public JSONObject sign(HttpServletRequest request) {
            String url=request.getParameter("url");
            Map<String, String> ret = new HashMap<String, String>();
            String nonce_str = create_nonce_str();
            String timestamp = create_timestamp();
            String string1;
            String signature = "";

            JsApiTicket jsApiTicket=(JsApiTicket)redisTokenHelper.getObject("global_ticket");
            String jsapi_ticket=jsApiTicket.getTicket();
            //注意这里参数名必须全部小写，且必须有序
            string1 = "jsapi_ticket=" + jsapi_ticket +
                    "&noncestr=" + nonce_str +
                    "&timestamp=" + timestamp +
                    "&url=" + url;
//            System.out.println(string1);

            try {
                MessageDigest crypt = MessageDigest.getInstance("SHA-1");
                crypt.reset();
                crypt.update(string1.getBytes("UTF-8"));
                signature = byteToHex(crypt.digest());
            } catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ret.put("appId",appId);
            ret.put("url", url);
            ret.put("jsapi_ticket", jsapi_ticket);
            ret.put("nonceStr", nonce_str);
            ret.put("timestamp", timestamp);
            ret.put("signature", signature);

            return (JSONObject) JSONObject.toJSON(ret);
        }

        private static String byteToHex(final byte[] hash) {
            Formatter formatter = new Formatter();
            for (byte b : hash) {
                formatter.format("%02x", b);
            }
            String result = formatter.toString();
            formatter.close();
            return result;
        }

        private static String create_nonce_str() {
//        return UUID.randomUUID().toString().replaceAll("-", "").substring(0, 32);
            return UUID.randomUUID().toString().replaceAll("-","").substring(0,32);
        }

        private static String create_timestamp() {
            return Long.toString(System.currentTimeMillis() / 1000);
        }


        //获取Ticket方法
    private static String getTicket(String access_token) {
        String ticket = null;
        String url = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token="+ access_token +"&type=jsapi";//这个url链接和参数不能变
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "30000");// 连接超时30秒
            System.setProperty("sun.net.client.defaultReadTimeout", "30000"); // 读取超时30秒
            http.connect();
            InputStream is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            String message = new String(jsonBytes, "UTF-8");
            JSONObject demoJson = JSONObject.parseObject(message);
//            System.out.println("JSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ticket;
    }
}

