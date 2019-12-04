$(function(){
	// tab切换  （我的鲸库、申请提现、达人信息）
	$('#footlist').on("click","ul li",function(){
		$(this).addClass("active").siblings().removeClass("active");
		var ids = $(this).children().attr("href").slice(1);
		$("#"+ids).show().siblings().hide()
	})

});