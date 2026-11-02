package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Course;
import com.mumululu.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/course")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) String courseName) {
        Map<String, Object> data = courseService.list(page, pageSize, courseName);
        return Result.ok(data);
    }

    @GetMapping("/options")
    public Result<?> options() {
        return Result.ok(courseService.options());
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.ok(courseService.getById(id));
    }

    @PostMapping
    public Result<?> create(@RequestBody Course course) {
        Map<String, Object> data = courseService.create(course);
        return Result.ok(data);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Course course) {
        course.setId(id);
        courseService.update(course);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        courseService.delete(id);
        return Result.ok();
    }
}
