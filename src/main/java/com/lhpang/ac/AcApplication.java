package com.lhpang.ac;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

/**
 * 描 述: //TODO Fuck BoHai University
 *
 * @date: 2019-04-24 9:57
 * @author: lhpang
 * @param:
 * @return:
 **/
@SpringBootApplication
@MapperScan("com.lhpang.ac.dao")
public class AcApplication {

    public static void main(String[] args) {
        SpringApplication.run(AcApplication.class, args);
    }

}
