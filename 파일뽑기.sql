
SET FOREIGN_KEY_CHECKS=0;

## 테이블 삭제;

# drop table if exists subcontractor;
drop table if exists test_work;
drop table if exists test_workfile;
drop table if exists biz_class;
drop table if exists customers;
drop table if exists test_work;

drop table if exists work_group;
drop table if exists work;
drop table if exists item_category;

drop table if exists audio;
drop table if exists machine;
drop table if exists partner;
drop table if exists customers;
drop table if exists subcontractor;

SELECT * FROM customers;

drop table if exists item_category;
drop table if exists addresses;
# drop table if exists workstate;

SELECT * FROM subcontractor;


drop table if exists any;
drop table if exists parent;
drop table if exists child1;
drop table if exists child2;

SELECT * FROM any;
SELECT * FROM parent;
SELECT * FROM child1;
SELECT * FROM child2;


SELECT id, upload_time uploadTime, work_id workId, file_type fileType,file_name fileName, memo, is_use isUse, download_count downloadCount, file_size fileSize FROM work_files LIMIT 0, 2;

SELECT * FROM customers INNER JOIN partner USING(id);

SELECT * FROM addresses;

show tables;
show create table partner;
show create table subcontractor;
show create table customers;
show create table bank_name;
show create table work_group;
show create table work;
show create table test_workfile;
show create table biz_class;
show create table addresses;
show create table item_category;

select  * FROm customers INNER JOIN partner USING(id) ORDER BY id DESC;
SELECT * FROM testTable;
create table id_test (
	id int not null auto_increment,
	work_id varchar(255),
	primary key(id, work_id)
);


SELECT * FROM work ORDER BY id DESC;


SELECT * FROM work_group ORDER BY id DESC;
SELECT * FROM work w, work_group g WHERE g.id = 11400;
show tables;

select * FROM test1 INNER JOIN test2 USING(id);

drop table if exists test1;
drop table if exists test2;
drop table if exists test1_map;
drop table if exists test1_test2;

SELECT * FROM bank_name;
SELECT * FROM subcontractor;
SELECT * FROM work_state;
SELECT * FROM work;
SELECT * FROM work_group;
SELECT * FROM item_category;
SELECT * FROM biz_class;
SELECT * FROM item_category;



select * FROM subcontractor;
show create table subcontractor;
DROP TABLE subcontractor;

SHOW tables;

DROP TABLE work_files2;

CREATE TABLE work_files2 (
  memo varchar(255) NOT NULL DEFAULT '',
  id int(11) NOT NULL AUTO_INCREMENT,
  work_id varchar(9) NOT NULL,
  file_name varchar(125) NOT NULL,
  is_use tinyint(4) DEFAULT '1',
  file_type varchar(5) NOT NULL,
  parent_path varchar(255) NOT NULL,
  upload_time timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  download_count int(11) NOT NULL DEFAULT '0',
  file_size int(10) unsigned NOT NULL,
  PRIMARY KEY (id),
  KEY idx_file2 (id),
  KEY work_id_files_fdk2 (work_id),
  CONSTRAINT work_id_files_fdk2 FOREIGN KEY (work_id) REFERENCES work_list (id) ON DELETE CASCADE ON UPDATE CASCADE
) SELECT * FROM work_files;

SELECT count(*) FROM work_list WHERE subcontract = '';

DROP TABLE callvalue;
SELECT test_date_seq(curdate());

DELETE FROM biz_class;
SELECT * FROM biz_class;
ALTER TABLE biz_class auto_increment = 1;
INSERT INTO biz_class (biz_class) VALUES
('공공'),
('비영리'),
('교육'),
('금융'),
('법률'),
('미디어'),
('서비스'),
('판매유통'),
('웹통신'),
('건설'),
('제조업'),
('종교'),
('개인'),
('기타');

# 유일키 위한 테이블
CREATE TABLE test_work_list_sequences (
    today DATE NOT NULL,
    seq_currval INT(3) zerofill UNSIGNED NOT NULL,
    PRIMARY KEY (today)
)  ENGINE=MyISAM;

show tables;


DELETE FROM testTable;
SELECT * FROM testTable;


show create procedure work_list_stats;

DELIMITER ;;
CREATE FUNCTION test_date_seq(d DATE)
	RETURNS VARCHAR(20)
	modifies SQL data
	SQL SECURITY INVOKER
	NOT DETERMINISTIC ## 함수 실행마다 반환값이 바뀌는지? 아니면 NOT! 성능문제 직격

	BEGIN

		DECLARE current_value INT(3) ZEROFILL;

		IF d IS NULL THEN
			SET d = curdate();
		END IF;

		INSERT INTO test_work_list_sequences
			SET today = (@v_today := d),
			seq_currval = (@v_current_value := 1)
			ON DUPLICATE key
			UPDATE seq_currval = (@v_current_value := seq_currval+1);
		
		SET current_value = @v_current_value;
		
		RETURN concat(RIGHT(d+0, 6), current_value);
	END;;

DELIMITER ;


