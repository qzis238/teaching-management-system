package com.mumululu.service;

import com.mumululu.mapper.TeacherMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class TeacherService {

    private final TeacherMapper teacherMapper;

    public TeacherService(TeacherMapper teacherMapper) {
        this.teacherMapper = teacherMapper;
    }

    public List<Map<String, Object>> list() {
        return teacherMapper.listTeachers();
    }
}
