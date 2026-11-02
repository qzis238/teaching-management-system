package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Student;
import com.mumululu.service.StudentService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/student")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) String name,
                          @RequestParam(required = false) Long classId) {
        Map<String, Object> data = studentService.list(page, pageSize, name, classId);
        return Result.ok(data);
    }

    @GetMapping("/check-student-no")
    public Result<?> checkStudentNo(@RequestParam String studentNo,
                                    @RequestParam(required = false) Long excludeId) {
        return Result.ok(studentService.checkStudentNo(studentNo, excludeId));
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.ok(studentService.getById(id));
    }

    @PostMapping
    public Result<?> create(@RequestBody Student student) {
        Map<String, Object> data = studentService.create(student);
        return Result.ok(data);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Student student) {
        student.setId(id);
        studentService.update(student);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        studentService.delete(id);
        return Result.ok();
    }
}
