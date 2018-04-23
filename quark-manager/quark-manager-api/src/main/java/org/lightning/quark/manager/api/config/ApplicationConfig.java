package org.lightning.quark.manager.api.config;

import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.type.TypeHandlerRegistry;
import org.lightning.quark.manager.api.filter.RepeatableBodyRequestFilter;
import org.lightning.quark.manager.dao.common.JsonTypeHandler;
import org.lightning.quark.manager.model.dto.quark.ConnectPoolProperties;
import org.lightning.quark.manager.model.dto.quark.ConnectProperties;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * Created by cook on 2018/4/23
 */
@Configuration
public class ApplicationConfig {

    private static final Logger logger = LoggerFactory.getLogger(ApplicationConfig.class);

//    @Bean
//    @Primary
//    @ConfigurationProperties(prefix = "spring.datasource")
//    public HikariDataSource primaryDataSource() {
//        return DataSourceBuilder.create().type(HikariDataSource.class).build();
//    }

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSourceProperties dataSourceProperties() {
        return new DataSourceProperties();
    }

    @Bean
    @ConfigurationProperties("spring.datasource")
    public HikariDataSource dataSource(DataSourceProperties properties) {
        return properties.initializeDataSourceBuilder().type(HikariDataSource.class)
                .build();
    }

    @Bean
    public RepeatableBodyRequestFilter repeatableBodyRequestFilter() {
        return new RepeatableBodyRequestFilter();
    }

    @Bean
    public FilterRegistrationBean repeatableBodyFilter() {
        FilterRegistrationBean<RepeatableBodyRequestFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(repeatableBodyRequestFilter());
        registration.addUrlPatterns("/*");
        registration.setName("repeatableBodyFilter");
        registration.setOrder(2);
        return registration;
    }

    @Bean
    public ConfigurationCustomizer mybatisConfigurationCustomizer() {
        return configuration -> {
            TypeHandlerRegistry registry = configuration.getTypeHandlerRegistry();

            registry.register(ConnectProperties.class, new JsonTypeHandler<>(ConnectProperties.class));
            registry.register(ConnectPoolProperties.class, new JsonTypeHandler<>(ConnectPoolProperties.class));

            logger.info("customize type handlers done");
        };
    }

    @Bean
    public CommandLineRunner commandLineRunner(ApplicationContext ctx) {
        return args -> {
            logger.info("inspect the beans size is {}", ctx.getBeanDefinitionCount());
        };
    }


}
