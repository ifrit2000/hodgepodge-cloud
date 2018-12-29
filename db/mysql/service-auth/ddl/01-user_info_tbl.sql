DROP TABLE IF EXISTS USER_INFO;
CREATE TABLE USER_INFO
(
  USER_ID         VARCHAR(32) PRIMARY KEY COMMENT '用户登陆ID',
  USERNAME        VARCHAR(32)  NOT NULL COMMENT '用户',
  PASSWORD        VARCHAR(256) NOT NULL COMMENT '密码',
  PASSWORD_KEY_ID varchar(32)  NOT NULL COMMENT '密码ID',
  PHONE           VARCHAR(20) COMMENT '电话',
  E_MAIL          VARCHAR(128) COMMENT '邮箱',
  CREATED_DATE    TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建日期',
  UPDATED_DATE    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日期'
)
  ENGINE = INNODB
  DEFAULT CHARSET = utf8;

