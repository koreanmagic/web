package kr.co.koreanmagic.web.db.mybatis.mapper;

import java.util.List;

import kr.co.koreanmagic.web.domain.WorkCondition;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

public interface WorkConditionMapper {
    @Delete({
        "delete from work_condition",
        "where con_id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(Integer id);

    @Insert({
        "insert into work_condition (con_id, name)",
        "values (#{id,jdbcType=INTEGER}, #{name,jdbcType=VARCHAR})"
    })
    int insert(WorkCondition record);

    int insertSelective(WorkCondition record);

    @Select({
        "select",
        "con_id, name",
        "from work_condition",
        "where con_id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    WorkCondition selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkCondition record);

    @Update({
        "update work_condition",
        "set name = #{name,jdbcType=VARCHAR}",
        "where con_id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WorkCondition record);
    
    
    @Select({
        "select",
        "*",
        "from work_condition",
    })
    @ResultMap("BaseResultMap")
    List<WorkCondition> getAll();
}