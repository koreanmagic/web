package kr.co.koreanmagic.web.db.mybatis.mapper;

import java.util.List;

import kr.co.koreanmagic.web.db.mybatis.mapper.extension.WorkDataMapper;
import kr.co.koreanmagic.web.domain.WorkDraft;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectKey;
import org.apache.ibatis.annotations.Update;

public interface WorkDraftMapper extends WorkDataMapper<WorkDraft> {
	
	/* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▼ extends GenericMapper ▼ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
	
	@Delete({
        "delete from work_drafts",
        "where id = #{id,jdbcType=INTEGER}"
    })
    int deleteByPrimaryKey(WorkDraft id);

    @Insert({
        "insert into work_drafts (upload_time, work_id, ",
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
    int insert(WorkDraft record);

    int insertSelective(WorkDraft record);

    @Select({
        "select",
        "id, upload_time, work_id, file_type, parent_path, file_name, memo, is_use, download_count, ",
        "file_size",
        "from work_drafts",
        "where id = #{id,jdbcType=INTEGER}"
    })
    @ResultMap("BaseResultMap")
    WorkDraft selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(WorkDraft record);

    @Update({
        "update work_drafts",
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
    int updateByPrimaryKey(WorkDraft record);
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ ▲ extends GenericMapper ▲ ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    
    
    
    /* ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ CUSTOM MAPPER ▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒▒ */
    
    @Select({
    	"select",
        "id, upload_time, work_id, file_type, parent_path, file_name, memo, is_use, download_count, ",
        "file_size",
        "from work_drafts",
        "where work_id = #{value,jdbcType=VARCHAR} AND is_use = 1 ORDER BY upload_time", // work랑 아이디가 같고 is_use가 1(유효한 파일이라는 뜻)
    })
    @ResultMap("BaseResultMap")
    List<WorkDraft> selectByWorkId(String workId);
    
    // 파일 갯수 알려주기
    @Select({
        "select",
        "COUNT(*)",
        "from work_drafts",
        "where work_id = #{value,jdbcType=VARCHAR} AND is_use = 1" // work랑 아이디가 같고 is_use가 1(유효한 파일이라는 뜻)
    })
    int count(String workId);
    
    @Update({
    	"UPDATE work_drafts SET download_count = download_count + 1 WHERE id = #{value,jdbcType=INTEGER}"
    })
    int addCount(Integer id);
}