package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.mapper.OperateLogMapper;
import org.example.pojo.OperateLog;
import org.example.pojo.Operate_LogQueryParam;
import org.example.pojo.PageResult;
import org.example.service.LogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
public class LogServiceImpl implements LogService {
    @Autowired
    private OperateLogMapper operateLogMapper;
    @Override
    public PageResult<OperateLog> page(Operate_LogQueryParam param) {
        PageHelper.startPage(param.getPage(), param.getPageSize());

        //2. 执行查询
        List<OperateLog> logList = operateLogMapper.list(param);
        Page<OperateLog> p = (Page<OperateLog>) logList;

        //3. 封装结果
        return new PageResult<>(p.getTotal(), p.getResult());
    }
}
