package org.example.service;

import org.example.pojo.PageResult;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;

import java.util.List;


public interface StudentService {
    PageResult<Student> page(StudentQueryParam studentQueryParam);


    void add(Student student);

    Student getById(Integer id);

    void update(Student student);

    void deleteById(List<Integer> ids);

    void updateViolationScore(Integer id, Integer score);
}
