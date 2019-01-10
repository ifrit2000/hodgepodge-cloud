package io.github.cd871127.hodgepodge.cloud.safebox.asset.mapper;

import io.github.cd871127.hodgepodge.cloud.safebox.asset.dto.AssetDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

@Mapper
public interface AssetMapper {

    @SelectProvider(type = AssetSqlProvider.class, method = "findAssetByUserId")
    List<AssetDTO> findAssetByUserId(@Param("userId") String userId, @Param("assetId") String assetId);

    @InsertProvider(type = AssetSqlProvider.class, method = "saveAsset")
    int saveAsset(AssetDTO assetDTO);

    @UpdateProvider(type = AssetSqlProvider.class, method = "removeAssetByUserId")
    int removeAssetByUserId(String userId, String assetId);

    class AssetSqlProvider {
        public String findAssetByUserId(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("USER_ID", "ASSET_ID", "PASSWORD", "PHONE",
                        "E_MAIL", "MAIN_PAGE", "DESCRIPTION", "CREATED_DATE",
                        "UPDATED_DATE");
                FROM("ASSET_INFO");
                WHERE("USER_ID=#{userId}");
                if (!StringUtils.isEmpty(paraMap.get("assetId"))) {
                    WHERE("ASSET_ID=#{assetId}");
                }
            }}.toString();
        }

        public String saveAsset() {
            return new SQL() {{
                INSERT_INTO("ASSET_INFO");
                INTO_VALUES();
                INTO_COLUMNS("USER_ID", "ASSET_ID", "PASSWORD",
                        "PHONE", "E_MAIL", "MAIN_PAGE", "DESCRIPTION");
                INTO_VALUES("#{userId}", "#{assetId}", "#{password}", "#{phone}"
                        , "#{eMail}", "#{mainPage}", "#{description}");
            }}.toString();
        }

        public String removeAssetByUserId(Map<String, String> paraMap) {
            return new SQL() {{
                UPDATE("ASSET_INFO");
                SET("IS_VALID='0'");
                WHERE("USER_ID=#{userId}");
                if (!StringUtils.isEmpty(paraMap.get("assetId"))) {
                    WHERE("ASSET_ID=#{assetId}");
                }
            }}.toString();
        }
    }
}
