package io.github.cd871127.hodgepodge.cloud.t66y.dto;

import lombok.Data;

import java.util.List;

@Data
public class TopicDTO {
    Integer topicId;
    private String topicUrl;
    private String topicFid;
    private String topicTitle;
    private String topicStatus;

    private List<TorrentDTO> torrentDTOList;
    private List<ImageDTO> imageDTOList;

    public String getKey() {
        return this.topicUrl;
    }
}
