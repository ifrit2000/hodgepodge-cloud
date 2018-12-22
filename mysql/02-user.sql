CREATE USER 'appdata'@'%' IDENTIFIED BY '123456';
grant all privileges on service_cipher.* to 'appdata'@'%';

CREATE USER 'appopr'@'%' IDENTIFIED BY '123456';
grant select,delete,update on service_cipher.* to 'appopr'@'%';



flush privileges;