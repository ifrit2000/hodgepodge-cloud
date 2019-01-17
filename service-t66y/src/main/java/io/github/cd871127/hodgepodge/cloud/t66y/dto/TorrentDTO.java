package io.github.cd871127.hodgepodge.cloud.t66y.dto;

import lombok.Data;

@Data
public class TorrentDTO {
    private String topicUrl;
    private String torrentUrl;
    private String torrentHash;
    private String torrentStatus;
}
