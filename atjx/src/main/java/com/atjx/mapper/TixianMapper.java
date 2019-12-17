package com.atjx.mapper;


import com.atjx.model.Tixian;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
 * @Classname TixianMapper
 * @Description TODO
 * @Date 2019/12/6 9:48
 * @Created by Administrator
 */

@Mapper
public interface TixianMapper {
    Tixian findByid(Integer id);

    int insert(Tixian tixian);

    int update(Tixian tixian);

    void delete();

    Integer count();

    List<Tixian> TIXIAN_LIST();
}
