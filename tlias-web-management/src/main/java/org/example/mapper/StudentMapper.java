package org.example.mapper;

import org.apache.ibatis.annotations.*;
import org.example.pojo.DegreeOptionCount;
import org.example.pojo.Student;
import org.example.pojo.StudentQueryParam;

import java.util.List;
import java.util.Map;

@Mapper
public interface StudentMapper {


    List<Student> page(StudentQueryParam studentQueryParam);



    @Insert("insert into student(name, no, gender, phone, degree, id_card, is_college, address, graduation_date, violation_count, violation_score, clazz_id, create_time, update_time) " +
            "values(#{name}, #{no}, #{gender}, #{phone}, #{degree}, #{idCard}, #{isCollege}, #{address}, #{graduationDate}, #{violationCount}, #{violationScore}, #{clazzId}, #{createTime}, #{updateTime})")
    void insert(Student student);


    @Select("select s.*, c.name as clazzName from student s left join clazz c on s.clazz_id = c.id where s.id = #{id}")
    Student selectById(Integer id);


    @Update("update student set name = #{name}, no = #{no}, gender = #{gender}, phone = #{phone}, degree = #{degree}, id_card = #{idCard}, is_college = #{isCollege}, address = #{address}, graduation_date = #{graduationDate}, violation_count = #{violationCount}, violation_score = #{violationScore}, clazz_id = #{clazzId}, update_time = #{updateTime} where id = #{id}")
    void update(Student student);


    void deleteById(@Param("ids") List<Integer> ids);


    @Update("update student set violation_score = #{score} where id = #{id}")
    void updateViolationScore(Integer id, Integer score);



    List<Map<String, Object>> countStudentCountData();

    List<DegreeOptionCount> countStudentDegreeData();
}
