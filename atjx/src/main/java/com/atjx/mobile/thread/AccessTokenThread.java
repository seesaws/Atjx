package com.atjx.mobile.thread;

import com.atjx.mobile.model.AccessToken;
import com.atjx.mobile.util.WeixinUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * 定时获取微信access_token的线程
 *在WechatMpDemoApplication中注解@EnableScheduling，在程序启动时就开启定时任务。
 * 每7200秒执行一次
 */
@Component
public class AccessTokenThread {
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
        if(null!=accessToken){
            log.info("获取成功，accessToken:"+accessToken.getToken());
        }else {
            log.error("获取token失败");
        }
    }
}