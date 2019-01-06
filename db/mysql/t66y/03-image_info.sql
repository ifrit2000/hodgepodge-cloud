DROP TABLE IF EXISTS IMAGE_INFO;
CREATE TABLE IMAGE_INFO
(
  TOPIC_URL    VARCHAR(128) COMMENT '帖子地址',
  IMAGE_URL    VARCHAR(2048) COMMENT '种子地址',
  image_status varchar(4),
  CREATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  UPDATED_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期',
  foreign key (TOPIC_URL) references TOPIC_INFO (TOPIC_URL)
) ENGINE = INNODB
  DEFAULT CHARSET = utf8;

