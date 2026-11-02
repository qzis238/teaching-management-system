package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Course;
import com.mumululu.mapper.CourseMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CourseService {

    private final CourseMapper courseMapper;

    public CourseService(CourseMapper courseMapper) {
        this.courseMapper = courseMapper;
    }

    public Map<String, Object> list(int page, int pageSize, String courseName) {
        int offset = (page - 1) * pageSize;
        List<Course> list = courseMapper.list(offset, pageSize, courseName);
        long total = courseMapper.count(courseName);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    public Course getById(Long id) {
        Course course = courseMapper.findById(id);
        if (course == null) {
            throw new BusinessException(404, "课程不存在");
        }
        return course;
    }

    public Map<String, Object> create(Course course) {
        validate(course);
        if (course.getMaxScore() == null) {
            course.setMaxScore(100);
        }
        courseMapper.insert(course);
        Map<String, Object> result = new HashMap<>();
        result.put("id", course.getId());
        return result;
    }

    public void update(Course course) {
        if (course.getId() == null) {
            throw new BusinessException("参数错误");
        }
        getById(course.getId());
        validate(course);
        if (course.getMaxScore() == null) {
            course.setMaxScore(100);
        }
        courseMapper.update(course);
    }

    public void delete(Long id) {
        getById(id);
        courseMapper.deleteById(id);
    }

    public List<Course> options() {
        return courseMapper.options();
    }

    private void validate(Course course) {
        if (course.getCourseName() == null || course.getCourseName().trim().isEmpty()) {
            throw new BusinessException("请填写课程名称");
        }
    }
}
