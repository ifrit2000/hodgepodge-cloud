delete from  IMAGE_INFO where 1=1;
delete from  HTML_INFO where 1=1;
delete from  TORRENT_INFO where 1=1;
delete from TOPIC_INFO where 1=1;

alter table IMAGE_INFO auto_increment= 1;
alter table HTML_INFO auto_increment= 1;
alter table TORRENT_INFO auto_increment= 1;
alter table TOPIC_INFO auto_increment= 1;

insert into TOPIC_INFO(topic_url, topic_fid, topic_title, topic_status, created_date, updated_date)
select *
from TOPIC_INFO_BAK;
insert into IMAGE_INFO(topic_url, image_url, image_status, created_date, updated_date)
select *
from IMAGE_INFO_BAK;
insert into HTML_INFO(topic_url, html, created_date, updated_date)
select *
from HTML_INFO_BAK;
insert into TORRENT_INFO(topic_url, torrent_url, torrent_hash, torrent_status, created_date, updated_date)
select *
from TORRENT_INFO_BAK;



