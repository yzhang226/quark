package org.lightning.quark.manager.api.main;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Created by cook on 2018/4/23
 */
@EnableSwagger2
@MapperScan(basePackages = "org.lightning.quark.manager.dao.daos")
@SpringBootApplication(scanBasePackages = "org.lightning.quark.manager")
@PropertySource("classpath:application.yml")
public class ManagerApiStart {

    public static void main(String[] args) throws Exception {
        SpringApplication.run(ManagerApiStart.class, args);
    }

}
