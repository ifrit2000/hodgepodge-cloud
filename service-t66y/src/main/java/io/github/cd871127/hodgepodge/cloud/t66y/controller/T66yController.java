package io.github.cd871127.hodgepodge.cloud.t66y.controller;

import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import io.github.cd871127.hodgepodge.cloud.t66y.dto.TopicDTO;
import io.github.cd871127.hodgepodge.cloud.t66y.service.T66yService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController
@RequestMapping("/t66y")
public class T66yController {

    @Resource
    private T66yService t66yService;

    @GetMapping("topic/list")
    public ServerResponse<PageInfo<TopicDTO>> findTopics(@RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize,
                                                         @RequestParam(required = false) Integer topicId, @RequestParam(required = false) String topicStatus,
                                                         @RequestParam(required = false) String topicFid, @RequestParam(required = false) String keyWord) {
        ServerResponse<PageInfo<TopicDTO>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(t66yService.findTopics(pageNum, pageSize, topicId, topicStatus, topicFid, keyWord));
        HashMap
        return serverResponse;
    }

    @GetMapping("topic")
    public ServerResponse<TopicDTO> findTopic(@RequestParam Integer topicId) {
        ServerResponse<TopicDTO> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(t66yService.findTopic(topicId));
        return serverResponse;
    }

    @GetMapping("fid")
    public ServerResponse<Map<String, String>> findFid() {
        ServerResponse<Map<String, String>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        Map<String, String> map = new HashMap<>();
        map.put("2", "亞洲無碼原創區");
        map.put("4", "歐美原創區");
        map.put("5", "動漫原創區");
        map.put("15", "亞洲有碼原創區");
        map.put("25", "國產原創區");
        map.put("26", "中字原創區");
        map.put("27", "轉帖交流區");
        serverResponse.setData(map);
        return serverResponse;
    }

    /**
     * 文件未下载 灰色
     * 部分文件下载了 蓝色
     * 有一个文件异常就是黄色
     * 所有文件异常就是红色
     *
     * @return ServerResponse
     */
    @GetMapping("status")
    public ServerResponse<Map<String, String>> status() {
        ServerResponse<Map<String, String>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        Map<String, String> map = new HashMap<>();
        map.put("0", "处理完成");
        map.put("1", "URL待处理");
        map.put("2", "图片URL异常");
        map.put("3", "种子URL异常");
        map.put("4", "全部URL异常");
        map.put("5", "文件待处理");
        map.put("6", "图片文件异常");
        map.put("7", "种子文件异常");
        map.put("8", "全部文件异常");
        serverResponse.setData(map);
        return serverResponse;
    }

    private byte[] getFileBytes(String fileId) throws IOException {
        String filePath = t66yService.getFileById(fileId);
        Path paths = Paths.get(filePath);
        return Files.readAllBytes(paths);
    }

    @GetMapping("image/{fileId}")
    public ServerResponse<String> getImageById(@PathVariable String fileId) throws IOException {
        ServerResponse<String> serverResponse = new ServerResponse<>(SUCCESSFUL);
        byte[] bytes = getFileBytes(fileId);
        serverResponse.setData("data:image;base64" + Base64.getEncoder().encodeToString(bytes));
        return serverResponse;
    }

    @GetMapping("torrent/{fileId}")
    public ResponseEntity<byte[]> getTorrentById(@PathVariable String fileId) throws IOException {
        byte[] bytes = getFileBytes(fileId);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Disposition", "attachment; filename=" + fileId + ".torrent");
        return ResponseEntity.ok().headers(headers).contentType(MediaType.parseMediaType("application/octet-stream")).body(bytes);
    }

}
