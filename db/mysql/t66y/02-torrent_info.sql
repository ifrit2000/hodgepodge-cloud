DROP TABLE IF EXISTS TORRENT_INFO;
CREATE TABLE TORRENT_INFO
(
  TOPIC_URL       VARCHAR(128) COMMENT '帖子地址',
  TORRENT_URL     VARCHAR(2048) COMMENT '种子地址',
  TORRENT_HASH    VARCHAR(512)  COMMENT '种子hash',
  TORRENT_STATUS VARCHAR(4) DEFAULT '0',
  CREATED_DATE    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  UPDATED_DATE    TIMESTAMP    DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  foreign key(TOPIC_URL) references TOPIC_INFO(TOPIC_URL)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

