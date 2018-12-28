CREATE DATABASE IF NOT EXISTS service_auth DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
grant all privileges on service_auth.* to 'appdata'@'%';
grant insert, select, delete, update on service_auth.* to 'appopr'@'%';
flush privileges;