package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Dept;
import org.example.pojo.Result;
import org.example.service.DeptService;
import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.slf4j.LoggerFactory;
import org.example.anno.Log;


@Slf4j
@RequestMapping("/depts")
@RestController
public class DeptController {
    //private static final Logger log = LoggerFactory.getLogger(DeptController.class);
    @Autowired
    private DeptService deptService;

    //@RequestMapping(value = "/depts",method = RequestMethod.GET)
    @GetMapping
    public Result list(){
        //System.out.println("查询全部部门数据");
        log.info("查询全部部门数据");
        List<Dept> deptList = deptService.findAll();

        return Result.success(deptList);
    }

    @Log
    @DeleteMapping
    public Result delete(Integer id){
        log.info("删除部门:{}",id);
        deptService.deleteById(id);
        //System.out.println("删除部门:"+id);
        return Result.success();
    }
    @Log
    @PostMapping
    public Result add(@RequestBody Dept dept){
        log.info("添加部门:{}",dept);
        deptService.add(dept);
       // System.out.println("添加部门:"+dept);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id){
        log.info("查询部门:{}",id);
        //System.out.println("查询部门:"+id);
        Dept dept = deptService.getById(id);
        return Result.success(dept);
    }
    @Log
    @PutMapping
    public Result update(@RequestBody Dept dept){
        log.info("更新部门:{}",dept);
        //System.out.println("更新部门:"+dept);
        deptService.update(dept);
        return Result.success();
    }
}
