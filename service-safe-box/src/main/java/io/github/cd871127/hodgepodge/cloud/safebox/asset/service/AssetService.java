package io.github.cd871127.hodgepodge.cloud.safebox.asset.service;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.dto.AssetDTO;
import io.github.cd871127.hodgepodge.cloud.safebox.asset.mapper.AssetMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AssetService {

    @Resource
    private AssetMapper assetMapper;

    public PageInfo<AssetDTO> findAssetByUserId(String userId, String assetId, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize, true, true, true);
        return new PageInfo<>(assetMapper.findAssetByUserId(userId, assetId));
    }

}
