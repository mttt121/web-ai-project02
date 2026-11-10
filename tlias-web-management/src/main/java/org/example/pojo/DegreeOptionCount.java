package org.example.pojo;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DegreeOptionCount {

    private String name;   // 学历名称，如：本科
    private Integer value; // 人数
}
