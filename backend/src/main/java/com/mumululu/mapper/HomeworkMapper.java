package com.mumululu.mapper;

import com.mumululu.entity.Homework;
import com.mumululu.entity.HomeworkSubmission;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface HomeworkMapper {

    // 作业 CRUD
    List<Homework> list(@Param("offset") int offset,
                        @Param("limit") int limit,
                        @Param("classId") Long classId,
                        @Param("courseId") Long courseId);

    long count(@Param("classId") Long classId,
               @Param("courseId") Long courseId);

    Homework findById(@Param("id") Long id);

    int insert(Homework homework);

    int update(Homework homework);

    int deleteById(@Param("id") Long id);

    int deleteSubmissionsByHomeworkId(@Param("homeworkId") Long homeworkId);

    long countSubmissions(@Param("homeworkId") Long homeworkId);

    // 提交情况
    List<Map<String, Object>> findSubmissionsByHomeworkId(@Param("homeworkId") Long homeworkId,
                                                           @Param("classId") Long classId);

    // 提交作业
    int upsertSubmission(HomeworkSubmission submission);

    // 查询提交记录
    HomeworkSubmission findSubmissionById(@Param("id") Long id);

    // 批改
    int gradeSubmission(@Param("id") Long id,
                        @Param("score") BigDecimal score,
                        @Param("comment") String comment);
}
