package com.mumululu.mapper;

import com.mumululu.entity.Course;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CourseMapper {

    List<Course> list(@Param("offset") int offset,
                      @Param("limit") int limit,
                      @Param("courseName") String courseName);

    long count(@Param("courseName") String courseName);

    Course findById(@Param("id") Long id);

    int insert(Course course);

    int update(Course course);

    int deleteById(@Param("id") Long id);

    List<Course> options();
}
