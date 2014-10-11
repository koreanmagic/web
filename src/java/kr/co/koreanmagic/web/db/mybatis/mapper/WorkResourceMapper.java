package kr.co.koreanmagic.web.db.mybatis.mapper;

import java.util.List;

import kr.co.koreanmagic.web.db.mybatis.mapper.extension.WorkDataMapper;
import kr.co.koreanmagic.web.domain.WorkResource;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface WorkResourceMapper extends WorkDataMapper<WorkResource> {
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ extends GenericMapper ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	@Delete({
        "delete from work_resources",
        "where id = #{id,jdbcType=INTEGER}"
    })
	int deleteByPrimaryKey(WorkResource id);

    @Insert({
        "insert into work_resources (upload_time, work_id, ",
        "file_type, parent_path, ",
        "file_name, memo, ",
        "is_use, download_count, ",
        "file_size)",
        "values (#{uploadTime,jdbcType=TIMESTAMP}, #{workId,jdbcType=VARCHAR}, ",
        "#{fileType,jdbcType=VARCHAR}, #{parentPath,jdbcType=VARCHAR}, ",
        "#{fileName,jdbcType=VARCHAR}, #{memo,jdbcType=VARCHAR}, ",
        "#{isUse,jdbcType=TINYINT}, #{downloadCount,jdbcType=INTEGER}, ",
        "#{fileSize,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(WorkResource record);

    int insertSelective(WorkResource record);

    @Select({
        "select",
        "id, upload_time, work_id, file_type, parent_path, file_name, memo, is_use, download_count, ",
        "file_size",
        "from work_resources",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    WorkResource selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkResource record);

    @Update({
        "update work_resources",
        "set upload_time = #{uploadTime,jdbcType=TIMESTAMP},",
          "work_id = #{workId,jdbcType=VARCHAR},",
          "file_type = #{fileType,jdbcType=VARCHAR},",
          "parent_path = #{parentPath,jdbcType=VARCHAR},",
          "file_name = #{fileName,jdbcType=VARCHAR},",
          "memo = #{memo,jdbcType=VARCHAR},",
          "is_use = #{isUse,jdbcType=TINYINT},",
          "download_count = #{downloadCount,jdbcType=INTEGER},",
          "file_size = #{fileSize,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WorkResource record);
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ extends GenericMapper ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ CUSTOM MAPPER ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    @Select({
    	"select",
        "id, upload_time, work_id, file_type, parent_path, file_name, memo, is_use, download_count, ",
        "file_size",
        "from work_resources",
        "where work_id = #{value,jdbcType=VARCHAR} AND is_use = 1 ORDER BY upload_time" // work랑 아이디가 같고 is_use가 1(유효한 파일이라는 뜻)
    })
    @ResultMap("BaseResultMap")
    List<WorkResource> selectByWorkId(String workId);
    
    // 파일 갯수 알려주기
    @Select({
        "select",
        "COUNT(*)",
        "from work_resources",
        "where work_id = #{value,jdbcType=VARCHAR}" // work랑 아이디가 같고 is_use가 1(유효한 파일이라는 뜻)
    })
    int count(String workId);
    
    
    @Update({
    	"UPDATE work_resources SET download_count = download_count + 1 WHERE id = #{value,jdbcType=INTEGER}"
    })
    int addCount(Integer id);
}