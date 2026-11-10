package org.example.controller;


import org.example.pojo.PageResult;
import org.example.pojo.Result;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;
import org.example.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/students")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @GetMapping()
    public Result page(StudentQueryParam studentQueryParam) {
        PageResult<Student> pageResult = studentService.page(studentQueryParam);
        return Result.success(pageResult);
    }

    @PostMapping()
    public Result add(@RequestBody Student student) {
        studentService.add(student);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getById(@PathVariable Integer id) {
        Student student = studentService.getById(id);
        return Result.success(student);
    }

    @PutMapping()
    public Result update(@RequestBody Student student) {
        studentService.update(student);
        return Result.success();
    }
    @DeleteMapping("/{ids}")
    public Result deleteById(@PathVariable List<Integer> ids) {
        studentService.deleteById(ids);
        return Result.success();
    }

    @PutMapping("/violation/{id}/{score}")
    public Result updateViolationScore(@PathVariable Integer id, @PathVariable Integer score) {
        studentService.updateViolationScore(id, score);
        return Result.success();
    }
}

























