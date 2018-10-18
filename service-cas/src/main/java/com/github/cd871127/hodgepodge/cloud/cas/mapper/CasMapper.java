package com.github.cd871127.hodgepodge.cloud.cas.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CasMapper {

    @Select("select 1+1")
    int test();

}
