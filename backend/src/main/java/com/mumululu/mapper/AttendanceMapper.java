package com.mumululu.mapper;

import com.mumululu.entity.Attendance;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface AttendanceMapper {

    List<Map<String, Object>> findStudentsWithStatus(@Param("classId") Long classId,
                                                      @Param("attendanceDate") String attendanceDate);

    int batchInsertOrUpdate(@Param("classId") Long classId,
                            @Param("attendanceDate") String attendanceDate,
                            @Param("records") List<Attendance> records);

    int updateStatus(@Param("id") Long id, @Param("status") Integer status);

    List<Attendance> list(@Param("offset") int offset,
                          @Param("limit") int limit,
                          @Param("classId") Long classId,
                          @Param("startDate") String startDate,
                          @Param("endDate") String endDate);

    long count(@Param("classId") Long classId,
               @Param("startDate") String startDate,
               @Param("endDate") String endDate);
}
