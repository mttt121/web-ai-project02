package org.example.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private Integer id;
    private String name;
    private String no;
    private Integer gender;
    private String phone;
    private Integer degree;
    private String idCard;
    private Integer isCollege;
    private String address;
    private LocalDate graduationDate;
    private Integer violationCount;
    private Integer violationScore;
    private Integer clazzId;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private String clazzName;
}
