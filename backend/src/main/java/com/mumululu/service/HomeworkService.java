package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Homework;
import com.mumululu.entity.HomeworkSubmission;
import com.mumululu.mapper.HomeworkMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class HomeworkService {

    private final HomeworkMapper homeworkMapper;

    public HomeworkService(HomeworkMapper homeworkMapper) {
        this.homeworkMapper = homeworkMapper;
    }

    public Map<String, Object> list(int page, int pageSize, Long classId, Long courseId) {
        int offset = (page - 1) * pageSize;
        List<Homework> list = homeworkMapper.list(offset, pageSize, classId, courseId);
        long total = homeworkMapper.count(classId, courseId);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    public Homework getById(Long id) {
        Homework homework = homeworkMapper.findById(id);
        if (homework == null) {
            throw new BusinessException(404, "作业不存在");
        }
        return homework;
    }

    public Map<String, Object> create(Homework homework) {
        validate(homework);
        if (homework.getMaxScore() == null) {
            homework.setMaxScore(100);
        }
        homeworkMapper.insert(homework);
        Map<String, Object> result = new HashMap<>();
        result.put("id", homework.getId());
        return result;
    }

    public void update(Homework homework) {
        if (homework.getId() == null) {
            throw new BusinessException("参数错误");
        }
        getById(homework.getId());
        if (homework.getTitle() == null || homework.getTitle().trim().isEmpty()) {
            throw new BusinessException("请填写作业标题");
        }
        if (homework.getDeadline() == null) {
            throw new BusinessException("请选择截止时间");
        }
        if (homework.getMaxScore() == null) {
            homework.setMaxScore(100);
        }
        homeworkMapper.update(homework);
    }

    @Transactional
    public void delete(Long id) {
        getById(id);
        homeworkMapper.deleteSubmissionsByHomeworkId(id);
        homeworkMapper.deleteById(id);
    }

    public List<Map<String, Object>> getSubmissions(Long homeworkId) {
        Homework homework = getById(homeworkId);
        return homeworkMapper.findSubmissionsByHomeworkId(homeworkId, homework.getClassId());
    }

    public Map<String, Object> submit(Long homeworkId, Long studentId, LocalDateTime submitTime) {
        if (studentId == null) {
            throw new BusinessException("学生ID不能为空");
        }
        if (submitTime == null) {
            throw new BusinessException("提交时间不能为空");
        }
        Homework homework = getById(homeworkId);

        int submitStatus = submitTime.isAfter(homework.getDeadline()) ? 2 : 1;

        HomeworkSubmission submission = new HomeworkSubmission();
        submission.setHomeworkId(homeworkId);
        submission.setStudentId(studentId);
        submission.setSubmitStatus(submitStatus);
        submission.setSubmitTime(submitTime);
        homeworkMapper.upsertSubmission(submission);

        Map<String, Object> result = new HashMap<>();
        result.put("id", submission.getId());
        result.put("submitStatus", submitStatus);
        return result;
    }

    public void grade(Long id, BigDecimal score, String comment) {
        if (score == null) {
            throw new BusinessException("分数不能为空");
        }
        HomeworkSubmission submission = homeworkMapper.findSubmissionById(id);
        if (submission == null) {
            throw new BusinessException(404, "提交记录不存在");
        }
        Homework homework = getById(submission.getHomeworkId());
        BigDecimal maxScore = BigDecimal.valueOf(homework.getMaxScore());
        if (score.compareTo(BigDecimal.ZERO) < 0) {
            throw new BusinessException("分数不能为负数");
        }
        if (score.compareTo(maxScore) > 0) {
            throw new BusinessException("得分不能超过满分" + homework.getMaxScore() + "分");
        }
        homeworkMapper.gradeSubmission(id, score, comment);
    }

    private void validate(Homework homework) {
        if (homework.getTitle() == null || homework.getTitle().trim().isEmpty()) {
            throw new BusinessException("请填写作业标题");
        }
        if (homework.getClassId() == null) {
            throw new BusinessException("请选择班级");
        }
        if (homework.getCourseId() == null) {
            throw new BusinessException("请选择课程");
        }
        if (homework.getDeadline() == null) {
            throw new BusinessException("请选择截止时间");
        }
    }
}
