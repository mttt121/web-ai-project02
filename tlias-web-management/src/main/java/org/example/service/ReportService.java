package org.example.service;

import org.example.pojo.ClazzOption;
import org.example.pojo.DegreeOptionCount;
import org.example.pojo.GenderOptionCount;
import org.example.pojo.JobOption;

import java.util.List;

public interface ReportService {
    JobOption getEmpJobData();

    List<GenderOptionCount> getEmpGenderData();


    ClazzOption getStudentCountData();

    List<DegreeOptionCount> getStudentDegreeData();
}
