package org.example.controller;


import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Emp;
import org.example.pojo.LoginInfo;
import org.example.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.example.pojo.Result;



@Slf4j
@RestController

public class LoginController {
    @Autowired
    private EmpService empService;
    @PostMapping("/login")
    public Result login(@RequestBody Emp emp) {

        log.info("登录:{}", emp);
        LoginInfo inInfo = empService.login(emp);
        if(inInfo != null){
            return Result.success(inInfo);
        }
        return Result.error("用户名或密码错误");
    }
}
