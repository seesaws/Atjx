package com.atjx.mobile.thread;

import com.alibaba.fastjson.JSONObject;
import com.atjx.mobile.model.AccessToken;
import com.atjx.mobile.model.JsApiTicket;
import com.atjx.mobile.util.RedisTokenHelper;
import com.atjx.mobile.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 定时获取微信access_token的线程
 *在WechatMpDemoApplication中注解@EnableScheduling，在程序启动时就开启定时任务。
 * 每7200秒执行一次
 */
@Component
public class AccessTokenThread {
    @Resource
    private RedisTokenHelper redisTokenHelper;
    private static Logger log = LoggerFactory.getLogger(AccessTokenThread.class);

    // 第三方用户唯一凭证
    public static String appid = "wx7738ac5a31d41ee4";

    // 第三方用户唯一凭证密钥
    public static String appsecret = "7092282a2dd53b572d39c2802b460b2f";
    // 第三方用户唯一凭证
    public static AccessToken accessToken = null;

    @Scheduled(fixedDelay = 2*3600*1000)
    //7200秒执行一次
    public void gettoken(){


        accessToken= WeixinUtil.getAccessToken(appid,appsecret);
        try{
            redisTokenHelper.saveObject("global_token", accessToken,7200);
            log.info("获取成功，accessToken:"+accessToken.getToken());
            JsApiTicket jsApiTicket = getTicket(accessToken.getToken());

            redisTokenHelper.saveObject("global_ticket", jsApiTicket,7200);
            log.info("获取成功，jsapi_ticket:"+ jsApiTicket.getTicket());




        }catch (Exception e){
            e.printStackTrace();
            log.error("获取token失败");

        }
    }


    //获取Ticket方法
    private static JsApiTicket getTicket(String access_token) {
        String ticket = null;
        JsApiTicket jsApiTicket=new JsApiTicket();
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
            System.out.println("JSON字符串："+demoJson);
            ticket = demoJson.getString("ticket");
            jsApiTicket.setTicket(ticket);
            jsApiTicket.setExpireIn(demoJson.getIntValue("expires_in"));
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsApiTicket;
    }
}