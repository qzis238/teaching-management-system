package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Score;
import com.mumululu.service.ScoreService;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/score")
public class ScoreController {

    private final ScoreService scoreService;

    public ScoreController(ScoreService scoreService) {
        this.scoreService = scoreService;
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam Long classId,
                          @RequestParam Long courseId,
                          @RequestParam(required = false) String examType) {
        return Result.ok(scoreService.list(page, pageSize, classId, courseId, examType));
    }

    @PostMapping("/batch")
    @SuppressWarnings("unchecked")
    public Result<?> batchSave(@RequestBody Map<String, Object> body) {
        Long courseId = toLong(body.get("courseId"));
        String examType = (String) body.get("examType");
        String examDate = (String) body.get("examDate");
        List<Map<String, Object>> recordsList = (List<Map<String, Object>>) body.get("records");

        if (courseId == null) {
            return Result.fail("请选择课程");
        }
        if (examType == null || examType.isEmpty()) {
            return Result.fail("请填写考试类型");
        }
        if (examDate == null || examDate.isEmpty()) {
            return Result.fail("请选择考试日期");
        }

        List<Score> records = new ArrayList<>();
        if (recordsList != null) {
            for (Map<String, Object> item : recordsList) {
                Score score = new Score();
                score.setStudentId(toLong(item.get("studentId")));
                Object scoreVal = item.get("score");
                if (scoreVal != null) {
                    score.setScore(BigDecimal.valueOf(((Number) scoreVal).doubleValue()));
                }
                records.add(score);
            }
        }

        return Result.ok(scoreService.batchSave(courseId, examType, examDate, records));
    }

    @PutMapping("/{id}")
    public Result<?> updateScore(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Object scoreVal = body.get("score");
        if (scoreVal == null) {
            return Result.fail("分数不能为空");
        }
        BigDecimal score = BigDecimal.valueOf(((Number) scoreVal).doubleValue());
        scoreService.updateScore(id, score);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        scoreService.delete(id);
        return Result.ok();
    }

    @GetMapping("/student/{studentId}")
    public Result<?> getByStudentId(@PathVariable Long studentId) {
        return Result.ok(scoreService.getByStudentId(studentId));
    }

    private Long toLong(Object value) {
        if (value == null) return null;
        if (value instanceof Long) return (Long) value;
        if (value instanceof Integer) return ((Integer) value).longValue();
        return null;
    }
}
