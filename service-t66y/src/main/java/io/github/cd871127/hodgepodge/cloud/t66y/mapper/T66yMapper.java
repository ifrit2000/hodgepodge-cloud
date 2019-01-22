package io.github.cd871127.hodgepodge.cloud.t66y.mapper;

import io.github.cd871127.hodgepodge.cloud.t66y.dto.ImageDTO;
import io.github.cd871127.hodgepodge.cloud.t66y.dto.TopicDTO;
import io.github.cd871127.hodgepodge.cloud.t66y.dto.TorrentDTO;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.*;
import org.apache.ibatis.jdbc.SQL;

import java.util.List;
import java.util.Map;

import static org.apache.ibatis.type.JdbcType.INTEGER;
import static org.apache.ibatis.type.JdbcType.VARCHAR;


@Mapper
public interface T66yMapper {

    /**
     * 主题列表
     *
     * @param topicId
     * @param topicStatus
     * @param topicFid
     * @param keyWord
     * @return
     */
    @Results(id = "topicDTOListMap", value = {
            @Result(column = "ID", property = "topicId", jdbcType = INTEGER),
            @Result(column = "TOPIC_URL", property = "topicUrl", jdbcType = VARCHAR),
            @Result(column = "TOPIC_FID", property = "topicFid", jdbcType = VARCHAR),
            @Result(column = "TOPIC_TITLE", property = "topicTitle", jdbcType = VARCHAR),
            @Result(column = "TOPIC_STATUS", property = "topicStatus", jdbcType = VARCHAR),
    })
    @SelectProvider(type = T66ySqlProvider.class, method = "findTopics")
    List<TopicDTO> findTopics(@Param("topicId") Integer topicId, @Param("topicStatus") String topicStatus,
                              @Param("topicFid") String topicFid, @Param("keyWord") String keyWord);

    /**
     * 单个主题详细信息
     *
     * @param topicId
     * @return
     */
    @Results(id = "topicDTOMap", value = {
            @Result(column = "ID", property = "topicId", jdbcType = INTEGER),
            @Result(column = "TOPIC_URL", property = "topicUrl", jdbcType = VARCHAR),
            @Result(column = "TOPIC_FID", property = "topicFid", jdbcType = VARCHAR),
            @Result(column = "TOPIC_TITLE", property = "topicTitle", jdbcType = VARCHAR),
            @Result(column = "TOPIC_STATUS", property = "topicStatus", jdbcType = VARCHAR),
            @Result(column = "TOPIC_URL", property = "torrentDTOList", many = @Many(select = "findTorrentsByTopicUrl")),
            @Result(column = "TOPIC_URL", property = "imageDTOList", many = @Many(select = "findImagesByTopicUrl"))
    })
    @SelectProvider(type = T66ySqlProvider.class, method = "findTopic")
    TopicDTO findTopic(@Param("topicId") Integer topicId);

    /**
     * 主题关联的图片信息
     *
     * @param topicUrl
     * @return
     */
    @Select("select IMAGE_URL,IMAGE_STATUS,FILE_ID,FILE_PATH from IMAGE_INFO where TOPIC_URL=#{topicUrl}")
    List<ImageDTO> findImagesByTopicUrl(@Param("topicUrl") String topicUrl);

    /**
     * 主题关联的种子信息
     *
     * @param topicUrl
     * @return
     */
    @Select("select TORRENT_HASH,TORRENT_STATUS,TORRENT_URL,FILE_ID,FILE_PATH from TORRENT_INFO where TOPIC_URL=#{topicUrl}")
    List<TorrentDTO> findTorrentsByTopicUrl(@Param("topicUrl") String topicUrl);


    class T66ySqlProvider {
        public String findTopics(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("ID,TOPIC_URL", "TOPIC_FID", "TOPIC_TITLE", "TOPIC_STATUS");
                FROM("TOPIC_INFO");
                if (paraMap.get("topicId") != null) {
                    WHERE("ID=#{topicId}");
                }
                if (!StringUtils.isEmpty(paraMap.get("topicStatus"))) {
                    WHERE("TOPIC_STATUS=#{topicStatus}");
                }
                if (!StringUtils.isEmpty(paraMap.get("topicFid"))) {
                    WHERE("TOPIC_FID=#{topicFid}");
                }
                if (!StringUtils.isEmpty(paraMap.get("keyWord"))) {
                    String keyWord = "'%" + paraMap.get("keyWord") + "%'";
                    WHERE("TOPIC_TITLE like " + keyWord);
                }
            }}.toString();
        }

        public String findTopic(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("ID,TOPIC_URL", "TOPIC_FID", "TOPIC_TITLE", "TOPIC_STATUS");
                FROM("TOPIC_INFO");
                WHERE("ID=#{topicId}");
            }}.toString();
        }

    }
}
