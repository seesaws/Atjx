package com.atjx.mapper;



import com.atjx.mobile.pojo.WeixinUserInfo;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface WxUserMapper {
    int insert(WeixinUserInfo weixinUserInfo);

    int update(WeixinUserInfo weixinUserInfo);

    void delete(Integer order_id);

    WeixinUserInfo select(String openid);

    Integer findByOpenid(String openid);

}
