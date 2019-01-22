package io.github.cd871127.hodgepodge.cloud.t66y.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageDTO extends FileDTO {

    private String imageUrl;
    private String imageStatus;
}
