package com.atjx.mobile.util;//package com.atjx.mobile.util;
//
//
//
//import java.io.BufferedReader;
//import java.io.InputStream;
//import java.io.InputStreamReader;
//import java.io.OutputStream;
//import java.net.ConnectException;
//import java.net.URL;
//
//import javax.net.ssl.HttpsURLConnection;
//import javax.net.ssl.SSLContext;
//import javax.net.ssl.SSLSocketFactory;
//import javax.net.ssl.TrustManager;
//
//import com.atjx.mobile.model.;
//import com.atjx.mobile.pojo.WeixinUserInfo;
//
//import net.sf.json.JSONObject;
//
//import com.survey.toysrus.model.WechatAccessTokenCheck;
//
//public class weChatUtils {
//    // 微信公众号的appId以及secret
//    private static String appId = "";
//    private static String secret = "";
//    // 获取网页授权access_token的Url，和基础服务access_token不同，记得区分
//    private static String getAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/access_token?appid=APPID&secret=SECRET&code=CODE&grant_type=authorization_code";
//    // 刷新网页授权access_token的Url，和基础服务access_token不同，记得区分
//    private static String getRefreshAccessTokenUrl = "https://api.weixin.qq.com/sns/oauth2/refresh_token?appid=APPID&grant_type=refresh_token&refresh_token=REFRESH_TOKEN";
//    // 检验授权凭证access_token是否有效,和基础服务access_token不同，记得区分
//    private static String checkAccessTokenUrl = "https://api.weixin.qq.com/sns/auth?access_token=ACCESS_TOKEN&openid=OPENID";
//    // 获取用户信息的Url
//    private static String getWXUserInfoUrl = "https://api.weixin.qq.com/sns/userinfo?access_token=ACCESS_TOKEN&openid=OPENID&lang=zh_CN";
//
//    /**
//     * 根据code获取到网页授权access_token
//     *
//     * @author Shen
//     * @param code
//     *            微信回调后带有的参数code值
//     */
//    public static WeChatAccessToken getAccessToken(String code) {
//        String url = weChatUtils.getAccessTokenUrl.replace("APPID", weChatUtils.appId).replace("SECRET", secret)
//                .replace("CODE", code);
//        JSONObject jsonObj=JSONObject.fromObject(httpRequest(url, "POST", null));
//        return (WeChatAccessToken) JSONObject.toBean(jsonObj,WeChatAccessToken.class);
//    }
//
//    /**
//     * 根据在获取accessToken时返回的refreshToken刷新accessToken
//     *
//     * @author Shen
//     * @param refreshToken
//     */
//    public static WeChatAccessToken getRefreshAccessToken(String refreshToken) {
//        String url = weChatUtils.getRefreshAccessTokenUrl.replace("APPID", weChatUtils.appId).replace("REFRESH_TOKEN",
//                refreshToken);
//        JSONObject jsonObj=JSONObject.fromObject(httpRequest(url, "POST", null));
//        return (WeChatAccessToken) JSONObject.toBean(jsonObj,WeChatAccessToken.class);
//    }
//
//    /**
//     * 获取微信用户信息
//     *
//     * @author Shen
//     * @param openId
//     *            微信标识openId
//     * @param accessToken
//     *            微信网页授权accessToken
//     */
//    public static WechatUserinfo getWXUserInfoUrl(String openId, String accessToken) {
//        String url = weChatUtils.getWXUserInfoUrl.replace("OPENID", openId).replace("ACCESS_TOKEN", accessToken);
//        JSONObject jsonObj=JSONObject.fromObject(httpRequest(url, "POST", null));
//        return (WechatUserinfo) JSONObject.toBean(jsonObj,WechatUserinfo.class);
//    }
//
//    /**
//     * 检验授权凭证accessToken是否有效
//     *
//     * @author Shen
//     * @param openId
//     * @param accessToken
//     */
//    public WechatAccessTokenCheck checkAccessToken(String openId, String accessToken) {
//        String url = weChatUtils.checkAccessTokenUrl.replace("OPENID", openId).replace("ACCESS_TOKEN", accessToken);
//        JSONObject jsonObj=JSONObject.fromObject(httpRequest(url, "POST", null));
//        return (WechatAccessTokenCheck) JSONObject.toBean(jsonObj,WechatAccessTokenCheck.class);
//    }
//
//    /**
//     * get或者post请求
//     *
//     * @author Shen
//     * @param requestUrl
//     * @param requestMethod
//     *            GET or POST 需要大写*
//     * @param outputStr
//     * @return
//     */
//    public static String httpRequest(String requestUrl, String requestMethod, String outputStr) {
//        StringBuffer buffer = new StringBuffer();
//        try {
//            // 创建SSLContext对象，并使用我们指定的信任管理器初始化
//            TrustManager[] tm = { new MyX509TrustManager() };
//            SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
//            sslContext.init(null, tm, new java.security.SecureRandom());
//            // 从上述SSLContext对象中得到SSLSocketFactory对象
//            SSLSocketFactory ssf = sslContext.getSocketFactory();
//            URL url = new URL(requestUrl);
//            HttpsURLConnection httpUrlConn = (HttpsURLConnection) url.openConnection();
//            httpUrlConn.setSSLSocketFactory(ssf);
//            httpUrlConn.setDoOutput(true);
//            httpUrlConn.setDoInput(true);
//            httpUrlConn.setUseCaches(false);
//            // 设置请求方式（GET/POST）
//            httpUrlConn.setRequestMethod(requestMethod);
//            if ("GET".equalsIgnoreCase(requestMethod))
//                httpUrlConn.connect();
//            // 当有数据需要提交时
//            if (null != outputStr) {
//                OutputStream outputStream = httpUrlConn.getOutputStream();
//                // 注意编码格式，防止中文乱码
//                outputStream.write(outputStr.getBytes("UTF-8"));
//                outputStream.close();
//            }
//            // 将返回的输入流转换成字符串
//            InputStream inputStream = httpUrlConn.getInputStream();
//            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
//            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
//            String str = null;
//            while ((str = bufferedReader.readLine()) != null) {
//                buffer.append(str);
//            }
//            bufferedReader.close();
//            inputStreamReader.close();
//            // 释放资源
//            inputStream.close();
//            inputStream = null;
//            httpUrlConn.disconnect();
//        } catch (ConnectException ce) {
//            System.out.println("Weixin server connection timed out." + ce.getMessage());
//        } catch (Exception e) {
//            System.out.println("https request error:{}" + e.getMessage());
//        }
//        return buffer.toString();
//    }
//
//}
