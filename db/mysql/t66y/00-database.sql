CREATE DATABASE IF NOT EXISTS service_cipher DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
grant all privileges on service_cipher.* to 'appdata'@'%';
grant insert, select, delete, update on service_cipher.* to 'appopr'@'%';
flush privileges;