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
	});
	//选择套餐
	// $(".particularsel").on("click","div",function(){
	// 	$(this).addClass("active").siblings().removeClass("active");
	// })
	//关闭分享悬浮框
	$(".close").click(function(){
		$("#sharebtn").hide();
	})
});
