package com.mumululu.service;

import com.mumululu.mapper.DashboardMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final DashboardMapper dashboardMapper;

    public DashboardService(DashboardMapper dashboardMapper) {
        this.dashboardMapper = dashboardMapper;
    }

    public Map<String, Object> getStats() {
        long studentTotal = dashboardMapper.countStudents();
        long classTotal = dashboardMapper.countClasses();
        long pendingHomeworkCount = dashboardMapper.countPendingHomework();

        long presentCount = dashboardMapper.countTodayAttendancePresent();
        long totalCount = dashboardMapper.countTodayAttendanceTotal();
        double todayAttendanceRate = totalCount > 0
                ? Math.round(presentCount * 1000.0 / totalCount) / 10.0
                : 0.0;

        Map<String, Object> stats = new HashMap<>();
        stats.put("studentTotal", studentTotal);
        stats.put("classTotal", classTotal);
        stats.put("todayAttendanceRate", todayAttendanceRate);
        stats.put("pendingHomeworkCount", pendingHomeworkCount);
        return stats;
    }

    public List<Map<String, Object>> getClassStudentDistribution() {
        return dashboardMapper.classStudentDistribution();
    }

    public List<Map<String, Object>> getAttendanceTrend() {
        return dashboardMapper.attendanceTrend();
    }
}
