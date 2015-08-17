
SET FOREIGN_KEY_CHECKS=0;

########################## 전체 작업리스트  ##############################
SELECT id,insert_time,con_id,customer,item,item_type,count,number,size,bleed,unit,tag,cost,price,update_time,delivery,delivery_time,subcontract,read_count,bleed_size,read_check
INTO OUTFILE 'g:/data.csv'
CHARACTER SET utf8
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY ''
ESCAPED BY '\\'
LINES TERMINATED BY '\r\n'
FROM work_list;
########################################################

########################## 하청업체  ##############################
SELECT subcontract
INTO OUTFILE 'g:/subcontract.csv'
CHARACTER SET utf8
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY ''
ESCAPED BY '\\'
LINES TERMINATED BY '\r\n'
FROM work_list;
########################################################

SELECT * FROM work_list WHERE subcontract LIKE '%,%';
UPDATE work_list SET subcontract = '(주)굿프린팅' WHERE id IN ('150112008');

########################## 거래처  ##############################
SELECT customer
INTO OUTFILE 'g:/customer.csv'
CHARACTER SET utf8
FIELDS TERMINATED BY ',' OPTIONALLY ENCLOSED BY ''
ESCAPED BY '\\'
LINES TERMINATED BY '\r\n'
FROM work_list;
########################################################

SELECT * FROm addresses;

SHOW CREATE TABLE partner;

SELECT * FROM partner ORDER BY id desc;
SELECT * FROM customers ORDER BY id desc;

SHOW CREATE TABLE Partner;

ALTER TABLE partner DROP COLUMN biz_class;
ALTER TABLE partner MODIFY COLUMN biz_class VARCHAR(255) DEFAULT '';
## 워크리스트 이동
INSERT hancome_work
(id, cost, count, customer, insert_time, item, item_detail, memo, sub_name, price, size, subcontructor, update_time, state)
SELECT 
id, cost, count, customer, insert_time, item, item_type, memo, '', price, concat(size, unit), subcontract, update_time, con_id
FROM work_list;



SELECT * FROM partner;
ALTER TABLE partner DROP COLUMN biz_class;



DROP TABLE callvalue;
SELECT test_date_seq(curdate());

DROP TABLE biz_class;
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



