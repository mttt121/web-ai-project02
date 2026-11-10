package org.example.mapper;


import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.example.pojo.EmpExpr;

import java.util.List;

@Mapper
public interface EmpExprMapper {
    
    
    void insertBatch(@Param("exprList") List<EmpExpr> exprList);


    void deleteByEmpIds(List<Integer> empIds);

//    @Select("select * from emp_expr where emp_id = #{id}")
//    List<EmpExpr> selectByEmpId(Integer id);

}