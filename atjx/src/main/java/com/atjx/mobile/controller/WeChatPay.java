//package com.atjx.mobile.controller;
//
//
//import org.springframework.stereotype.Controller;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.servlet.ModelAndView;
//import sun.security.provider.MD5;
//
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Random;
//import java.util.SortedMap;
//import java.util.TreeMap;
//
//@Controller
//public class WeChatPay {
//
//
//    /**
//     * 用户提交支付，获取微信支付订单接口
//     */
//    @RequestMapping(value="/pay")
//    public ModelAndView pay(HttpServletRequest request, HttpServletResponse response){
//        ModelAndView mv = new ModelAndView();
//        String GZHID = "wxfd7c065eee11112222";// 微信公众号id
//        String GZHSecret = "b5b3a627f5d1f8888888888888";// 微信公众号密钥id
//        String SHHID = "111111111";// 财付通商户号
//        String SHHKEY = "mmmmmmmmmmmmmmm";// 商户号对应的密钥
//
//        /*------1.获取参数信息------- */
//        //商户订单号
//        String out_trade_no= request.getParameter("state");
//        //价格
//        String money = request.getParameter("money");
//        //金额转化为分为单位
//        String finalmoney = WeChat.getMoney(money);
//        //获取用户的code
//        String code = request.getParameter("code");
//
//        /*------2.根据code获取微信用户的openId和access_token------- */
//        //注： 如果后台程序之前已经得到了用户的openId 可以不需要这一步，直接从存放openId的位置或session中获取就可以。
//        //toPay.jsp页面中提交的url路径也就不需要再经过微信重定向。写成：http://localhost:8080/项目名/wechat/pay?money=${sumPrice}&state=${orderId}
//        String openid=null;
//        try {
//            List<Object> list = accessToken(code);
//            openid=list.get(1).toString();
//        } catch (IOException e) {
//            logger.error("根据code获取微信用户的openId出现错误", e);
//            mv.setViewName("error");
//            return mv;
//        }
//
//        /*------3.生成预支付订单需要的的package数据------- */
//        //随机数
//        String nonce_str;
//        nonce_str = MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
//        //订单生成的机器 IP
//        String spbill_create_ip = request.getRemoteAddr();
//        //交易类型 ：jsapi代表微信公众号支付
//        String trade_type = "JSAPI";
//        //这里notify_url是 微信处理完支付后的回调的应用系统接口url。
//        String notify_url ="http://69a6a38e.ngrok.natapp.cn/heyi-console/wechat/weixinNotify";
//
//        SortedMap<String, String> packageParams = new TreeMap<String, String>();
//        packageParams.put("appid",  GZHID);
//        packageParams.put("mch_id",  SHHID);
//        packageParams.put("nonce_str", nonce_str);
//        packageParams.put("body", "费用");
//        packageParams.put("out_trade_no", out_trade_no);
//        packageParams.put("total_fee", finalmoney);
//        packageParams.put("spbill_create_ip", spbill_create_ip);
//        packageParams.put("notify_url", notify_url);
//        packageParams.put("trade_type", trade_type);
//        packageParams.put("openid", openid);
//
//        /*------4.根据package数据生成预支付订单号的签名sign------- */
//        RequestHandler reqHandler = new RequestHandler(request, response);
//        reqHandler.init( GZHID,  GZHSecret,  SHHKEY);
//        String sign = reqHandler.createSign(packageParams);
//
//        /*------5.生成需要提交给统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder 的xml数据-------*/
//        String xml="<xml>"+
//                "<appid>"+ GZHID+"</appid>"+
//                "<mch_id>"+ SHHID+"</mch_id>"+
//                "<nonce_str>"+nonce_str+"</nonce_str>"+
//                "<sign>"+sign+"</sign>"+
//                "<body><![CDATA["+"费用"+"]]></body>"+
//                "<out_trade_no>"+out_trade_no+"</out_trade_no>"+
//                "<total_fee>"+finalmoney+"</total_fee>"+
//                "<spbill_create_ip>"+spbill_create_ip+"</spbill_create_ip>"+
//                "<notify_url>"+notify_url+"</notify_url>"+
//                "<trade_type>"+trade_type+"</trade_type>"+
//                "<openid>"+openid+"</openid>"+
//                "</xml>";
//
//        /*------6.调用统一支付接口https://api.mch.weixin.qq.com/pay/unifiedorder 生产预支付订单----------*/
//        String createOrderURL = "https://api.mch.weixin.qq.com/pay/unifiedorder";
//        String prepay_id="";
//        try {
//            prepay_id = GetWxOrderno.getPayNo(createOrderURL, xml);
//            if(prepay_id.equals("")){
//                mv.addObject("ErrorMsg", "支付错误");
//                mv.setViewName("error");
//                return mv;
//            }
//        } catch (Exception e) {
//            logger.error("统一支付接口获取预支付订单出错", e);
//            mv.setViewName("error");
//            return mv;
//        }
//        /*将prepay_id存到库中*/
//        PageData p = new PageData();
//        p.put("shopId", out_trade_no);
//        p.put("prePayId", prepay_id);
//        activityService.updatePrePayId(p);
//
//
//        /*------7.将预支付订单的id和其他信息生成签名并一起返回到jsp页面 ------- */
//        nonce_str= MD5.getMessageDigest(String.valueOf(new Random().nextInt(10000)).getBytes());
//        SortedMap<String, String> finalpackage = new TreeMap<String, String>();
//        String timestamp = String.valueOf(System.currentTimeMillis() / 1000);
//        String packages = "prepay_id="+prepay_id;
//        finalpackage.put("appId",  GZHID);
//        finalpackage.put("timeStamp", timestamp);
//        finalpackage.put("nonceStr", nonce_str);
//        finalpackage.put("package", packages);
//        finalpackage.put("signType", "MD5");
//        String finalsign = reqHandler.createSign(finalpackage);
//
//        mv.addObject("appid",  GZHID);
//        mv.addObject("timeStamp", timestamp);
//        mv.addObject("nonceStr", nonce_str);
//        mv.addObject("packageValue", packages);
//        mv.addObject("paySign", finalsign);
//        mv.addObject("success","ok");
//        mv.setViewName("wechat/pay");
//        return mv;
//    }
//}
