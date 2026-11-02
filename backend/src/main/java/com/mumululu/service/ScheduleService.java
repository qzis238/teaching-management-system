package com.mumululu.service;

import com.mumululu.common.BusinessException;
import com.mumululu.entity.Schedule;
import com.mumululu.mapper.ScheduleMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ScheduleService {

    private static final String[] DAY_NAMES = {"", "周一", "周二", "周三", "周四", "周五"};

    private final ScheduleMapper scheduleMapper;

    public ScheduleService(ScheduleMapper scheduleMapper) {
        this.scheduleMapper = scheduleMapper;
    }

    public List<Schedule> getByClassId(Long classId) {
        return scheduleMapper.findByClassId(classId);
    }

    public Map<String, Object> create(Schedule schedule) {
        validate(schedule);

        int classConflict = scheduleMapper.countByClassDayPeriod(
                schedule.getClassId(), schedule.getDayOfWeek(), schedule.getPeriod(), null);
        if (classConflict > 0) {
            throw new BusinessException("该时间段已有课程安排");
        }

        checkTeacherConflict(schedule.getTeacherId(), schedule.getDayOfWeek(),
                schedule.getPeriod(), null);

        scheduleMapper.insert(schedule);
        Map<String, Object> result = new HashMap<>();
        result.put("id", schedule.getId());
        return result;
    }

    public void update(Schedule schedule) {
        if (schedule.getId() == null) {
            throw new BusinessException("参数错误");
        }
        if (schedule.getCourseId() == null) {
            throw new BusinessException("请选择课程");
        }
        if (schedule.getTeacherId() == null) {
            throw new BusinessException("请选择授课老师");
        }
        scheduleMapper.update(schedule);
    }

    public void delete(Long id) {
        scheduleMapper.deleteById(id);
    }

    @Transactional
    public void copy(Long sourceClassId, Long targetClassId) {
        long sourceCount = scheduleMapper.countByClassId(sourceClassId);
        if (sourceCount == 0) {
            throw new BusinessException("源班级无排课数据");
        }
        long targetCount = scheduleMapper.countByClassId(targetClassId);
        if (targetCount > 0) {
            throw new BusinessException("目标班级已有排课数据，请先清空后再复制");
        }

        List<Schedule> sourceSchedules = scheduleMapper.findByClassIdRaw(sourceClassId);
        for (Schedule s : sourceSchedules) {
            Schedule copy = new Schedule();
            copy.setClassId(targetClassId);
            copy.setCourseId(s.getCourseId());
            copy.setTeacherId(s.getTeacherId());
            copy.setDayOfWeek(s.getDayOfWeek());
            copy.setPeriod(s.getPeriod());
            scheduleMapper.insert(copy);
        }
    }

    public Map<String, Object> checkConflict(Long teacherId, Integer dayOfWeek,
                                              Integer period, Long excludeId) {
        int count = scheduleMapper.countByTeacherDayPeriod(teacherId, dayOfWeek, period, excludeId);
        Map<String, Object> result = new HashMap<>();
        result.put("conflict", count > 0);
        return result;
    }

    private void checkTeacherConflict(Long teacherId, Integer dayOfWeek,
                                       Integer period, Long excludeId) {
        int conflict = scheduleMapper.countByTeacherDayPeriod(teacherId, dayOfWeek, period, excludeId);
        if (conflict > 0) {
            String dayName = DAY_NAMES[dayOfWeek];
            throw new BusinessException("该老师在" + dayName + "第" + period + "节已有其他课程安排");
        }
    }

    private void validate(Schedule schedule) {
        if (schedule.getClassId() == null) {
            throw new BusinessException("请选择班级");
        }
        if (schedule.getCourseId() == null) {
            throw new BusinessException("请选择课程");
        }
        if (schedule.getTeacherId() == null) {
            throw new BusinessException("请选择授课老师");
        }
        if (schedule.getDayOfWeek() == null || schedule.getDayOfWeek() < 1 || schedule.getDayOfWeek() > 5) {
            throw new BusinessException("请选择正确的星期（1-5）");
        }
        if (schedule.getPeriod() == null || schedule.getPeriod() < 1 || schedule.getPeriod() > 8) {
            throw new BusinessException("请选择正确的节次（1-8）");
        }
    }
}
