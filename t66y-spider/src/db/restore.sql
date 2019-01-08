delete from IMAGE_INFO    where 1=1;
delete from HTML_INFO     where 1=1;
delete from TORRENT_INFO where 1=1;
delete from TOPIC_INFO    where 1=1;

insert into TOPIC_INFO   select * from TOPIC_INFO_BAK;
insert into IMAGE_INFO select * from IMAGE_INFO_BAK;
insert into HTML_INFO select * from HTML_INFO_BAK;
insert into TORRENT_INFO select * from TORRENT_INFO_BAK;
