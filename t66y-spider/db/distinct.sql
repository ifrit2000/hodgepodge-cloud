delete
from TORRENT_INFO
where id in (
  select *
  from (select a.id id
        from TORRENT_INFO a,
             (select TOPIC_URL,TORRENT_HASH
              from (
                     select count(1) num,TOPIC_URL,TORRENT_HASH
                     from TORRENT_INFO
                     group by TORRENT_HASH,TOPIC_URL) tmp
              where num > 1) tmp2
        where tmp2.TORRENT_HASH = a.TORRENT_HASH
          and tmp2.TOPIC_URL = a.TOPIC_URL
          and a.id not in
              (
                select max(a.id)
                from TORRENT_INFO a,
                     (select TOPIC_URL,TORRENT_HASH
                      from (
                             select count(1) num,TOPIC_URL,TORRENT_HASH
                             from TORRENT_INFO
                             group by TORRENT_HASH,TOPIC_URL) tmp
                      where num > 1) tmp2
                where tmp2.TORRENT_HASH = a.TORRENT_HASH
                  and tmp2.TOPIC_URL = a.TOPIC_URL
                group by a.topic_url,a.TORRENT_HASH
              )) tmp4
);


delete
from IMAGE_INFO
where id in (
  select *
  from (select a.id id
        from IMAGE_INFO a,
             (select TOPIC_URL,IMAGE_URL
              from (
                     select count(1) num,TOPIC_URL,IMAGE_URL
                     from IMAGE_INFO
                     group by IMAGE_URL,TOPIC_URL) tmp
              where num > 1) tmp2
        where tmp2.IMAGE_URL = a.IMAGE_URL
          and tmp2.TOPIC_URL = a.TOPIC_URL
          and a.id not in
              (
                select max(a.id)
                from IMAGE_INFO a,
                     (select TOPIC_URL,IMAGE_URL
                      from (
                             select count(1) num,TOPIC_URL,IMAGE_URL
                             from IMAGE_INFO
                             group by IMAGE_URL,TOPIC_URL) tmp
                      where num > 1) tmp2
                where tmp2.IMAGE_URL = a.IMAGE_URL
                  and tmp2.TOPIC_URL = a.TOPIC_URL
                group by a.topic_url,a.IMAGE_URL
              )) tmp4
);
