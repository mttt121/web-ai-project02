package org.example.service;

import org.example.pojo.OperateLog;
import org.example.pojo.Operate_LogQueryParam;
import org.example.pojo.PageResult;

public interface LogService {
    PageResult<OperateLog> page(Operate_LogQueryParam param);
}
