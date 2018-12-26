CREATE DATABASE IF NOT EXISTS t66y DEFAULT CHARSET utf8 COLLATE utf8_general_ci;
grant all privileges on t66y.* to 'appdata'@'%';
grant insert, select, delete, update on t66y.* to 'appopr'@'%';
flush privileges;