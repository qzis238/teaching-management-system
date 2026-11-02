package com.mumululu.mapper;

import com.mumululu.entity.SysUser;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {

    SysUser findByUsername(@Param("username") String username);

    int updateLoginFail(@Param("id") Long id,
                        @Param("loginFail") Integer loginFail,
                        @Param("status") Integer status,
                        @Param("lockedUntil") String lockedUntil);
}
