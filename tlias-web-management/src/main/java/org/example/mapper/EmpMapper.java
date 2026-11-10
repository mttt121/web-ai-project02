package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.Emp;
import org.example.pojo.EmpExpr;
import org.example.pojo.EmpQueryParam;
import org.example.pojo.GenderOptionCount;


import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Mapper
public interface EmpMapper {
//    @Select("select count(*) from emp left join dept on emp.dept_id = dept.id")
//    Long count();
//
//    @Select("select * , dept.name as deptName from emp left join dept on emp.dept_id = dept.id " +
//            "order by emp.update_time desc limit #{start},#{pageSize}")
//    List<Emp> list(Integer start, Integer pageSize);



   // @Select("select e.*,d.name deptName from emp e left join dept d on e.dept_id = d.id " +
         //   "where e.name like '%#{name}%' and e.gender = #{gender} and e.entry_date between #{begin} and #{end} " +
           // "order by e.update_time desc")
    List<Emp> list(EmpQueryParam empQueryParam);


    @Select("select *from emp where job = 1")
    List<Emp> listAll();



    @Options(useGeneratedKeys = true, keyProperty = "id")
    @Insert("insert into emp (username,name,gender,phone,job,salary,image,entry_date,dept_id,create_time,update_time) " +
            "values (#{username},#{name},#{gender},#{phone},#{job},#{salary},#{image}," +
            "#{entryDate},#{deptId},#{createTime},#{updateTime})")
    void insert(Emp emp);



    void deleteByIds(List<Integer> ids);

//    @Select("select * from emp e  where e.id = #{id}")
//    Emp selectById(Integer id);

     Emp getById(Integer id);



//     @Update("update emp set username = #{username},name = #{name}," +
//             "gender = #{gender},phone = #{phone},job = #{job}," +
//             "salary = #{salary},image = #{image}," +
//             "entry_date = #{entryDate},dept_id = #{deptId}," +
//             "update_time = #{updateTime} where id = #{id}")
     void updateById(Emp emp);


     List<Map<String, Object>> countEmpJobData();


     List<GenderOptionCount> countEmpGenderData();

     @Select("select id,username,name from emp where username = #{username} and password = #{password}")
     Emp selectByUsernameAndPassword(Emp emp);
}
