package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Schedule;
import com.mumululu.service.ScheduleService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @GetMapping("/check-conflict")
    public Result<?> checkConflict(@RequestParam Long teacherId,
                                   @RequestParam Integer dayOfWeek,
                                   @RequestParam Integer period,
                                   @RequestParam(required = false) Long excludeId) {
        return Result.ok(scheduleService.checkConflict(teacherId, dayOfWeek, period, excludeId));
    }

    @GetMapping("/{classId}")
    public Result<?> getByClassId(@PathVariable Long classId) {
        return Result.ok(scheduleService.getByClassId(classId));
    }

    @PostMapping("/copy")
    public Result<?> copy(@RequestBody Map<String, Long> params) {
        Long sourceClassId = params.get("sourceClassId");
        Long targetClassId = params.get("targetClassId");
        if (sourceClassId == null || targetClassId == null) {
            return Result.fail("请选择源班级和目标班级");
        }
        scheduleService.copy(sourceClassId, targetClassId);
        return Result.ok();
    }

    @PostMapping
    public Result<?> create(@RequestBody Schedule schedule) {
        Map<String, Object> data = scheduleService.create(schedule);
        return Result.ok(data);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Schedule schedule) {
        schedule.setId(id);
        scheduleService.update(schedule);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        scheduleService.delete(id);
        return Result.ok();
    }
}
