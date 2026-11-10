package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.pojo.*;
import org.example.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;


@RequestMapping("/emps")
@Slf4j
@RestController
public class EmpController {
    @Autowired
    private EmpService empService;

    @GetMapping()
    public Result page(EmpQueryParam empQueryParam){

        log.info("分页查询员工",empQueryParam);
        PageResult<Emp> pageResult = empService.page(empQueryParam);
        return Result.success(pageResult);
    }

    @GetMapping("/list")
    public Result listAll(){
        List<Emp> empList = empService.listAll();
        return Result.success(empList);
    }


    @PostMapping()
    public Result save(@RequestBody Emp emp){

        log.info("添加员工",emp);
        empService.save(emp);
        return Result.success();
    }

    @DeleteMapping
    public Result delete(@RequestParam List<Integer> ids){
        log.info("批量删除员工:{}",ids);
        empService.deleteByIds(ids);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result getInfo(@PathVariable Integer id){
        log.info("根据员工id查询员工:{}",id);
//        Emp emp = empService.getById(id);
//        emp.setDeptName(empService.getDeptName(id));
//        emp.setExprList(empService.getExprById(id));

        Emp emp = empService.getInfo(id);
        return Result.success(emp);
    }

    @PutMapping()
    public Result update(@RequestBody Emp emp){
        log.info("更新员工",emp);
        empService.update(emp);
        return Result.success();
    }


}
