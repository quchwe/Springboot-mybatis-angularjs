package com.quchwe;

import com.quchwe.mapper.PersonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/test")
public class TestController {
    @Autowired
    private PersonMapper personMapper;

    @RequestMapping("/list")
    public List<Person> personList() {
        return personMapper.findAllList();
    }
}
