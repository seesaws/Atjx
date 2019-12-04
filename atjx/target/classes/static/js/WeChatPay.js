//     function onBridgeReady(){
//         WeixinJSBridge.invoke(
//             'getBrandWCPayRequest',
//             {
//                 "appId" : "<%=paramsMap.get("appId") %>",     //公众号名称，由商户传入
//                 "timeStamp":"<%=paramsMap.get("timeStamp") %>",         //时间戳，自1970年以来的秒数
//                 "nonceStr" : "<%=paramsMap.get("nonceStr")%>", //随机串
//                 "package" : "<%=paramsMap.get("package")%>",
//                 "signType" : "<%=paramsMap.get("signType")%>",         //微信签名方式：
//                 "paySign" : "<%=paramsMap.get("paySign")%>" //微信签名
//             },
//             function(res){
//                 // 使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回    ok，但并不保证它绝对可靠。
//                 if(res.err_msg == "get_brand_wcpay_request:ok" ) {//成功
//                     $("#tip_icon").addClass("weui_icon_success");
//                     $("#tip").text("购买成功");
//                     $("#amount").text("恭喜你，获得"+"<%=goodContent%>");
//                 }else if(res.err_msg == "get_brand_wcpay_request:cancel" ) {//取消
//                     $("#tip_icon").addClass("weui_icon_warn");
//                     $("#tip").text("用户取消支付");
//                 }else if(res.err_msg == "get_brand_wcpay_request:fail" ) {//失败
//                     $("#tip_icon").addClass("weui_icon_warn");
//                     $("#tip").text("支付失败");
//                 }else{
//                     $("#tip").text(res.err_msg);
//                 }
//                 $(".confirm_container").show();
//                 $("#chargeBtn").show();
//             }
//         );
//
//     }
//
// if (typeof WeixinJSBridge == "undefined"){
//     if( document.addEventListener ){
//         document.addEventListener('WeixinJSBridgeReady', onBridgeReady, false);
//     }else if (document.attachEvent){
//         document.attachEvent('WeixinJSBridgeReady', onBridgeReady);
//         document.attachEvent('onWeixinJSBridgeReady', onBridgeReady);
//     }
// }else{
//     onBridgeReady();
// }
