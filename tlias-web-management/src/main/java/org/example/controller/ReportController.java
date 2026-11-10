package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.pojo.ClazzOption;
import org.example.pojo.DegreeOptionCount;
import org.example.pojo.JobOption;
import org.example.pojo.Result;
import org.example.service.ReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequestMapping("/report")
@RestController
public class ReportController {

    @Autowired
    private ReportService reportService;

    @GetMapping("/empJobData")
    public Result getEmpJobData(){
        log.info("统计员工人数");
         JobOption jobOption = reportService.getEmpJobData();
         return Result.success(jobOption);
        }


        @GetMapping("/empGenderData")
        public Result getEmpGenderData(){
            log.info("统计员工性别");
            // 前端直接把这个数组塞进饼图的 series.data，所以不能包成 {genderList, dataList}
            return Result.success(reportService.getEmpGenderData());
        }


        @GetMapping("/studentCountData")
        public Result getStudentCountData(){
            log.info("统计学生人数");
            ClazzOption clazzOption = reportService.getStudentCountData();
            return Result.success(clazzOption);
        }

        @GetMapping("/studentDegreeData")
        public Result getStudentDegreeData(){
            log.info("统计学生学历");
            return Result.success(reportService.getStudentDegreeData());
        }



}
