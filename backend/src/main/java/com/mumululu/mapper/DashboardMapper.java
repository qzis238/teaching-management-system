package com.mumululu.mapper;

import java.util.List;
import java.util.Map;

public interface DashboardMapper {

    long countStudents();

    long countClasses();

    long countTodayAttendancePresent();

    long countTodayAttendanceTotal();

    long countPendingHomework();

    List<Map<String, Object>> classStudentDistribution();

    List<Map<String, Object>> attendanceTrend();
}
