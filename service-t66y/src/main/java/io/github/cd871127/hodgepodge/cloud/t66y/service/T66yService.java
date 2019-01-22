package io.github.cd871127.hodgepodge.cloud.t66y.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.t66y.dto.TopicDTO;
import io.github.cd871127.hodgepodge.cloud.t66y.mapper.T66yMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class T66yService {
    @Resource
    private T66yMapper t66yMapper;

    public PageInfo<TopicDTO> findTopics(Integer pageNum, Integer pageSize, Integer topicId, String topicStatus,
                                         String topicFid, String keyWord) {
        PageHelper.startPage(pageNum, pageSize, true, true, true);
        return new PageInfo<>(t66yMapper.findTopics(topicId, topicStatus, topicFid, keyWord));
    }

    public TopicDTO findTopic(Integer topicId) {
        return t66yMapper.findTopic(topicId);
    }

}
