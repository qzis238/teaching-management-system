package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Attendance;
import com.mumululu.service.AttendanceService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/attendance")
public class AttendanceController {

    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @GetMapping("/students")
    public Result<?> getStudents(@RequestParam Long classId,
                                 @RequestParam String attendanceDate) {
        return Result.ok(attendanceService.getStudents(classId, attendanceDate));
    }

    @PostMapping("/batch")
    @SuppressWarnings("unchecked")
    public Result<?> batchSave(@RequestBody Map<String, Object> body) {
        Long classId = toLong(body.get("classId"));
        String attendanceDate = (String) body.get("attendanceDate");
        List<Map<String, Object>> recordsList = (List<Map<String, Object>>) body.get("records");

        if (classId == null) {
            return Result.fail("请选择班级");
        }
        if (attendanceDate == null || attendanceDate.isEmpty()) {
            return Result.fail("请选择考勤日期");
        }

        List<Attendance> records = new ArrayList<>();
        if (recordsList != null) {
            for (Map<String, Object> item : recordsList) {
                Attendance att = new Attendance();
                att.setStudentId(toLong(item.get("studentId")));
                att.setStatus((Integer) item.get("status"));
                records.add(att);
            }
        }

        return Result.ok(attendanceService.batchSave(classId, attendanceDate, records));
    }

    @PutMapping("/{id}")
    public Result<?> updateStatus(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        Integer status = body.get("status");
        if (status == null) {
            return Result.fail("请选择出勤状态");
        }
        attendanceService.updateStatus(id, status);
        return Result.ok();
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam Long classId,
                          @RequestParam(required = false) String startDate,
                          @RequestParam(required = false) String endDate) {
        return Result.ok(attendanceService.list(page, pageSize, classId, startDate, endDate));
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return null;
    }
}
