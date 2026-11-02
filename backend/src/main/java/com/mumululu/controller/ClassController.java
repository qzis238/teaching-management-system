package com.mumululu.controller;

import com.mumululu.common.Result;
import com.mumululu.entity.Class;
import com.mumululu.service.ClassService;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @GetMapping("/list")
    public Result<?> list(@RequestParam(defaultValue = "1") int page,
                          @RequestParam(defaultValue = "10") int pageSize,
                          @RequestParam(required = false) String className) {
        Map<String, Object> data = classService.list(page, pageSize, className);
        return Result.ok(data);
    }

    @GetMapping("/options")
    public Result<?> options() {
        return Result.ok(classService.options());
    }

    @GetMapping("/{id}/check")
    public Result<?> checkDelete(@PathVariable Long id) {
        return Result.ok(classService.checkDelete(id));
    }

    @GetMapping("/{id}")
    public Result<?> getById(@PathVariable Long id) {
        return Result.ok(classService.getById(id));
    }

    @PostMapping
    public Result<?> create(@RequestBody Class clazz) {
        Map<String, Object> data = classService.create(clazz);
        return Result.ok(data);
    }

    @PutMapping("/{id}")
    public Result<?> update(@PathVariable Long id, @RequestBody Class clazz) {
        clazz.setId(id);
        classService.update(clazz);
        return Result.ok();
    }

    @DeleteMapping("/{id}")
    public Result<?> delete(@PathVariable Long id) {
        classService.delete(id);
        return Result.ok();
    }
}
