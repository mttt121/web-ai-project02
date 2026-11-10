package org.example.controller;


import org.example.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.example.service.LogService;
import org.example.pojo.Result;
import org.example.pojo.PageResult;



@RestController
@RequestMapping("/log")
public class LogController {
    @Autowired
    private LogService logService;

    @GetMapping("/page")
    public Result getLogPage(Operate_LogQueryParam param) {
        PageResult<OperateLog> pageResult = logService.page(param);
        return Result.success(pageResult);
    }
}

