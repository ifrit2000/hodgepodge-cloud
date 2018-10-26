package com.github.cd871127.hodgepodge.cloud.authentication.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface AuthenticationMapper {

    @Select("select 1+1")
    int test();

}
