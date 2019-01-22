package io.github.cd871127.hodgepodge.cloud.t66y.dto;

import lombok.Data;

@Data
public abstract class FileDTO {
    private String fileId;
    private String filePath;

    public abstract String getFileStatus();
}
