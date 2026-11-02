package com.mumululu.mapper;

import com.mumululu.entity.Student;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface StudentMapper {

    List<Student> list(@Param("offset") int offset,
                       @Param("limit") int limit,
                       @Param("name") String name,
                       @Param("classId") Long classId);

    long count(@Param("name") String name,
               @Param("classId") Long classId);

    Student findById(@Param("id") Long id);

    int insert(Student student);

    int update(Student student);

    int deleteById(@Param("id") Long id);

    int countByStudentNo(@Param("studentNo") String studentNo,
                         @Param("excludeId") Long excludeId);

    long countPendingHomework(@Param("studentId") Long studentId);
}
