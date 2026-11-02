package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Course;
import com.mumululu.entity.Score;
import com.mumululu.mapper.CourseMapper;
import com.mumululu.mapper.ScoreMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScoreService {

    private final ScoreMapper scoreMapper;
    private final CourseMapper courseMapper;

    public ScoreService(ScoreMapper scoreMapper, CourseMapper courseMapper) {
        this.scoreMapper = scoreMapper;
        this.courseMapper = courseMapper;
    }

    public Map<String, Object> list(int page, int pageSize, Long classId,
                                     Long courseId, String examType) {
        int offset = (page - 1) * pageSize;
        List<Score> list = scoreMapper.list(offset, pageSize, classId, courseId, examType);
        long total = scoreMapper.count(classId, courseId, examType);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    public Map<String, Object> batchSave(Long courseId, String examType,
                                          String examDate, List<Score> records) {
        if (records == null || records.isEmpty()) {
            throw new BusinessException("成绩记录不能为空");
        }
        Course course = courseMapper.findById(courseId);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        BigDecimal maxScore = BigDecimal.valueOf(course.getMaxScore());

        for (Score record : records) {
            if (record.getStudentId() == null) {
                throw new BusinessException("学生ID不能为空");
            }
            if (record.getScore() == null) {
                throw new BusinessException("分数不能为空");
            }
            if (record.getScore().compareTo(BigDecimal.ZERO) < 0) {
                throw new BusinessException("分数不能为负数");
            }
            if (record.getScore().compareTo(maxScore) > 0) {
                throw new BusinessException("分数不能超过满分" + course.getMaxScore() + "分");
            }
        }

        scoreMapper.batchInsertOrUpdate(courseId, examType, examDate, records);
        Map<String, Object> result = new HashMap<>();
        result.put("savedCount", records.size());
        return result;
    }

    public void updateScore(Long id, BigDecimal score) {
        if (score == null) {
            throw new BusinessException("分数不能为空");
        }
        Score existing = scoreMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "成绩记录不存在");
        }
        Course course = courseMapper.findById(existing.getCourseId());
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        BigDecimal maxScore = BigDecimal.valueOf(course.getMaxScore());
        if (score.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("分数不能为负数");
        }
        if (score.compareTo(maxScore) > 0) {
            throw new BusinessException("分数不能超过满分" + course.getMaxScore() + "分");
        }
        scoreMapper.updateScore(id, score);
    }

    public void delete(Long id) {
        Score existing = scoreMapper.findById(id);
        if (existing == null) {
            throw new BusinessException(404, "成绩记录不存在");
        }
        scoreMapper.deleteById(id);
    }

    public List<Score> getByStudentId(Long studentId) {
        return scoreMapper.findByStudentId(studentId);
    }
}
