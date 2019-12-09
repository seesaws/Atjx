package com.atjx.mapper;


import com.atjx.model.User;
import org.apache.ibatis.annotations.Mapper;


@Mapper
public interface UserMapper {

    User selectByNameAndPwd(User user);

    int insert(User user);

    int update(User user);

    void selectIsName(User user);

    String selectPasswordByName(User user);
}
