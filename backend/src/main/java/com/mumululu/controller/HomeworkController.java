package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Homework;
import com.mumululu.service.HomeworkService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RestController
public class HomeworkController {

    private final HomeworkService homeworkService;

    public HomeworkController(HomeworkService homeworkService) {
        this.homeworkService = homeworkService;
    }

    // ========== 作业 CRUD ==========

    @GetMapping("/api/homework/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) Long classId,
                          @RequestParam(required = false) Long courseId) {
        return Result.ok(homeworkService.list(page, pageSize, classId, courseId));
    }

    @GetMapping("/api/homework/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.ok(homeworkService.getById(id));
    }

    @PostMapping("/api/homework")
    public Result<?> create(@RequestBody Homework homework) {
        return Result.ok(homeworkService.create(homework));
    }

    @PutMapping("/api/homework/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Homework homework) {
        homework.setId(id);
        homeworkService.update(homework);
        return Result.ok();
    }

    @DeleteMapping("/api/homework/{id}")
    public Result<?> delete(@PathVariable Long id) {
        homeworkService.delete(id);
        return Result.ok();
    }

    @GetMapping("/api/homework/{homeworkId}/submissions")
    public Result<?> submissions(@PathVariable Long homeworkId) {
        return Result.ok(homeworkService.getSubmissions(homeworkId));
    }

    // ========== 提交与批改 ==========

    @PostMapping("/api/homework-submission")
    public Result<?> submit(@RequestBody Map<String, Object> body) {
        Long homeworkId = toLong(body.get("homeworkId"));
        Long studentId = toLong(body.get("studentId"));
        String submitTimeStr = (String) body.get("submitTime");

        if (homeworkId == null || studentId == null) {
            return Result.fail("参数错误");
        }
        if (submitTimeStr == null || submitTimeStr.isEmpty()) {
            return Result.fail("请填写提交时间");
        }

        LocalDateTime submitTime = LocalDateTime.parse(submitTimeStr,
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        return Result.ok(homeworkService.submit(homeworkId, studentId, submitTime));
    }

    @PutMapping("/api/homework-submission/{id}/grade")
    public Result<?> grade(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Object scoreVal = body.get("score");
        String comment = (String) body.get("comment");
        if (scoreVal == null) {
            return Result.fail("分数不能为空");
        }
        BigDecimal score = BigDecimal.valueOf(((Number) scoreVal).doubleValue());
        homeworkService.grade(id, score, comment);
        return Result.ok();
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return null;
    }
}
