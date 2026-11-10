package org.example.mapper;


import org.apache.ibatis.annotations.Select;
import org.example.pojo.OperateLog;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.example.pojo.Operate_LogQueryParam;

import java.util.List;

@Mapper
public interface OperateLogMapper {

    //插入日志数据
    @Insert("insert into operate_log (operate_emp_id, operate_time, class_name, method_name, method_params, return_value, cost_time) " +
            "values (#{operateEmpId}, #{operateTime}, #{className}, #{methodName}, #{methodParams}, #{returnValue}, #{costTime});")
    public void insert(OperateLog log);


    /**
     * 分页查询日志：left join emp 补出操作人姓名，按操作时间倒序。
     * method_params 用 ifnull 兜底，避免前端 .length 报错。
     */
    @Select("select l.id, l.operate_emp_id, l.operate_time, l.class_name, l.method_name, " +
            "ifnull(l.method_params, '') method_params, l.return_value, l.cost_time, " +
            "e.name operate_emp_name " +
            "from operate_log l left join emp e on l.operate_emp_id = e.id " +
            "order by l.operate_time desc, l.id desc")
    List<OperateLog> list(Operate_LogQueryParam param);
}
