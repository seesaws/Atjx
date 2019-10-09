package com.atjx.vo;


public class KindImage {
    private Integer error = 0;	//1表示文件上传失败，0表示成功
    private String url;        	//路径地址，虚拟的网络空间地址

    public Integer getError() {
        return error;
    }
    public void setError(Integer error) {
        this.error = error;
    }
    public String getUrl() {
        return url;
    }
    public void setUrl(String url) {
        this.url = url;
    }

}