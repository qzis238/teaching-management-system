package com.mumululu.mapper;

import com.mumululu.entity.Class;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ClassMapper {

    List<Class> list(@Param("offset") int offset,
                     @Param("limit") int limit,
                     @Param("className") String className);

    long count(@Param("className") String className);

    Class findById(@Param("id") Long id);

    int insert(Class clazz);

    int update(Class clazz);

    int deleteById(@Param("id") Long id);

    int countByClassName(@Param("className") String className,
                         @Param("excludeId") Long excludeId);

    long countStudentsByClassId(@Param("classId") Long classId);

    long countSchedulesByClassId(@Param("classId") Long classId);

    List<Class> options();
}
