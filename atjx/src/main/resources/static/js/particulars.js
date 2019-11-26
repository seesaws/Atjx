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

		wx.onMenuShareAppMessage({
			title: '分享好友标题', // 分享标题
			desc: '分享好友描述', // 分享描述
			link: currurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
			imgUrl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505419265109&di=cc30743d364e5ae89172c62a662e1321&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2017-6%2F14973136731543515.jpg', // 分享图标
			type: '', // 分享类型,music、video或link，不填默认为link
			dataUrl: '', // 如果type是music或video，则要提供数据链接，默认为空
			success: function () {
				// 用户确认分享后执行的回调函数
				// alert('share successful');
			},
			cancel: function () {
				// 用户取消分享后执行的回调函数
				// alert('share cancel');
			}
		}); // end onMenuShareAppMessage
		wx.onMenuShareTimeline({
			title: '分享朋友圈标题', // 分享标题
			link: currurl, // 分享链接，该链接域名或路径必须与当前页面对应的公众号JS安全域名一致
			imgUrl: 'https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1505419265109&di=cc30743d364e5ae89172c62a662e1321&imgtype=0&src=http%3A%2F%2Fpic.qqtn.com%2Fup%2F2017-6%2F14973136731543515.jpg', // 分享图标
			success: function () {
				// 用户确认分享后执行的回调函数
			},
			cancel: function () {
				// 用户取消分享后执行的回调函数
			}
		}); // end onMenuShareTimeline
	})




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
