package io.github.cd871127.hodgepodge.cloud.safebox.asset.controller;

import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.dto.AssetDTO;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.service.AssetService;
import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.jdbc.SQL;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

import static io.github.cd871127.hodgepodge.cloud.lib.web.server.response.GeneralHodgepodgeResponse.SUCCESSFUL;

@RestController
@RequestMapping("/asset")
@Slf4j
public class AssetController {

    @Resource
    private AssetService assetService;

    @GetMapping({"{userId}", "{userId}/{assetId}"})
    ServerResponse<PageInfo<AssetDTO>> findAssetByUserId(@PathVariable("userId") String userId, @PathVariable(value = "assetId", required = false) String assetId, @RequestParam(defaultValue = "1") Integer pageNum, @RequestParam(defaultValue = "0") Integer pageSize) {
        ServerResponse<PageInfo<AssetDTO>> serverResponse = new ServerResponse<>(SUCCESSFUL);
        serverResponse.setData(assetService.findAssetByUserId(userId, assetId, pageNum, pageSize));
        return serverResponse;
    }

    @PostMapping("")
    ServerResponse<AssetDTO> saveAsset(@RequestBody AssetDTO assetDTO) {
        return null;
    }

    @DeleteMapping("{userId}/{assetId}")
    ServerResponse<AssetDTO> removeAssetByUserId() {
        return null;
    }



}
