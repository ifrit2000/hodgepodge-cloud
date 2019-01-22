package io.github.cd871127.hodgepodge.cloud.t66y.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.t66y.dto.TopicDTO;
import io.github.cd871127.hodgepodge.cloud.t66y.mapper.T66yMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.File;

@Service
@Slf4j
public class T66yService {
    @Resource
    private T66yMapper t66yMapper;

    @Value("${t66y.file.base-path}")
    private String basePath;

    public PageInfo<TopicDTO> findTopics(Integer pageNum, Integer pageSize, Integer topicId, String topicStatus,
                                         String topicFid, String keyWord) {
        PageHelper.startPage(pageNum, pageSize, true, true, true);
        return new PageInfo<>(t66yMapper.findTopics(topicId, topicStatus, topicFid, keyWord));
    }

    public TopicDTO findTopic(Integer topicId) {
        return t66yMapper.findTopic(topicId);
    }


    public String getFileById(String fileId) {
        String filePath = t66yMapper.findFilePath(fileId);
        if (StringUtils.isEmpty(filePath)) {
            return null;
        }
        log.debug(basePath + "/" + filePath + "/" + fileId);
        return basePath + "/" + filePath + "/" + fileId;
    }
}
