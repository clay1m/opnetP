package com.morgan.opnet.configuration;

import java.util.Properties;

import javax.sql.DataSource;

import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate4.HibernateTransactionManager;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableTransactionManagement
@ComponentScan({ "com.morgan.opnet.configuration" })
@PropertySource(value = { "classpath:application.properties" })
public class HibernateConfiguration {
	
//	private static final boolean USE_LOCAL_SERVER = false;
	private static final boolean USE_LOCAL_SERVER = true;

    @Autowired
    private Environment environment;

    @Bean
    public LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan(new String[] { "com.morgan.opnet.model" });
        sessionFactory.setHibernateProperties(hibernateProperties());
        return sessionFactory;
     }
    
//    private @Value("${JDBC_CONNECTION_STRING}") String url;
//    private @Value("${AWS_ACCESS_KEY_ID}") String username;
//    private @Value("${AWS_SECRET_KEY}") String password;
    
    @Bean
    public DataSource dataSource() {
//    	DriverManager.registerDriver(new com.mysql.jdbc.Driver ());
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
//        dataSource.setDriverClassName(environment.getRequiredProperty("jdbc.driverClassName"));
        dataSource.setDriverClassName("com.mysql.jdbc.Driver");
        
        if (USE_LOCAL_SERVER) {
        	dataSource.setUrl(environment.getRequiredProperty("jdbc.url.local"));
	        dataSource.setUsername(environment.getRequiredProperty("jdbc.username.local"));
	        dataSource.setPassword(environment.getRequiredProperty("jdbc.password.local"));
        }
	    else {
	    	dataSource.setUrl("jdbc:mysql://opnet.crvbhhzc2wh5.us-east-1.rds.amazonaws.com:3306/opnet?user=clay&password=cdmorga1");
	        dataSource.setUsername("clay");
	        dataSource.setPassword("cdmorga1");
//	        test();
	    }
        return dataSource;
    }
    
    private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        return properties;        
    }
    
	@Bean
    @Autowired
    public HibernateTransactionManager transactionManager(SessionFactory s) {
       HibernateTransactionManager txManager = new HibernateTransactionManager();
       txManager.setSessionFactory(s);
       return txManager;
    }
}

