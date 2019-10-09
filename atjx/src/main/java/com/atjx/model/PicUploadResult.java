package com.atjx.model;

/**
 * 文件上传的类
 *
 *  {"error":0,
 *  "url":"图片的保存路径",
 *  "width":图片的宽度,
 *  "height":图片的高度}
 * @author Administrator
 *
 */
public class PicUploadResult {

    private Integer error=0;		//0表示无异常，1代表异常
    private String url;
    private String width;
    private String height;

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

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }



}