package com.atjx.mobile.pojo;

import lombok.Data;

/**
 * @Classname ItemPage
 * @Description TODO
 * @Date 2019/12/13 10:55
 * @Created by Administrator
 */

@Data
public class ItemPage {
    private int curPageData; //当前页
    private int totalPage; //总页数
    private int totalSize; //总数据量
    private boolean hasNext; //是否有下一页
}
