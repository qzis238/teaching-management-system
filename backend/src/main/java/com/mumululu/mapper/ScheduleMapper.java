package com.mumululu.mapper;

import com.mumululu.entity.Schedule;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ScheduleMapper {

    List<Schedule> findByClassId(@Param("classId") Long classId);

    int insert(Schedule schedule);

    int update(Schedule schedule);

    int deleteById(@Param("id") Long id);

    int countByClassDayPeriod(@Param("classId") Long classId,
                              @Param("dayOfWeek") Integer dayOfWeek,
                              @Param("period") Integer period,
                              @Param("excludeId") Long excludeId);

    int countByTeacherDayPeriod(@Param("teacherId") Long teacherId,
                                @Param("dayOfWeek") Integer dayOfWeek,
                                @Param("period") Integer period,
                                @Param("excludeId") Long excludeId);

    long countByClassId(@Param("classId") Long classId);

    List<Schedule> findByClassIdRaw(@Param("classId") Long classId);
}
