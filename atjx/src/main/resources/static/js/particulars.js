$(function() {
	// 轮播图
	var swiper = new Swiper('.swiper-container', {
		pagination: '.swiper-pagination',
		paginationClickable: true,
		spaceBetween: 30,
		centeredSlides: true,
		autoplay: 2500,
		loop : true,
		autoplayDisableOnInteraction: false
	});
	// 分享功能
	$("#sharebtn").click(function() {
		var currurl = decodeURIComponent(location.href.split('#')[0]);
		//ajax注入权限验证  
		$.ajax({
			url: "",//后台接口地址
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
					wx.onMenuShareAppMessage({
						title: '分享好友标题', // 分享标题
						desc: '分享好友描述', // 分享描述
						link: currurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						imgUrl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505419265109&di=cc30743d364e5ae89172c62a662e1321&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2017-6%2F14973136731543515.jpg', // 分享图标
						type: '', // 分享类型,music、video或link，不填默认为link
						dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
						success: function() {
							// 用户确认分享后执行的回调函数
							// alert('share successful');
						},
						cancel: function() {
							// 用户取消分享后执行的回调函数
							// alert('share cancel');
						}
					}); // end onMenuShareAppMessage
					wx.onMenuShareTimeline({
						title: '分享朋友圈标题', // 分享标题
						link: currurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
						imgUrl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505419265109&di=cc30743d364e5ae89172c62a662e1321&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2017-6%2F14973136731543515.jpg', // 分享图标
						success: function() {
							// 用户确认分享后执行的回调函数
						},
						cancel: function() {
							// 用户取消分享后执行的回调函数
						}
					}); // end onMenuShareTimeline
				}); // end ready

				wx.error(function(res) {
					// config信息验证失败会执行error函数，如签名过期导致验证失败，具体错误信息可以打开config的debug模式查看，也可以在返回的res参数中查看，对于SPA可以在这里更新签名。
					alert('error');
				});
			}
		});
	});
	// 返回顶部
	$(window).scroll(function() {
		if ($(window).scrollTop() > 100) {
			$("#gotops").fadeIn(800);
		} else {
			$("#gotops").fadeOut(800);
		}
	});
	$("#gotops").click(function(){
		$('body,html').animate({scrollTop:0},800);
	})
	//选择套餐
	// $(".particularsel").on("click","div",function(){
	// 	$(this).addClass("active").siblings().removeClass("active");
	// })
	//关闭分享悬浮框
	$(".close").click(function(){
		$("#sharebtn").hide();
	})
})
