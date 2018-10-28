package com.github.cd871127.hodgepodge.cloud.safebox.account.mapper;

import com.github.cd871127.hodgepodge.cloud.safebox.account.dto.AccountInfoDTO;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface AccountMapper {

    /**
     * 添加账户信息
     *
     * @param accountInfoDTO
     * @return
     */
    @Insert("insert into ACCOUNT_INFO_TBL(user_id,account_id,password,description,account_page_url,created_date) " +
            "values(#{userId},#{accountId})")
    int insertAccountInfo(AccountInfoDTO accountInfoDTO);

    List<AccountInfoDTO> getAccountInfo();
}
