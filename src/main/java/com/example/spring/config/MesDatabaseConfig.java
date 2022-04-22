package com.example.passwordinitializer.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
@MapperScan(value="com.example.passwordinitializer.mapper.mes", sqlSessionFactoryRef="mesSqlSessionFactory")
public class MesDatabaseConfig {

    @Bean(name="mesDataSource")
    @ConfigurationProperties(prefix="spring.mes.datasource")
    public DataSource mesDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="mesSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("mesDataSource") DataSource mesDataSource) throws Exception{

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(mesDataSource);
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/mes/*.xml"));
        return sessionFactory.getObject();

    }

    @Bean(name="mesSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("mesSqlSessionFactory") SqlSessionFactory mesSqlSessionFactory) throws Exception{
        return new SqlSessionTemplate(mesSqlSessionFactory);
    }

}
