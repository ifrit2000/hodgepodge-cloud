package io.github.cd871127.hodgepodge.cloud.t66y.controller;

import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import io.github.cd871127.hodgepodge.cloud.t66y.dto.TopicDTO;
import io.github.cd871127.hodgepodge.cloud.t66y.service.T66yService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController
@RequestMapping("/t66y")
public class T66yController {

    @Resource
    private T66yService t66yService;

    @GetMapping("topic/list")
    public ServerResponse<PageInfo<TopicDTO>> findTopics(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize,
                                                         @RequestParam(required = false) String topicUrl, @RequestParam(required = false) String topicStatus,
                                                         @RequestParam(required = false) String topicFid, @RequestParam(required = false) String keyWord) {
        ServerResponse<PageInfo<TopicDTO>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(t66yService.findTopics(pageNum, pageSize, topicUrl, topicStatus, topicFid, keyWord));
        return serverResponse;
    }

    @GetMapping("topic")
    public ServerResponse<TopicDTO> findTopic() {
        return null;
    }

}
