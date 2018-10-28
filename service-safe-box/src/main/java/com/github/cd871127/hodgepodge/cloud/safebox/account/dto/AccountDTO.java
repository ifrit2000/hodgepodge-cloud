package com.github.cd871127.hodgepodge.cloud.safebox.account.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AccountDTO {
    private String userId;
    private String accountId;
    private String password;
    private String description;
    private Date createdDate;
}
