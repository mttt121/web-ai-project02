package org.example.exception;


import lombok.extern.slf4j.Slf4j;
import org.example.pojo.Result;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@Slf4j

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    public Result handleException(Exception e){
        log.error("程序出错",e);
        return Result.error("出错了，请联系管理员");
    }

    @ExceptionHandler
    public Result handleDeletionNotAllowedException(DeletionNotAllowedException e){
        log.warn("删除被拒绝：{}", e.getMessage());
        return Result.error(e.getMessage());
    }

    @ExceptionHandler
    public Result handleDuplicateKeyException(DuplicateKeyException e){
        log.error("程序出错",e);
        String message = e.getMessage();
        int i = message.indexOf("Duplicate entry");
        String errMsg  = message.substring(i);
        String[] arr = errMsg.split(" ");
        return Result.error(arr[2]+"已存在");
    }
}
