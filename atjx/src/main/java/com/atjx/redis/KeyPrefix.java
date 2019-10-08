package com.atjx.redis;

/**
 * Created by jiangyunxiong on 2018/5/21.
 * <p>
 * 缓冲key前缀
 */
public interface KeyPrefix {

    /**
     * 有效期
     *
     * @return
     */
    public int expireSeconds();

    /**
     * 前缀
     *
     * @return
     */
    public String getPrefix();
}
