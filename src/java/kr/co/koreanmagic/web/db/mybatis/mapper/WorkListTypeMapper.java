package kr.co.koreanmagic.web.db.mybatis.mapper;

import java.util.List;

import kr.co.koreanmagic.web.domain.WorkListType;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WorkListTypeMapper {
	@Delete({
        "delete from work_list_type",
        "where list_type_id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into work_list_type (list_type_id, list_type_name)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR})"
    })
    int insert(WorkListType record);

    int insertSelective(WorkListType record);

    @Select({
        "select",
        "list_type_id, list_type_name",
        "from work_list_type",
        "where list_type_id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    WorkListType selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkListType record);

    @Update({
        "update work_list_type",
        "set list_type_name = #{name,jdbcType=VARCHAR}",
        "where list_type_id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WorkListType record);
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ CUSTOM MAPPER ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    
    @Select({
    	"select",
        "list_type_id, list_type_name",
        "from work_list_type",
    })
    @ResultMap("BaseResultMap")
    List<WorkListType> selectAll();
}