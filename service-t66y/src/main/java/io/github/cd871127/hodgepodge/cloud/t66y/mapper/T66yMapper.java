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
            @Result(column = "{topicUrl=TOPIC_URL,fileId=NULL_COL}", property = "torrentDTOList", many = @Many(select = "findTorrent")),
            @Result(column = "{topicUrl=TOPIC_URL,fileId=NULL_COL}", property = "imageDTOList", many = @Many(select = "findImage"))
    })

    @SelectProvider(type = T66ySqlProvider.class, method = "findTopic")
    TopicDTO findTopic(@Param("topicId") Integer topicId);

    @Select("select (\n" +
            "         case\n" +
            "           when total_status = 0 then 0\n" +
            "           when total_status = 1 then 1\n" +
            "           when total_status = 2 then 2\n" +
            "           when total_status = 3 then 3\n" +
            "           end\n" +
            "         )\n" +
            "from (select sum(group_status) total_status\n" +
            "      from (\n" +
            "             select (\n" +
            "                      case\n" +
            "                        when status_count = 0 and image_status = '0' then 0\n" +
            "                        when status_count = 1 and image_status = '0' then 1\n" +
            "                        when status_count = 0 and image_status = '1' then 0\n" +
            "                        when status_count = 1 and image_status = '1' then 2\n" +
            "                        when status_count = 0 and image_status = '2' then 0\n" +
            "                        when status_count = 1 and image_status = '2' then 0\n" +
            "                        end\n" +
            "                      ) group_status\n" +
            "             from (select '0' image_status,1 status_count\n" +
            "                   union\n" +
            "                   select '1' image_status,1 status_count\n" +
            "                   union\n" +
            "                   select '2' image_status,1 status_count) t1) t2) t3")
    Map<Integer, String> findImageStatusByUrl(@Param("topicUrl") String topicUrl);

    @Select("SELECT TORRENT_STATUS,COUNT(1) FROM TORRENT_INFO WHERE TOPIC_URL=#{topicUrl}")
    String findTorrentStatusByUrl(@Param("topicUrl") String topicUrl);

    /**
     * 主题关联的图片信息
     *
     * @param topicUrl
     * @return
     */
    @SelectProvider(type = T66ySqlProvider.class, method = "findImage")
    List<ImageDTO> findImage(@Param("topicUrl") String topicUrl, @Param("fileId") String fileId);

    /**
     * 主题关联的种子信息
     *
     * @param topicUrl
     * @return
     */
    @SelectProvider(type = T66ySqlProvider.class, method = "findTorrent")
    List<TorrentDTO> findTorrent(@Param("topicUrl") String topicUrl, @Param("fileId") String fileId);

    @Select("(select file_path from IMAGE_INFO where file_id=#{fileId} limit 1) union " +
            "select file_path from TORRENT_INFO where file_id=#{fileId}")
    String findFilePath(@Param("fileId") String fileId);

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
                ORDER_BY("TOPIC_URL DESC");
            }}.toString();
        }

        public String findTopic(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("ID,TOPIC_URL", "TOPIC_FID", "TOPIC_TITLE", "TOPIC_STATUS,null as NULL_COL");
                FROM("TOPIC_INFO");
                WHERE("ID=#{topicId}");
            }}.toString();
        }

        public String findTorrent(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("TORRENT_HASH,TORRENT_STATUS,TORRENT_URL,FILE_ID,FILE_PATH");
                FROM("TORRENT_INFO");
                if (StringUtils.isNotEmpty(paraMap.get("topicUrl"))) {
                    WHERE("TOPIC_URL=#{topicUrl}");
                }
                if (StringUtils.isNotEmpty(paraMap.get("fileId"))) {
                    WHERE("FILE_ID=#{fileId}");
                }
            }}.toString();
        }

        public String findImage(Map<String, String> paraMap) {
            return new SQL() {{
                SELECT("IMAGE_URL,IMAGE_STATUS,FILE_ID,FILE_PATH");
                FROM("IMAGE_INFO");
                if (StringUtils.isNotEmpty(paraMap.get("topicUrl"))) {
                    WHERE("TOPIC_URL=#{topicUrl}");
                }
                if (StringUtils.isNotEmpty(paraMap.get("fileId"))) {
                    WHERE("FILE_ID=#{fileId}");
                }
            }}.toString();
        }
    }
}

//    select (
//         case
//         when total_status = 0 then 0
//                 when total_status = 1 then 1
//                 when total_status = 2 then 2
//                 when total_status = 3 then 3
//                 end
//    )
//    from (select sum(group_status) total_status
//    from (
//            select (
//            case
//            when status_count = 0 and image_status = '0' then 0
//            when status_count = 1 and image_status = '0' then 1
//            when status_count = 0 and image_status = '1' then 0
//            when status_count = 1 and image_status = '1' then 2
//            when status_count = 0 and image_status = '2' then 0
//            when status_count = 1 and image_status = '2' then 0
//            end
//    ) group_status
//    from (select '0' image_status,1 status_count
//            union
//                   select '1' image_status,1 status_count
//                  union
//                   select '2' image_status,1 status_count) t1) t2) t3
