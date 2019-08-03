CREATE DATABASE IF NOT EXISTS account DEFAULT CHARSET utf8mb4 COLLATE utf8mb4_general_ci;

CREATE USER 'accountwrite'@'%' IDENTIFIED WITH mysql_native_password BY '123456';

grant insert, select, delete, update on account.* to 'accountwrite'@'%';

flush privileges;