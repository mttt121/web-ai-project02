package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.mapper.StudentMapper;
import org.example.pojo.*;
import org.example.service.StudentService;

import java.time.LocalDateTime;
import java.util.List;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Override
    public PageResult<Student> page(StudentQueryParam studentQueryParam) {
        PageHelper.startPage(studentQueryParam.getPage(), studentQueryParam.getPageSize());
        //2. 执行查询
        List<Student> students = studentMapper.page(studentQueryParam);
        //3. 封装分页结果
        Page<Student> p = (Page<Student>)students;
        return new PageResult(p.getTotal(), p.getResult());
    }

    @Override
    public void add(Student student) {
        // 违纪字段前端表单不传，数据库又是 NOT NULL，必须给默认值
        if (student.getViolationCount() == null) {
            student.setViolationCount(0);
        }
        if (student.getViolationScore() == null) {
            student.setViolationScore(0);
        }
        student.setCreateTime(LocalDateTime.now());
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.insert(student);
    }


    @Override
    public Student getById(Integer id) {
        return studentMapper.selectById(id);
    }

    @Override
    public void update(Student student) {
        student.setUpdateTime(LocalDateTime.now());
        studentMapper.update(student);
    }

    @Override
    public void deleteById(List<Integer> ids) {
        studentMapper.deleteById(ids);
    }

    @Override
    public void updateViolationScore(Integer id, Integer score) {
        studentMapper.updateViolationScore(id, score);
    }
}
