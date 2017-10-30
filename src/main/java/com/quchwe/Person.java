package com.quchwe;

import java.util.Date;


public class Person {

    private Long id;

    private String name;
    private Date createTime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Person() {
        this.name = "qucher";
        this.createTime = new Date();
        System.out.println(createTime.getTime());
    }
}
