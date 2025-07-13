package com.pig.app.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.pig.app.user.entity.User;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface UserMapper extends BaseMapper<User> {

    List<User> selectByLimit(@Param("limit") long limit, @Param("offset") long offset);

    List<User> selectByLimitOptimized(@Param("limit") long limit, @Param("offset") long offset);
}
