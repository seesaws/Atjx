//导航选择器
var url = window.location.href;
if (url.indexOf("newsCategoryManage") > 0
    || url.indexOf("newsCategoryEdit") > 0) {
    $("#news").addClass("active");
    $("#newsCategoryManage").addClass("active");
} else if (url.indexOf("newsManage") > 0 || url.indexOf("newsEdit") > 0) {
    $("#news").addClass("active");
    $("#newsManage").addClass("active");
}

// 提示条配置
toastr.options = { // toastr配置
    "closeButton": false, //是否显示关闭按钮
    "debug": false, //是否使用debug模式
    "progressBar": false, //是否显示进度条，当为false时候不显示；当为true时候，显示进度条，当进度条缩短到0时候，消息通知弹窗消失
    "positionClass": "toast-top-center",//显示的动画位置
    "showDuration": "300", //显示的动画时间
    "hideDuration": "100", //消失的动画时间
    "timeOut": "3000", //展现时间
    "extendedTimeOut": "500", //加长展示时间
    "showEasing": "swing", //显示时的动画缓冲方式
    "hideEasing": "linear", //消失时的动画缓冲方式
    "showMethod": "fadeIn", //显示时的动画方式
    "hideMethod": "fadeOut" //消失时的动画方式
};