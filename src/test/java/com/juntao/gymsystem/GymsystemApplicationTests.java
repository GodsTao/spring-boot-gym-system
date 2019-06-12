package com.juntao.gymsystem;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GymsystemApplicationTests {
    private static final Logger logger = LoggerFactory.getLogger(GymsystemApplicationTests.class);
    @Test
    public void contextLoads() {

        logger.info("测试info");

        String msg1 ="错误1";
        String msg2 ="错误2";
        logger.error("message is:  {}  {}",msg1,msg2);
    }

}
