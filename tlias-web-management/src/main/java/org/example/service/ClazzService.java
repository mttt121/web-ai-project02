package org.example.service;

import org.example.pojo.*;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ClazzService {

    PageResult<Clazz> page(ClazzQueryParam calzzQueryParam);

    List<Clazz> listAll();




    void add(Clazz clazz);

    void deleteById(Integer id);

    Clazz getById(Integer id);

    void update(Clazz clazz);

}
