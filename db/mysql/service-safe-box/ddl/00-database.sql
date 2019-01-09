CREATE DATABASE IF NOT EXISTS service_safe_box DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
grant all privileges on service_safe_box.* to 'appdata'@'%';
grant insert, select, delete, update on service_safe_box.* to 'appopr'@'%';
flush privileges;