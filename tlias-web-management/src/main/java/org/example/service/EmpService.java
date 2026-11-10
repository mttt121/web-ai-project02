package org.example.service;

import org.example.pojo.*;

import java.util.List;


public interface EmpService {

    PageResult<Emp> page(EmpQueryParam empQueryParam);


    List<Emp> listAll();

    void save(Emp emp);


    void deleteByIds(List<Integer> ids);

//    Emp getById(Integer id);
//
//    List<EmpExpr> getExprByEmpId(Integer id);

    Emp getInfo(Integer id);

    void update(Emp emp);


    LoginInfo login(Emp emp);
}
