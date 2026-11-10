package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import lombok.extern.slf4j.Slf4j;
import org.example.mapper.EmpExprMapper;
import org.example.mapper.EmpMapper;
import org.example.pojo.*;
import org.example.service.EmpService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.example.utils.JwtUtils;


@Slf4j
@Service
public class EmpServiceImpl implements EmpService {
    @Autowired
    private EmpMapper empMapper;
    @Autowired
    private EmpExprMapper empExprMapper;

//    @Override
//    public PageResult<Emp> page(Integer page, Integer pageSize) {
//
//        Long total = empMapper.count();
//        List<Emp> rows = empMapper.list((page - 1) * pageSize, pageSize);
//
//        return new PageResult<Emp>(total, rows);
//
//    }

    @Override
    public PageResult<Emp> page(EmpQueryParam empQueryParam) {
        //1. 设置分页参数
        PageHelper.startPage(empQueryParam.getPage(), empQueryParam.getPageSize());

        //2. 执行查询
        List<Emp> empList = empMapper.list(empQueryParam);
        Page<Emp> p = (Page<Emp>) empList;

        //3. 封装结果
        return new PageResult(p.getTotal(), p.getResult());
    }


    @Override
    public List<Emp> listAll() {
        return empMapper.listAll();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void save(Emp emp) {
        emp.setCreateTime(LocalDateTime.now());
        emp.setUpdateTime(LocalDateTime.now());
        empMapper.insert(emp);


        List<EmpExpr> exprList = emp.getExprList();
        if (exprList != null && exprList.size() > 0) {
            //设置empId
            exprList.forEach(expr -> expr.setEmpId(emp.getId()));
            empExprMapper.insertBatch(exprList);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void deleteByIds(List<Integer> ids) {
        empMapper.deleteByIds(ids);
        empExprMapper.deleteByEmpIds(ids);
    }

//    @Override
//    public Emp getById(Integer id) {
//        return empMapper.selectById(id);
//    }
//
//
//    @Override
//    public List<EmpExpr> getExprById(Integer id) {
//        return empExprMapper.selectByEmpId(id);
//    }

    @Override
    public Emp getInfo(Integer id) {


        return empMapper.getById(id);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void update(Emp emp) {
        emp.setUpdateTime(LocalDateTime.now());

        empMapper.updateById(emp);
        empExprMapper.deleteByEmpIds(Arrays.asList(emp.getId()));


       List<EmpExpr> exprList = emp.getExprList();
       if (exprList != null && exprList.size() > 0) {
           //设置empId
           exprList.forEach(expr -> expr.setEmpId(emp.getId()));
           empExprMapper.insertBatch(exprList);
       }
    }

    @Override
    public LoginInfo login(Emp emp) {
        Emp e = empMapper.selectByUsernameAndPassword(emp);
        if(e != null){

            log.info("员工信息为:{}", e);

            //生成token
            Map<String, Object> claims = new HashMap<>();
            claims.put("id", e.getId());
            claims.put("username", e.getUsername());
            String token = JwtUtils.generate(claims);
            return new LoginInfo(e.getId(), e.getUsername(), e.getPassword(), e.getName(), token);
        }
        return null;
    }
}