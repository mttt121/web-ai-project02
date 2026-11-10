package org.example.controller;


import org.example.pojo.*;
import org.example.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequestMapping("/clazzs")
@RestController
public class ClazzController {
    @Autowired
    private ClazzService clazzService;

    @GetMapping()
    public Result page(ClazzQueryParam calzzQueryParam){
        PageResult<Clazz> pageResult = clazzService.page(calzzQueryParam);
        return Result.success(pageResult);
    }



    @GetMapping("/list")
    public Result listAll(){
        List<Clazz> clazzList = clazzService.listAll();
        return Result.success(clazzList);
    }

    @PostMapping()
    public Result add(@RequestBody Clazz clazz){
        clazzService.add(clazz);
        return Result.success();
    }


    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Integer id){
        clazzService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/{id}")
    public Result get(@PathVariable Integer id) {
        Clazz clazz = clazzService.getById(id);
        return Result.success(clazz);
    }

    @PutMapping()
    public Result update(@RequestBody Clazz clazz){
        clazzService.update(clazz);
        return Result.success();
    }

}
