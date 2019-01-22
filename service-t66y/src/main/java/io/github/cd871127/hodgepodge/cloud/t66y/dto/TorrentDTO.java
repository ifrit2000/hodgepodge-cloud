package io.github.cd871127.hodgepodge.cloud.t66y.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class TorrentDTO extends FileDTO {
    private String torrentUrl;
    private String torrentHash;
    private String torrentStatus;
}
