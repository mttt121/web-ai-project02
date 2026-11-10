package org.example.service.impl;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.example.exception.DeletionNotAllowedException;
import org.example.mapper.ClazzMapper;
import org.example.pojo.Clazz;
import org.example.pojo.ClazzQueryParam;
import org.example.pojo.Emp;
import org.example.pojo.PageResult;
import org.example.service.ClazzService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;



@Service
public class ClazzServiceImpl implements ClazzService {
    @Autowired
    private ClazzMapper clazzMapper;



    @Override
    public PageResult<Clazz> page(ClazzQueryParam calzzQueryParam) {
        PageHelper.startPage(calzzQueryParam.getPage(), calzzQueryParam.getPageSize());
        //2. 执行查询
        List<Clazz> clazzList = clazzMapper.list(calzzQueryParam);
        LocalDate today = LocalDate.now();
        for (Clazz c : clazzList) {
            if (today.isBefore(c.getBeginDate())) {
                c.setStatus("未开班");
            } else if (today.isAfter(c.getEndDate())) {
                c.setStatus("已结课");
            } else {
                c.setStatus("在读");
            }
        }
        //3. 封装分页结果
        Page<Clazz> p = (Page<Clazz>)clazzList;
        return new PageResult(p.getTotal(), p.getResult());
    }



    @Override
    public List<Clazz> listAll() {
        return clazzMapper.listAll();
    }


    @Override
    public void add(Clazz clazz) {
        clazz.setCreateTime(LocalDateTime.now());
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.add(clazz);
    }



    @Override
    public void deleteById(Integer id) {
        // 班级下还有学生时，不允许删除
        Integer count = clazzMapper.countStudentByClazzId(id);
        if (count != null && count > 0) {
            throw new DeletionNotAllowedException("对不起, 该班级下有学生, 不能直接删除");
        }
        clazzMapper.deleteById(id);
    }

    @Override
    public Clazz getById(Integer id) {
        return clazzMapper.getById(id);
    }

    @Override
    public void update(Clazz clazz) {
        clazz.setUpdateTime(LocalDateTime.now());
        clazzMapper.update(clazz);
    }
}
