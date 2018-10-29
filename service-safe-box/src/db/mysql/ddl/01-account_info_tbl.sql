create table ACCOUNT_INFO_TBL (
  id               int primary key auto_increment,
  user_id          varchar(32),
  account_id       varchar(32),
  password         varchar(256),
  description      varchar(512),
  account_page_url varchar(128),
  created_date     date
)
  engine = innodb;