package io.github.cd871127.hodgepodge.cloud.safebox.asset.dto;

import lombok.Data;

import java.util.Date;

@Data
public class AssetDTO {
    private String userId;
    private String assetId;
    private String password;
    private String phone;
    private String eMail;
    private String mainPage;
    private String description;
    private Date createdDate;
    private Date updatedDate;
    private Integer status;
}
