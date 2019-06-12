package com.juntao.gymsystem.usermanagement.domain;

import java.io.Serializable;

public class Role implements Serializable {
    private static final long serialVersionUID =1L;

    private Long id;

    private String name;

    public Role(String name) {
        this.name = name;
    }

    public Role(){}

    public Long getId() {
        return id;
    }

    public void setId(Long rid) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
