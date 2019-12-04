$(function() {
	var price = 99; //商品单价
	// 商品数量增减
	$(".orderbtns").on("click", "span", function() {
		var num = $("#ordernum").val();
		if ($(this).data("type") == "-" && $("#ordernum").val() > 1) {
			num--;
		} else if ($(this).data("type") == "+") {
			num++;
		}
		$("#ordernum").val(num);
		$("#totalmoney").text('￥' + price * num)
	})
	// 商品支付
	$("#paybtn").click(function() {
		if($("#ordername").val() == ""){
			return
		}
		if($("#ordertel").val() == ""){
			return
		}
		var username = $("#ordername").val();//用户姓名
		var usertel = $("#ordertel").val();//用户电话
		var orderremark = $("#ordertel").val();//用户备注
		var num = $("#ordernum").val();//数量
		var totalmoney = $("#totalmoney").text().slice(1);//总价
		var currurl = decodeURIComponent(location.href.split('#')[0]);
		//ajax注入权限验证  
		$.ajax({
			url: "", //后台接口地址
			dataType: 'json',
			data: {
				'url': currurl
			},
			complete: function(XMLHttpRequest, textStatus) {},
			error: function(XMLHttpRequest, textStatus, errorThrown) {
				alert("发生错误：" + errorThrown);
			},
			success: function(res) {
				var appId = res.appId;
				var nonceStr = res.nonceStr;
				var jsapi_ticket = res.jsapi_ticket;
				var timestamp = res.timestamp;
				var signature = res.signature;
				// alert(appId +" " + nonceStr +" "+jsapi_ticket+" "+timestamp+" "+signature);
				wx.config({
					debug: false, //开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。  
					appId: appId, //必填，公众号的唯一标识  
					timestamp: timestamp, // 必填，生成签名的时间戳  
					nonceStr: nonceStr, //必填，生成签名的随机串  
					signature: signature, // 必填，签名，见附录1  
					jsApiList: ['onMenuShareAppMessage', 'onMenuShareTimeline'] //必填，需要使用的JS接口列表，所有JS接口列表 见附录2  
				}); // end wx.config
				wx.ready(function() {
					$.ajax({
						url: "",//后台支付接口
						type: "post",
						dataType: "json",
						success: function(result) {
							wx.chooseWXPay({
								timestamp: result.timeStamp, // 支付签名时间戳，注意微信jssdk中的所有使用timestamp字段均为小写。但最新版的支付后台生成签名使用的timeStamp字段名需大写其中的S字符
								nonceStr: result.nonceStr, // 支付签名随机串，不长于 32 位
								package: "prepay_id=" + result.prepay_id,
								signType: 'MD5', // 签名方式，默认为'SHA1'，使用新版支付需传入'MD5'
								paySign: result.paySign, // 支付签名
								success: function(res) {
									
								}
							});
						},
						error: function(data) {
							alert(data);
						}
					})
				}); // end ready

				wx.error(function(res) {
					// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
					alert('error');
				});
			}
		});
	})
})
