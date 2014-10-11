package kr.co.koreanmagic.web.db.mybatis.mapper;

import java.util.List;

import kr.co.koreanmagic.web.db.mybatis.mapper.extension.WorkDataMapper;
import kr.co.koreanmagic.web.domain.WorkFile;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface WorkFileMapper extends WorkDataMapper<WorkFile> {
	
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ extends GenericMapper ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	
	@Delete({
        "delete from work_files",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(WorkFile id);

    @Insert({
        "insert into work_files (memo, work_id, ",
        "file_name, is_use, ",
        "file_type, parent_path, ",
        "upload_time, download_count, ",
        "file_size)",
        "values (#{memo,jdbcType=VARCHAR}, #{workId,jdbcType=VARCHAR}, ",
        "#{fileName,jdbcType=VARCHAR}, #{isUse,jdbcType=TINYINT}, ",
        "#{fileType,jdbcType=VARCHAR}, #{parentPath,jdbcType=VARCHAR}, ",
        "#{uploadTime,jdbcType=TIMESTAMP}, #{downloadCount,jdbcType=INTEGER}, ",
        "#{fileSize,jdbcType=INTEGER})"
    })
    @SelectKey(statement="SELECT LAST_INSERT_ID()", keyProperty="id", before=false, resultType=Integer.class)
    int insert(WorkFile record);

    int insertSelective(WorkFile record);

    @Select({
        "select",
        "id, memo, work_id, file_name, is_use, file_type, parent_path, upload_time, download_count, ",
        "file_size",
        "from work_files",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    WorkFile selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkFile record);

    @Update({
        "update work_files",
        "set memo = #{memo,jdbcType=VARCHAR},",
          "work_id = #{workId,jdbcType=VARCHAR},",
          "file_name = #{fileName,jdbcType=VARCHAR},",
          "is_use = #{isUse,jdbcType=TINYINT},",
          "file_type = #{fileType,jdbcType=VARCHAR},",
          "parent_path = #{parentPath,jdbcType=VARCHAR},",
          "upload_time = #{uploadTime,jdbcType=TIMESTAMP},",
          "download_count = #{downloadCount,jdbcType=INTEGER},",
          "file_size = #{fileSize,jdbcType=INTEGER}",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int updateByPrimaryKey(WorkFile record);
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ extends GenericMapper ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ CUSTOM MAPPER ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    @Select({
    	 "select",
         "id, memo, work_id, file_name, is_use, file_type, parent_path, upload_time, download_count, ",
         "file_size",
         "from work_files",
        "where work_id = #{value,jdbcType=VARCHAR} AND is_use = 1 ORDER BY upload_time" // work랑 아이디가 같고 is_use가 1(유효한 파일이라는 뜻)
    })
    @ResultMap("BaseResultMap")
    List<WorkFile> selectByWorkId(String workId);
    
    // 파일 갯수 알려주기
    @Select({
        "select",
        "COUNT(*)",
        "from work_files",
        "where work_id = #{value,jdbcType=VARCHAR} AND is_use = 1" // work랑 아이디가 같고 is_use가 1(유효한 파일이라는 뜻)
    })
    int count(String workId);
    
    @Update({
    	"UPDATE work_files SET download_count = download_count + 1 WHERE id = #{value,jdbcType=INTEGER}"
    })
    int addCount(Integer id);
}