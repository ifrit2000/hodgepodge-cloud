package io.github.cd871127.hodgepodge.cloud.account.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface TestMapper {
    @Select("select 1")
    int getOne();
}
