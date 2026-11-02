package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Attendance;
import com.mumululu.mapper.AttendanceMapper;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AttendanceService {

    private final AttendanceMapper attendanceMapper;

    public AttendanceService(AttendanceMapper attendanceMapper) {
        this.attendanceMapper = attendanceMapper;
    }

    public List<Map<String, Object>> getStudents(Long classId, String attendanceDate) {
        return attendanceMapper.findStudentsWithStatus(classId, attendanceDate);
    }

    public Map<String, Object> batchSave(Long classId, String attendanceDate, List<Attendance> records) {
        if (records == null || records.isEmpty()) {
            throw new BusinessException("考勤记录不能为空");
        }
        for (Attendance record : records) {
            if (record.getStudentId() == null) {
                throw new BusinessException("学生ID不能为空");
            }
            Integer status = record.getStatus();
            if (status == null || status < 1 || status > 4) {
                throw new BusinessException("出勤状态值无效，只能是1-4");
            }
        }
        attendanceMapper.batchInsertOrUpdate(classId, attendanceDate, records);
        Map<String, Object> result = new HashMap<>();
        result.put("savedCount", records.size());
        return result;
    }

    public void updateStatus(Long id, Integer status) {
        if (status == null || status < 1 || status > 4) {
            throw new BusinessException("出勤状态值无效，只能是1-4");
        }
        attendanceMapper.updateStatus(id, status);
    }

    public Map<String, Object> list(int page, int pageSize, Long classId,
                                     String startDate, String endDate) {
        int offset = (page - 1) * pageSize;
        List<Attendance> list = attendanceMapper.list(offset, pageSize, classId, startDate, endDate);
        long total = attendanceMapper.count(classId, startDate, endDate);
        Map<String, Object> result = new HashMap<>();
        result.put("total", total);
        result.put("list", list);
        return result;
    }
}
