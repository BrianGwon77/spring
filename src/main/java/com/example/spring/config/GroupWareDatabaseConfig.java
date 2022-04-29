package com.example.spring.config;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import javax.sql.DataSource;

@Configuration
@PropertySource("classpath:/application.properties")
@MapperScan(value="com.example.spring.mapper.groupware", sqlSessionFactoryRef="groupwareSqlSessionFactory")
public class GroupWareDatabaseConfig {

    @Bean(name="groupwareDataSource")
    @ConfigurationProperties(prefix="spring.groupware.datasource")
    /** application.properties 참조 **/
    public DataSource db1DataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name="groupwareSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactory(@Qualifier("groupwareDataSource") DataSource groupwareDataSource) throws Exception{

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

        final SqlSessionFactoryBean sessionFactory = new SqlSessionFactoryBean();
        sessionFactory.setDataSource(groupwareDataSource);
        sessionFactory.setMapperLocations(resolver.getResources("classpath:mappers/groupware/*.xml"));
        return sessionFactory.getObject();

    }

    @Bean(name="groupwareSqlSessionTemplate")
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("groupwareSqlSessionFactory") SqlSessionFactory groupwareSqlSessionFactory) throws Exception {
        return new SqlSessionTemplate(groupwareSqlSessionFactory);
    }

}
