DROP TABLE IF EXISTS TOPIC_INFO;
CREATE TABLE TOPIC_INFO
(
  TOPIC_URL    VARCHAR(128) PRIMARY KEY COMMENT '帖子地址',
  TOPIC_AREA   VARCHAR(4)    NOT NULL COMMENT '帖子分区',
  TOPIC_TITLE  VARCHAR(2048) NOT NULL COMMENT '帖子标题',
  CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期'
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

