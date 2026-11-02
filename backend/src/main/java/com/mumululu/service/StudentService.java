package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Student;
import com.mumululu.mapper.StudentMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class StudentService {

    private final StudentMapper studentMapper;

    public StudentService(StudentMapper studentMapper) {
        this.studentMapper = studentMapper;
    }

    public Map<String, Object> list(int page, int pageSize, String name, Long classId) {
        int offset = (page - 1) * pageSize;
        List<Student> list = studentMapper.list(offset, pageSize, name, classId);
        long total = studentMapper.count(name, classId);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    public Student getById(Long id) {
        Student student = studentMapper.findById(id);
        if (student == null) {
            throw new BusinessException(404, "学生不存在");
        }
        return student;
    }

    public Map<String, Object> create(Student student) {
        validate(student);
        assertStudentNoUnique(student.getStudentNo(), null);
        studentMapper.insert(student);
        Map<String, Object> result = new HashMap<>();
        result.put("id", student.getId());
        return result;
    }

    public void update(Student student) {
        if (student.getId() == null) {
            throw new BusinessException("参数错误");
        }
        Student existing = getById(student.getId());
        validate(student);
        assertStudentNoUnique(student.getStudentNo(), student.getId());
        studentMapper.update(student);
    }

    public void delete(Long id) {
        getById(id);
        long pendingCount = studentMapper.countPendingHomework(id);
        if (pendingCount > 0) {
            throw new BusinessException("该学生还有" + pendingCount + "份作业未批改，请确认后重试");
        }
        studentMapper.deleteById(id);
    }

    public Map<String, Object> checkStudentNo(String studentNo, Long excludeId) {
        int count = studentMapper.countByStudentNo(studentNo, excludeId);
        Map<String, Object> result = new HashMap<>();
        result.put("available", count == 0);
        return result;
    }

    private void validate(Student student) {
        if (student.getStudentNo() == null || student.getStudentNo().trim().isEmpty()) {
            throw new BusinessException("请填写学号");
        }
        if (student.getName() == null || student.getName().trim().isEmpty()) {
            throw new BusinessException("请填写姓名");
        }
        if (student.getGender() == null || (student.getGender() != 1 && student.getGender() != 2)) {
            throw new BusinessException("请选择性别");
        }
        if (student.getClassId() == null) {
            throw new BusinessException("请选择班级");
        }
    }

    private void assertStudentNoUnique(String studentNo, Long excludeId) {
        int count = studentMapper.countByStudentNo(studentNo, excludeId);
        if (count > 0) {
            throw new BusinessException("该学号已被占用，请更换");
        }
    }
}
