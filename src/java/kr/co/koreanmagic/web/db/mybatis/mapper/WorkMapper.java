package kr.co.koreanmagic.web.db.mybatis.mapper;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

import kr.co.koreanmagic.web.db.mybatis.mapper.extension.GenericMapper;
import kr.co.koreanmagic.web.domain.Work;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WorkMapper extends GenericMapper<Work, String> {
	@Delete({
        "delete from work_list",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int deleteByPrimaryKey(Work work);

    @Insert({
        "insert into work_list (id, insert_time, ",
        "con_id, customer, ",
        "item, item_type, ",
        "count, number, ",
        "size, unit, bleed, ",
        "memo, tag, cost, ",
        "price, update_time, ",
        "delivery, subcontract, ",
        "delivery_time, item_memo, ",
        "read_count, bleed_size, ",
        "delivery_memo, list_type_id, ",
        "read_check)",
        "values (#{id,jdbcType=VARCHAR}, #{insertTime,jdbcType=TIMESTAMP}, ",
        "#{condition.id,jdbcType=INTEGER}, #{customer,jdbcType=VARCHAR}, ",
        "#{item,jdbcType=VARCHAR}, #{itemType,jdbcType=VARCHAR}, ",
        "#{count,jdbcType=SMALLINT}, #{number,jdbcType=VARCHAR}, ",
        "#{size,jdbcType=VARCHAR}, #{unit,jdbcType=VARCHAR}, #{bleed,jdbcType=TINYINT}, ",
        "#{memo,jdbcType=VARCHAR}, #{tag,jdbcType=VARCHAR}, #{cost,jdbcType=INTEGER}, ",
        "#{price,jdbcType=INTEGER}, #{updateTime,jdbcType=TIMESTAMP}, ",
        "#{delivery,jdbcType=VARCHAR}, #{subcontract,jdbcType=VARCHAR}, ",
        "#{deliveryTime,jdbcType=TIMESTAMP}, #{itemMemo,jdbcType=VARCHAR}, ",
        "#{readCount,jdbcType=INTEGER}, #{bleedSize,jdbcType=VARCHAR}, ",
        "#{deliveryMemo,jdbcType=VARCHAR}, #{listType.id,jdbcType=INTEGER}, ",
        "#{readCheck,jdbcType=TINYINT})"
    })
    int insert(Work record);

    int insertSelective(Work record);


    int updateByPrimaryKeySelective(Work record);

    @Update({
        "update work_list",
        "set insert_time = #{insertTime,jdbcType=TIMESTAMP},",
          "con_id = #{condition.id,jdbcType=INTEGER},",
          "customer = #{customer,jdbcType=VARCHAR},",
          "item = #{item,jdbcType=VARCHAR},",
          "item_type = #{itemType,jdbcType=VARCHAR},",
          "count = #{count,jdbcType=SMALLINT},",
          "number = #{number,jdbcType=VARCHAR},",
          "size = #{size,jdbcType=VARCHAR},",
          "unit = #{unit,jdbcType=VARCHAR},",
          "bleed = #{bleed,jdbcType=TINYINT},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "tag = #{tag,jdbcType=VARCHAR},",
          "cost = #{cost,jdbcType=INTEGER},",
          "price = #{price,jdbcType=INTEGER},",
          "update_time = #{updateTime,jdbcType=TIMESTAMP},",
          "delivery = #{delivery,jdbcType=VARCHAR},",
          "subcontract = #{subcontract,jdbcType=VARCHAR},",
          "delivery_time = #{deliveryTime,jdbcType=TIMESTAMP},",
          "item_memo = #{itemMemo,jdbcType=VARCHAR},",
          "read_count = #{readCount,jdbcType=INTEGER},",
          "bleed_size = #{bleedSize,jdbcType=VARCHAR},",
          "delivery_memo = #{deliveryMemo,jdbcType=VARCHAR},",
          "list_type_id = #{listType.id,jdbcType=INTEGER},",
          "read_check = #{readCheck,jdbcType=TINYINT}",
        "where id = #{id,jdbcType=VARCHAR}"
    })
    int updateByPrimaryKey(Work record);

    @Select({
        "select",
        "COUNT(*)",
        "from work_list",
    })
    int count();
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ MODIFY MAPPER ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    

    // innerjoin으로 바꿈
    @Select({
    	"select",
        "w.id, w.insert_time, w.con_id, w.customer, w.item, w.item_type, w.count, w.number, w.size, w.unit, ",
        "w.bleed, w.memo, w.tag, w.cost, w.price, w.update_time, w.delivery, w.subcontract, w.delivery_time, ",
        "w.item_memo, w.read_count, w.bleed_size, w.delivery_memo, w.list_type_id, w.read_check,",
        "t.list_type_name list_type_name, c.name name",
        "from work_list w, work_condition c, work_list_type t",
        
        "WHERE w.id = #{id,jdbcType=VARCHAR} AND w.con_id = c.con_id AND w.list_type_id = t.list_type_id"
    })
    @ResultMap("BaseResultMap")
    Work selectByPrimaryKey(String id);
    
    
    
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ SELECT ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    /*
     * 1) SQL에 쓰이는 ${}, #{}의 차이 :
     * ${} --> SQL문을 직접 작성할때 사용. preparedStatement의 ?를 사용하지 않는다.
     * #{} --> preparedStatement의 ?를 채우는 값
     * 
     * 2) SQL문에 입력될 수 있는 값. 
     * 	#{param1} #{param2}..
     * 	#{0} #{1}..
     * 	#{propertyName}..
     * 
     * ★★★★★★★★★ #{0} ${0} 숫자는 상수로 인식한다.
     * 
     * 그런데 신기한건 ${}에 들어가는 식별이다.
     * parma을 사용할때는 #{param}과 동일한 순서로 인자를 인식하는 거서 같은데
     * 0, 1을 적으면 알 수 없다.
     * 
     */
    // list_type이 3, 즉 긴급 게시물은 제외한 모든 게시물 갯수
    @Select({
    	"select",
        "w.id, w.insert_time, w.con_id, w.customer, w.item, w.item_type, w.count, w.number, w.size, w.unit, ",
        "w.bleed, w.memo, w.tag, w.cost, w.price, w.update_time, w.delivery, w.subcontract, w.delivery_time, ",
        "w.item_memo, w.read_count, w.bleed_size, w.delivery_memo, w.list_type_id, w.read_check,",
        "t.list_type_name list_type_name, c.name name",
        "from work_list w, work_condition c, work_list_type t",
        
        "WHERE w.list_type_id in(1, 2) AND t.list_type_id = w.list_type_id AND w.con_id = c.con_id",
        "ORDER BY update_time DESC",
        "LIMIT #{param1,jdbcType=INTEGER}, #{param2,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    List<Work> selectLimitAll(int start, int size);
    
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒ 검색기능 ▒▒▒▒▒▒▒▒▒▒▒▒ */
    @Select({
    	"select",
        "w.id, w.insert_time, w.con_id, w.customer, w.item, w.item_type, w.count, w.number, w.size, w.unit, ",
        "w.bleed, w.memo, w.tag, w.cost, w.price, w.update_time, w.delivery, w.subcontract, w.delivery_time, ",
        "w.item_memo, w.read_count, w.bleed_size, w.delivery_memo, w.list_type_id, w.read_check,",
        "t.list_type_name list_type_name, c.name name",
        "from work_list w, work_condition c, work_list_type t",
        
        "WHERE w.list_type_id in(1, 2) AND w.con_id = #{param1,jdbcType=INTEGER} AND w.customer LIKE #{param2,jdbcType=VARCHAR}",
        "AND t.list_type_id = w.list_type_id AND w.con_id = c.con_id",
        "ORDER BY update_time DESC",
        "LIMIT #{param3,jdbcType=INTEGER}, #{param4,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    List<Work> searchByCustomer(Integer conditionId, String customer, int start, int size);
    
    // 갯수
    @Select({
    	"select",
    	"COUNT(*)",
        "from work_list w",
        "WHERE w.list_type_id in(1, 2) AND w.con_id = #{param1,jdbcType=INTEGER} AND w.customer LIKE #{param2,jdbcType=VARCHAR}",
    })
    Integer searchByCustomerLen(Integer conditionId, String customer);
    
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒ work_list_type & work_condition ▒▒▒▒▒▒▒▒▒▒▒▒ */
    //  긴급을 제외한 모든 리스트
   
    
    
  // [프로시저 호출]  컨디션별 갯수 가지고 오기. 
   void workListStats(Map<String, Integer> map);
   
   
   /*
    * condition 별로 총 갯수 가지고 오기. 긴급 타입은 뺀다.
    * 긴급타입은 게시판에서 별도로 관리한다.
    */
   @Select({
	   "select",
       "w.id, w.insert_time, w.con_id, w.customer, w.item, w.item_type, w.count, w.number, w.size, w.unit, ",
       "w.bleed, w.memo, w.tag, w.cost, w.price, w.update_time, w.delivery, w.subcontract, w.delivery_time, ",
       "w.item_memo, w.read_count, w.bleed_size, w.delivery_memo, w.list_type_id, w.read_check,",
       "t.list_type_name list_type_name, c.name name",
       "from work_list w, work_condition c, work_list_type t",
       
       "WHERE w.list_type_id in(1, 2) AND w.con_id = #{param1,jdbcType=INTEGER} AND t.list_type_id = w.list_type_id AND w.con_id = c.con_id",
       "ORDER BY update_time DESC",
       "LIMIT #{param2,jdbcType=INTEGER}, #{param3,jdbcType=INTEGER}"
   })
   @ResultMap("BaseResultMap")
   List<Work> selectListByCondition(Integer conditionId, int start, int size);
   
   // 긴급을 제외한 condition 총 갯수 가지고 오기
   @Select({
	   "select",
   		"COUNT(*)",
       "from work_list",
       "WHERE list_type_id in(1, 2) AND con_id = #{param1,jdbcType=INTEGER}",
   })
   Integer countListByCondition(Integer conditionId);
   
   // 긴급 게시물
   @Select({
	   "select",
       "w.id, w.insert_time, w.con_id, w.customer, w.item, w.item_type, w.count, w.number, w.size, w.unit, ",
       "w.bleed, w.memo, w.tag, w.cost, w.price, w.update_time, w.delivery, w.subcontract, w.delivery_time, ",
       "w.item_memo, w.read_count, w.bleed_size, w.delivery_memo, w.list_type_id, w.read_check,",
       "t.list_type_name list_type_name, c.name name",
       "from work_list w, work_condition c, work_list_type t",
       
       "WHERE w.list_type_id in(3) AND w.con_id = #{param1,jdbcType=INTEGER} AND t.list_type_id = w.list_type_id AND w.con_id = c.con_id",
       "ORDER BY update_time DESC",
   })
   @ResultMap("BaseResultMap")
   List<Work> selectEmergencytWorks(Integer conditionId);
   
   
   
   
   /* ▒▒▒▒▒▒▒▒▒▒▒▒ GLOBAL ▒▒▒▒▒▒▒▒▒▒▒▒ */
   
   // work_list PRIMARY_KEY 받아오기
   @Select({
       "select date_seq(#{param1,jdbcType=DATE})"
   })
   String sequence(Timestamp date);
   
   
   //▶▶▶ WHERE 문 동적 완성 쿼리
   @Select({
       "select",
       "*",
       "from work_list w INNER JOIN work_condition c USING(con_id)",
       "WHERE ${param1}",
       "ORDER BY update_time DESC LIMIT #{param2,jdbcType=INTEGER}, #{param3,jdbcType=INTEGER}"
   })
   @ResultMap("BaseResultMap")
   List<Work> selectListByCostomSQL(String query, int start, int size);
   
   //▶▶▶ WHERE 문 동적 쿼리문 총 갯수
   @Select({ // 총 갯수
       "select",
       "COUNT(*)",
       "from work_list",
       "WHERE ${value}",
   })
   Integer selectCountByCostomSQL(String whereSql);
   
   
   
   
   /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ UPDATE ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
   
   // 게시물 조회수 갱신
   @Update({
   	"UPDATE work_list SET read_count = read_count + 1, update_time = update_time WHERE id = #{value,jdbcType=VARCHAR}"
   })
   int read(String id);
   
   // 게시물 수정일 갱신
   @Update({
	   	"UPDATE work_list SET update_time = null WHERE id = #{value,jdbcType=VARCHAR}"
   })
   int refresh(String id);
   
   // work_list_type 긴급을 일반으로
   @Update({
	   	"UPDATE work_list SET list_type_id = #{param1,jdbcType=INTEGER} WHERE id = #{param2,jdbcType=VARCHAR}"
   })
   int setListType(Integer listTypeId, String workId);
   
   
   // 읽음표시
	@Update({
		"UPDATE work_list SET read_check = !read_check, update_time = update_time WHERE id = #{param1,jdbcType=VARCHAR}"
	})
	int updateReadCheck(String workId);
   
}