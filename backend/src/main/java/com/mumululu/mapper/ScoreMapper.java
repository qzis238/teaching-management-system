package com.mumululu.mapper;

import com.mumululu.entity.Score;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScoreMapper {

    List<Score> list(@Param("offset") int offset,
                     @Param("limit") int limit,
                     @Param("classId") Long classId,
                     @Param("courseId") Long courseId,
                     @Param("examType") String examType);

    long count(@Param("classId") Long classId,
               @Param("courseId") Long courseId,
               @Param("examType") String examType);

    int batchInsertOrUpdate(@Param("courseId") Long courseId,
                            @Param("examType") String examType,
                            @Param("examDate") String examDate,
                            @Param("records") List<Score> records);

    int updateScore(@Param("id") Long id, @Param("score") java.math.BigDecimal score);

    int deleteById(@Param("id") Long id);

    Score findById(@Param("id") Long id);

    List<Score> findByStudentId(@Param("studentId") Long studentId);
}
