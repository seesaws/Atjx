package com.atjx.mapper;


import com.atjx.model.Tixian;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Classname TixianMapper
 * @Description TODO
 * @Date 2019/12/6 9:48
 * @Created by Administrator
 */

@Mapper
public interface TixianMapper {
    Tixian findByUser(Tixian tixian);

    int insert(Tixian tixian);

    int update(Tixian tixian);

    void delete();
}
