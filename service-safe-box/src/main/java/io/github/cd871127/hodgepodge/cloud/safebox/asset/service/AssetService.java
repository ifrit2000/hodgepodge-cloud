package io.github.cd871127.hodgepodge.cloud.safebox.asset.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.dto.AssetDTO;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.mapper.AssetMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class AssetService {

    @Resource
    private AssetMapper assetMapper;

    public PageInfo<AssetDTO> findAssetByUserId(String userId, String assetId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true, true, true);
        return new PageInfo<>(assetMapper.findAssetByUserId(userId, assetId));
    }

    public int saveAsset(AssetDTO assetDTO) {
        return assetMapper.saveAsset(assetDTO);
    }

    public PageInfo<AssetDTO> removeAssetByUserId(String userId, String assetId) {
        PageHelper.startPage(1, 0, true, true, true);
        PageInfo<AssetDTO> pageInfo = new PageInfo<>(assetMapper.findAssetByUserId(userId, assetId));
        int count = assetMapper.removeAssetByUserId(userId, assetId);
        if (pageInfo.getSize() == count) {
            log.warn("size not match");
        }
        return pageInfo;
    }
}
