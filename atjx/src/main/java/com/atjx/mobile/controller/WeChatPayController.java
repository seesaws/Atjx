package com.atjx.mobile.controller;


import com.alibaba.fastjson.JSONObject;
import com.atjx.mapper.ItemMapper;
import com.atjx.mapper.SpecificationMapper;
import com.atjx.mapper.WxOderMapper;
import com.atjx.mapper.WxUserMapper;
import com.atjx.mobile.model.ResultInfo;
import com.atjx.mobile.pojo.WeixinUserInfo;
import com.atjx.mobile.util.WXPayUtil;
import com.atjx.model.Item;
import com.atjx.model.Specification;
import com.atjx.model.WxOrder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Controller
public class WeChatPayController {

    @Resource
    private ItemMapper itemMapper;
    @Resource
    private WxOderMapper wxOderMapper;
    @Resource
    private WxUserMapper wxUserMapper;
    @Resource
    private SpecificationMapper specificationMapper;
    @RequestMapping("/getOpenid")
    @ResponseBody
    public JSONObject getOpenid(HttpServletRequest request) {
        String code = request.getParameter("code");
        String appId = "wx7738ac5a31d41ee4";
        String appSecret = "7092282a2dd53b572d39c2802b460b2f";
        JSONObject jsonObject=new JSONObject();

        try {
            String URL = "https://api.weixin.qq.com/sns/oauth2/access_token?grant_type=authorization_code";
            String getDataStr = "&appid=" + appId + "&secret=" + appSecret+"&code="+code;
            String str = HttpGet(URL, getDataStr);
            net.sf.json.JSONObject json = net.sf.json.JSONObject.fromObject(str);
            String openid = (String)json.get("openid");
            if(openid!=null){
                jsonObject.put("openid",openid);
                WeixinUserInfo weixinUserInfo=wxUserMapper.select(openid);
                jsonObject.put("username",weixinUserInfo.getUsername());
                jsonObject.put("phone",weixinUserInfo.getPhone());
            }else{
                //获取失败的处理
                System.out.println("获取openid失败");
            }
        } catch (Exception e) {
            // 异常的处理
            e.printStackTrace();
            System.out.println("获取openid异常");
        }
        return jsonObject;
    }

    //HttpGet方法
    public String HttpGet(String URL, String GetDataStr){

        String getUrl = URL+GetDataStr;
        StringBuffer sb = new StringBuffer();
        InputStreamReader isr = null;
        BufferedReader br = null;
        try
        {
            java.net.URL url = new URL(getUrl);
            URLConnection urlConnection = url.openConnection();
            urlConnection.setAllowUserInteraction(false);
            isr = new InputStreamReader(url.openStream());
            br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        finally
        {
            try {
                br.close();
                isr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    public static String sendPost(String url, String param) {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try {
            URL realUrl = new URL(url);
            // 打开和URL之间的连接
            URLConnection conn = realUrl.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("user-agent",
                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            // 发送POST请求必须设置如下两行
            conn.setDoOutput(true);
            conn.setDoInput(true);
            // 获取URLConnection对象对应的输出流
            out = new PrintWriter(conn.getOutputStream());
            // 发送请求参数
            out.print(param);
            // flush输出流的缓冲
            out.flush();

            out.close();
            // 定义BufferedReader输入流来读取URL的响应
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null) {
                result += line;
            }
        } catch (Exception e) {
            System.out.println("发送 POST 请求出现异常！"+e);
            e.printStackTrace();
        }
        //使用finally块来关闭输出流、输入流
        finally{
            try{
                if(out!=null){
                    out.flush();
                    out.close();
                }
                if(in!=null){
                    in.close();
                }

            }

            catch(IOException ex){
                ex.printStackTrace();
            }
        }
        return result;
    }

    // 第二步，用户点击“确认充值”按钮后，生成用户订单，存入数据库，然后生成一堆信息，向微信支付系统请求prepay_id
// 得到prepay_id后，将一堆信息打包发送到前端，由前端调起支付界面
    @RequestMapping(path = {"/pay/order"}, method = {RequestMethod.POST,RequestMethod.GET})
    @ResponseBody
    public ResultInfo order(HttpServletRequest request, ResultInfo resultInfo, WxOrder wxOrder, Item item, Specification specification) {
//        String orderAccount= request.getParameter("accountID"); //账户
        String orderFee1= request.getParameter("orderFee"); //金额（单位：分）
        String username= request.getParameter("name");
        String usertel= request.getParameter("tel");
        String remark= request.getParameter("remark");
        String openId= request.getParameter("openid");//openid
        String item_id= request.getParameter("item_id");
        String spe_id= request.getParameter("spe_id");
        String op= request.getParameter("op");
        String paternerKey="atjx100410251625asd15gf15r51g54s";
        String appId = "wx7738ac5a31d41ee4";
//        System.out.println(op);
        item.setId(Integer.parseInt(item_id));
        item=itemMapper.findById(item);
        specification.setSpe_id(Integer.parseInt(spe_id));
        specification=specificationMapper.find(specification);
        // 将充值金额的单位由元转换为分
        int index = orderFee1.indexOf(".");
        int length = orderFee1.length();
        Long amLong = 0l;
        if(index == -1){
            amLong = Long.valueOf(orderFee1+"00");
        }else if(length - index >= 3){
            amLong = Long.valueOf((orderFee1.substring(0, index+3)).replace(".", ""));
        }else if(length - index == 2){
            amLong = Long.valueOf((orderFee1.substring(0, index+2)).replace(".", "")+0);
        }else{
            amLong = Long.valueOf((orderFee1.substring(0, index+1)).replace(".", "")+"00");
        }
        String orderFee= amLong.toString();




        try {
            // ---------------生成用户订单-----------------
            String orderId=WXPayUtil.generateOrderId();//生成订单编号
            // 最好设置一个订单状态标志位，在第三步“接收交易状态”时再修改标志位
            // 将用户订单存入数据库等等操作
            // --------------------------------------------

            // ---------------获取用户的IP------------------
            String ip = request.getHeader("x-forwarded-for");
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getHeader("WL-Proxy-Client-IP");
            }
            if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)){
                ip = request.getRemoteAddr();
            }
            if(ip.indexOf(",")!=-1){
                String[] ips = ip.split(",");
                ip = ips[0].trim();
            }
            // -------------------------------------------

            // ----- 统一下单参数------
            // 注意，参数的顺序不能错！！！！否则无法成功下单
            Map<String, String> paraMap = new HashMap<String, String>();
            paraMap.put("appid", appId);
            paraMap.put("attach", op);//附加数据 	attach 	否 	String(127)  	附加数据，在查询API和支付通知中原样返回，可作为自定义参数使用。
            paraMap.put("body", "艾特鲸选-商品");
            paraMap.put("mch_id", "1554775991");
            paraMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串，这里产生的是格式化的时间戳和随机字符
            paraMap.put("openid", openId);
            paraMap.put("out_trade_no",orderId );//订单号
            paraMap.put("spbill_create_ip", ip);
            paraMap.put("total_fee","1");//金额
            paraMap.put("trade_type", "JSAPI");//支付方式
            paraMap.put("notify_url","http://atjx.club/callback");// 此路径是微信服务器调用支付结果通知路径
            String sign = WXPayUtil.generateSignature(paraMap, paternerKey);

            paraMap.put("sign", sign);
            String xml = WXPayUtil.mapToXml(paraMap);//将所有参数(map)转xml格式

            // 统一下单接口
            String unifiedorder_url = "https://api.mch.weixin.qq.com/pay/unifiedorder";

            //发送post请求"统一下单接口"返回预支付id:prepay_id
            String xmlStr = sendPost(unifiedorder_url, xml);

            //以下内容是返回前端页面的json数据
            String prepay_id = "";//预支付id
            if (xmlStr.contains("SUCCESS")) {
                System.out.println("支付系统返回了prepay_id");
                Map<String, String> map = WXPayUtil.xmlToMap(xmlStr);
                prepay_id =map.get("prepay_id");

            }else {
                System.out.println("prepay_id获取失败");
            }

            // =============至此已成功获取到prepay_id================
            //System.out.println("您的prepay_id的值是："+prepay_id);

            // 将“微信内H5调起支付”需要的参数打包成JSON，发给前端
            Map<String, String> payMap = new HashMap<String, String>();
            payMap.put("appId", appId);
            payMap.put("timeStamp", WXPayUtil.getCurrentTimestamp()+"");//当前时间戳
            payMap.put("nonceStr", WXPayUtil.generateNonceStr());//随机字符串
            payMap.put("signType", "MD5");//签名方式
            payMap.put("package", "prepay_id=" + prepay_id);//订单详情扩展字符串
            String paySign = WXPayUtil.generateSignature(payMap, paternerKey);
            payMap.put("paySign", paySign);//签名

            if(payMap!=null){
                resultInfo.setType(1);
                resultInfo.setData(payMap);
                resultInfo.setMessage("成功获得prepay_id,且将数据发送到前端" );
                //设置订单信息
                Date date=new Date();
                wxOrder.setWxorder_id(0);
                wxOrder.setPayment(orderFee1);
                wxOrder.setCreat_time(date);
                wxOrder.setOpenid(openId);
                wxOrder.setUsername(username);
                wxOrder.setPhone(usertel);
                wxOrder.setB_describe(remark);
                wxOrder.setStatus_code("104");
                wxOrder.setOrder_no(paraMap.get("out_trade_no"));
                wxOrder.setItem_title(item.getTitle());
                wxOrder.setOrder_desc(specification.getT_describe());
                wxOrder.setSpe_id(Integer.parseInt(spe_id));
                wxOrder.setItem_id(Integer.parseInt(item_id));
                wxOrder.setS_opid(op);

                //入库
                try{
                    wxOderMapper.insert(wxOrder);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }else{
                resultInfo.setType(0);
                resultInfo.setMessage("获取prepayId失败" );
                resultInfo.setData(null);
            }

        } catch (Exception e) {
            resultInfo.setType(2);
            resultInfo.setMessage("异常：" + e.toString());
            resultInfo.setData(null);
        }
//        System.out.println(resultInfo.getType()+"...."+resultInfo.getMessage()+"...."+resultInfo.getData());

        return resultInfo;
    }
//    样例
//    <bank_type><![CDATA[CFT]]></bank_type>
//    <cash_fee><![CDATA[1]]></cash_fee>
//    <fee_type><![CDATA[CNY]]></fee_type>
//    <is_subscribe><![CDATA[Y]]></is_subscribe>
//    <mch_id><![CDATA[1554775991]]></mch_id>
//    <nonce_str><![CDATA[OGG7CvbNTURFX3NSWoEPmy2qTu9I3nhW]]></nonce_str>
//    <openid><![CDATA[ok9MWvyZxGwi7XgXi2jhyPGP8-TM]]></openid>
//    <out_trade_no><![CDATA[20191128143315NICUwh]]></out_trade_no>
//    <result_code><![CDATA[SUCCESS]]></result_code>
//    <return_code><![CDATA[SUCCESS]]></return_code>
//    <sign><![CDATA[ACFE64BF3695C0CB433B2C5C187BC790]]></sign>
//    <time_end><![CDATA[20191128143321]]></time_end>
//    <total_fee>1</total_fee>
//    <trade_type><![CDATA[JSAPI]]></trade_type>
//    <transaction_id><![CDATA[4200000467201911281682504036]]></transaction_id>
    @RequestMapping("/callback")
    @ResponseBody
    public ResultInfo payCallBack(HttpServletRequest request, HttpServletResponse response){
//        String item_id=request.getParameter("id");
//        item.setId(Integer.parseInt(item_id));
        WxOrder wxOrder=new WxOrder();
        Item item=new Item();
        ResultInfo resultInfo =new ResultInfo();
        InputStream inputStream = null;
        try {
            inputStream = request.getInputStream();
            String xml = WXPayUtil.inputStream2String(inputStream, "UTF-8");
            Map<String, String> notifyMap = WXPayUtil.xmlToMap(xml);//将微信发的xml转map
//            System.out.println("支付系统返回支付结果"+xml);
//            System.out.println(notifyMap);
            if(notifyMap.get("return_code").equals("SUCCESS")){
                System.out.println("return_code："+notifyMap.get("return_code"));
                // 交易成功
                if(notifyMap.get("result_code").equals("SUCCESS")){
                    // 接下来进行一些业务处理
                    //获取购买人openid
                    String openid=notifyMap.get("openid");
                    //获取订单号
                    String out_trade_no =notifyMap.get("out_trade_no");
                    wxOrder.setOrder_no(out_trade_no);
                    wxOrder=wxOderMapper.findByOrderNo(wxOrder);
                    WeixinUserInfo weixinUserInfo=wxUserMapper.select(openid);
                    weixinUserInfo.setUsername(wxOrder.getUsername());
                    weixinUserInfo.setPhone(wxOrder.getPhone());
                    wxUserMapper.update(weixinUserInfo);
                    item.setId(wxOrder.getItem_id());
                    item=itemMapper.findById(item);
                    BigDecimal item_money=item.getMoney();
                    BigDecimal a=new BigDecimal("0.6");
                    BigDecimal b=new BigDecimal("0.4");
                    BigDecimal six=item_money.multiply(a);
                    BigDecimal four=item_money.multiply(b);
                    wxOrder.setStatus_code("100");
                    //更新商品销量+1,库存-1
                    //先查询库存
                    if(item.getNum()>0){
                        item.setNum(item.getNum()-1);
                        item.setSales(item.getSales()+1);
                        itemMapper.update(item);
                        System.out.println("库存销量更新成功!");
                    }else{
                        System.out.println("更新商品信息失败!");
                    }

                        //更新用户金库信息
                        //反商品佣金
                        weixinUserInfo.setMoney(weixinUserInfo.getMoney().add(item_money));
                        //更新用户信息
                        //购买订单数+1
                        weixinUserInfo.setBuyNum(weixinUserInfo.getBuyNum()+1);
                        //判断订单数升级大V
                        if(weixinUserInfo.getBuyNum()>=5){
                            weixinUserInfo.setU_level("1");
                        }
                        //直卖收益
                        weixinUserInfo.setBack_money(weixinUserInfo.getBack_money().add(item_money));
                        //总收益=直卖+团队收益
                        weixinUserInfo.setSum_money(weixinUserInfo.getSum_money().add(item_money));

//                        if(weixinUserInfo.getU_level()==0)
                        wxUserMapper.update(weixinUserInfo);
                    //更新状态码
                    wxOderMapper.update(wxOrder);

                    //获取分享人openid

                    String share_op =notifyMap.get("attach");
                    if(share_op!=null&&!weixinUserInfo.getU_level().equals("1")){

                        try{

                            //购买人60%商品佣金
                            weixinUserInfo.setGroup_money(weixinUserInfo.getGroup_money().add(six));//团队收益
                            //总收益=直卖+团队收益
                            weixinUserInfo.setSum_money(weixinUserInfo.getSum_money().add(six));//总收益

                            weixinUserInfo.setMoney(weixinUserInfo.getMoney().add(six));//余额
                            wxUserMapper.update(weixinUserInfo);
                            //查询分享人
                            WeixinUserInfo share_userInfo=wxUserMapper.select(share_op);
                            //分享人40%商品佣金
                            share_userInfo.setMoney(share_userInfo.getMoney().add(four));//余额
                            //总收益=直卖+团队收益
                            share_userInfo.setSum_money(share_userInfo.getSum_money().add(four));//总收益
                            share_userInfo.setGroup_money(share_userInfo.getGroup_money().add(four));//团队收益
                            wxUserMapper.update(share_userInfo);
                        }catch (Exception e){
                            e.printStackTrace();
                            System.out.println("查询分享人异常!");
                        }
                    }

                }
            }else{
                // 交易失败的处理
                System.out.println("交易失败!");
            }

            response.reset();
            response.setContentType("application/xml");

            response.getOutputStream().write("<xml><return_code><![CDATA[SUCCESS]]></return_code><return_msg><![CDATA[OK]]></return_msg></xml>".getBytes(StandardCharsets.UTF_8)); //告知微信支付系统已收到消息
            response.getOutputStream().flush();
            response.getOutputStream().close();
//            response.getWriter().flush();
//            response.getWriter().close();
            inputStream.close();

        } catch (Exception e) {
            e.printStackTrace();
            // 异常的处理
            System.out.println("回调异常");
        }

        return resultInfo;
    }


}
