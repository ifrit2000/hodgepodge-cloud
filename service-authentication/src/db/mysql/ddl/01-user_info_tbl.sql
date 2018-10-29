create table USER_INFO_TBL (
  id               int primary key auto_increment,
  user_id          varchar(32),
  password         varchar(256),
  phone varchar (20),
  e_mail varchar (128),
  created_date     date
)
engine = innodb;