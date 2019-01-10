package io.github.cd871127.hodgepodge.cloud.safebox.asset.mapper;

import io.github.cd871127.hodgepodge.cloud.safebox.asset.dto.AssetDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

@Mapper
public interface AssetMapper {

    @SelectProvider(type = AssetSqlProvider.class, method = "findAssetByUserId")
    List<AssetDTO> findAssetByUserId(@Param("userId") String userId, @Param("assetId") String assetId);


    class AssetSqlProvider {
        public String findAssetByUserId(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("USER_ID", "ASSET_ID", "PASSWORD", "PASSWORD_KEY_ID",
                        "PHONE", "E_MAIL", "MAIN_PAGE", "DESCRIPTION", "CREATED_DATE",
                        "UPDATED_DATE");
                FROM("ASSET_INFO");
                WHERE("USER_ID=#{userId}");
                if (!StringUtils.isEmpty(paraMap.get("assetId"))) {
                    WHERE("ASSET_ID=#{assetId}");
                }
            }}.toString();
        }
    }
}
