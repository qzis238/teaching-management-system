package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/stats")
    public Result<?> stats() {
        return Result.ok(dashboardService.getStats());
    }

    @GetMapping("/class-student-distribution")
    public Result<?> classStudentDistribution() {
        return Result.ok(dashboardService.getClassStudentDistribution());
    }

    @GetMapping("/attendance-trend")
    public Result<?> attendanceTrend() {
        return Result.ok(dashboardService.getAttendanceTrend());
    }
}
