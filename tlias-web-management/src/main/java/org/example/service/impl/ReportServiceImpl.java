package org.example.service.impl;

import org.example.mapper.EmpMapper;
import org.example.pojo.ClazzOption;
import org.example.pojo.DegreeOptionCount;
import org.example.pojo.GenderOptionCount;
import org.example.pojo.JobOption;
import org.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import org.example.mapper.StudentMapper;


@Service
public class ReportServiceImpl implements ReportService {


    @Autowired
    private EmpMapper empMapper;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public JobOption getEmpJobData(){
        List<Map<String, Object>> list = empMapper.countEmpJobData();
        List<Object> jobList = list.stream().map(dataMap-> dataMap.get("pos")).toList();
        List<Object> dataList = list.stream().map(dataMap-> dataMap.get("num")).toList();

        return new JobOption(jobList,dataList);
    }

    @Override
    public List<GenderOptionCount> getEmpGenderData(){
        // 前端饼图要的是 [{name:"男",value:26}, {name:"女",value:3}]，直接返回列表
        return empMapper.countEmpGenderData();
    }

    @Override
    public ClazzOption getStudentCountData(){
        List<Map<String, Object>> list = studentMapper.countStudentCountData();
        List<Object> clazzList = list.stream().map(dataMap-> dataMap.get("clazz")).toList();
        List<Object> dataList = list.stream().map(dataMap-> dataMap.get("num")).toList();

        return new ClazzOption(clazzList,dataList);
    }

    @Override
    public List<DegreeOptionCount> getStudentDegreeData(){
        // 前端饼图要的是 [{name:"本科", value:8}, ...]，SQL 里直接用别名映射，不用再拆两个数组
        return studentMapper.countStudentDegreeData();
    }
}
