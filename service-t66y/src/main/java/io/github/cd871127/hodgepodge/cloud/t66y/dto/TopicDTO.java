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
    private String imageStatus;
    private String torrentStatus;

    private List<TorrentDTO> torrentDTOList;
    private List<ImageDTO> imageDTOList;


    public void setTorrentDTOList(List<TorrentDTO> torrentDTOList) {
        this.torrentDTOList = torrentDTOList;
        torrentStatus = status(torrentDTOList);
    }

    public void setImageDTOList(List<ImageDTO> imageDTOList) {
        this.imageDTOList = imageDTOList;

    }

    @SuppressWarnings("unchecked")
    private static String status(List fileDTOS) {
        int noStart = 0;
        int ok = 0;
        int failed = 0;
        for (FileDTO fileDTO : (List<FileDTO>) fileDTOS) {
            if ("0".equals(fileDTO.getFileStatus())) {
                ++noStart;
            }
            if ("1".equals(fileDTO.getFileStatus())) {
                ++ok;
            }
            if ("2".equals(fileDTO.getFileStatus())) {
                ++failed;
            }
        }
        return "";
    }
}
