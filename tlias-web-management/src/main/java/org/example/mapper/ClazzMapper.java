package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.Clazz;
import org.example.pojo.ClazzQueryParam;

import java.util.List;




@Mapper
public interface ClazzMapper {

    List<Clazz> list(ClazzQueryParam calzzQueryParam);

    @Select("select * from clazz")
    List<Clazz> listAll();


    @Insert("insert into clazz(name,room, begin_date, end_date,master_id, subject) " +
            "values(#{name}, #{room}, #{beginDate}, #{endDate}, #{masterId}, #{subject})")
    void add(Clazz clazz);

    @Delete("delete from clazz where id = #{id}")
    void deleteById(Integer id);

    @Select("select * from clazz where id = #{id}")
    Clazz getById(Integer id);

    @Select("select count(*) from student where clazz_id = #{id}")
    Integer countStudentByClazzId(Integer id);



    @Update("update clazz set name = #{name}, room = #{room}," +
            " begin_date = #{beginDate}, end_date = #{endDate}, " +
            "master_id = #{masterId}, subject = #{subject}," +
            " update_time = #{updateTime} where id = #{id}")
    void update(Clazz clazz);

}
