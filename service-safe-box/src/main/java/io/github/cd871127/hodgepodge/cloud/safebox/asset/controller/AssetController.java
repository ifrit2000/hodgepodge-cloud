package io.github.cd871127.hodgepodge.cloud.safebox.asset.controller;

import io.github.cd871127.hodgepodge.cloud.lib.util.PageList;
import io.github.cd871127.hodgepodge.cloud.lib.web.server.response.ServerResponse;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.dto.AssetDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/asset")
@Slf4j
public class AssetController {

    @GetMapping("page/{pageNum}")
    ServerResponse<PageList<AssetDTO>> getAssetsByPage(@PathVariable("pageNum") Integer pageNum) {
        System.out.println(pageNum);
        return null;
    }

    @GetMapping("")
    ServerResponse<List<AssetDTO>> getAllAssets() {
        return null;
    }

    @GetMapping("{assetId}")
    ServerResponse<AssetDTO> getSingleAssets(@PathVariable("assetId") String assetId) {
        return null;
    }

}
