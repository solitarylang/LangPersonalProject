-- create table
CREATE TABLE IF NOT EXISTS `runoob_tbl`(
   `runoob_id` INT UNSIGNED AUTO_INCREMENT,
   `runoob_title` VARCHAR(100) NOT NULL,
   `runoob_author` VARCHAR(40) NOT NULL,
   `submission_date` DATE,
   PRIMARY KEY ( `runoob_id` )
)ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- insert data
INSERT INTO runoob_tbl
(runoob_title, runoob_author, submission_date)
VALUES
("学习 PHP", "菜鸟教程", NOW());

-- select
select * from runoob_tbl;

-- update
UPDATE runoob_tbl SET runoob_title='学习 C++' WHERE runoob_id=1;

-- delete data
DELETE FROM runoob_tbl WHERE runoob_id=1;

-- drop table
DROP TABLE runoob_tbl;