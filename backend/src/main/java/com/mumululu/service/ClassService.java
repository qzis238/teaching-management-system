package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Class;
import com.mumululu.mapper.ClassMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ClassService {

    private final ClassMapper classMapper;

    public ClassService(ClassMapper classMapper) {
        this.classMapper = classMapper;
    }

    public Map<String, Object> list(int page, int pageSize, String className) {
        int offset = (page - 1) * pageSize;
        List<Class> list = classMapper.list(offset, pageSize, className);
        long total = classMapper.count(className);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }

    public Class getById(Long id) {
        Class clazz = classMapper.findById(id);
        if (clazz == null) {
            throw new BusinessException(404, "班级不存在");
        }
        return clazz;
    }

    public Map<String, Object> create(Class clazz) {
        validate(clazz);
        assertClassNameUnique(clazz.getClassName(), null);
        classMapper.insert(clazz);
        Map<String, Object> result = new HashMap<>();
        result.put("id", clazz.getId());
        return result;
    }

    public void update(Class clazz) {
        if (clazz.getId() == null) {
            throw new BusinessException("参数错误");
        }
        getById(clazz.getId());
        validate(clazz);
        assertClassNameUnique(clazz.getClassName(), clazz.getId());
        classMapper.update(clazz);
    }

    public void delete(Long id) {
        getById(id);
        long studentCount = classMapper.countStudentsByClassId(id);
        if (studentCount > 0) {
            throw new BusinessException("该班级下还有" + studentCount + "名学生，请先将学生转出后再删除");
        }
        long scheduleCount = classMapper.countSchedulesByClassId(id);
        if (scheduleCount > 0) {
            throw new BusinessException("该班级还有未完成的排课，请先清理排课数据");
        }
        classMapper.deleteById(id);
    }

    public Map<String, Object> checkDelete(Long id) {
        long studentCount = classMapper.countStudentsByClassId(id);
        long scheduleCount = classMapper.countSchedulesByClassId(id);
        Map<String, Object> result = new HashMap<>();
        result.put("canDelete", studentCount == 0 && scheduleCount == 0);
        result.put("studentCount", studentCount);
        result.put("scheduleCount", scheduleCount);
        return result;
    }

    public List<Class> options() {
        return classMapper.options();
    }

    private void validate(Class clazz) {
        if (clazz.getClassName() == null || clazz.getClassName().trim().isEmpty()) {
            throw new BusinessException("请填写班级名称");
        }
        if (clazz.getTeacherId() == null) {
            throw new BusinessException("请选择负责老师");
        }
    }

    private void assertClassNameUnique(String className, Long excludeId) {
        int count = classMapper.countByClassName(className, excludeId);
        if (count > 0) {
            throw new BusinessException("该班级名称已存在");
        }
    }
}
