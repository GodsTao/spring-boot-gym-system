package com.juntao.gymsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.juntao.gymsystem.usermanagement.mapper")
public class GymsystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(GymsystemApplication.class, args);
    }

}
