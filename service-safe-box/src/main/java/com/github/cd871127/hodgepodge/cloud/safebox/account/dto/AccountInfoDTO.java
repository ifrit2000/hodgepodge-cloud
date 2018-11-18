package com.github.cd871127.hodgepodge.cloud.safebox.account.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountInfoDTO {
    /**
     * 账户属组
     */
    private String userId;
    /**
     * 账户ID
     */
    private String accountId;
    private String password;
    /**
     * 账户类型 普通 金融账户
     */
    private String accountType;
    /**
     * 描述
     */
    private String description;
    /**
     * 账户主页
     */
    private String accountPageUrl;
    /**
     * 生成日期
     */
    private Date createdDate;
    /**
     * 修改密码的日期
     */
    private Date updatedDate;
}
