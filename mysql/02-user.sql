CREATE USER 'safeboxdata'@'%' IDENTIFIED BY '123456';
grant all privileges on safe_box.* to 'safeboxdata'@'%';

CREATE USER 'safeboxopr'@'%' IDENTIFIED BY '123456';
grant select,delete,update on safe_box.* to 'safeboxopr'@'%';



flush privileges;