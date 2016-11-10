package it.ifttt.configuration;

import javax.persistence.EntityManagerFactory;

import org.apache.commons.dbcp.BasicDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@ComponentScan(basePackages={"it.ifttt"})
@EnableTransactionManagement
@EnableJpaRepositories("it.ifttt")
@PropertySource("classpath:application.properties")
public class RootConfig {
	@Autowired
	private Environment environment;
	
	
	@Bean
	public BasicDataSource dataSource() {		
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(environment.getProperty("database.driver"));
		ds.setUrl(environment.getProperty("database.url"));
		ds.setUsername(environment.getProperty("database.username"));
		ds.setPassword(environment.getProperty("database.password"));

		return ds;
	}
	
	@Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(BasicDataSource dataSource) {
     
            HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
            vendorAdapter.setDatabase(Database.MYSQL);
            vendorAdapter.setGenerateDdl(false);
 
            LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
            factory.setJpaVendorAdapter(vendorAdapter);
            factory.setPackagesToScan("it.ifttt.model");
            factory.setDataSource(dataSource);
            return factory;
    }
   
   
    @Bean
    public PlatformTransactionManager transactionManager(EntityManagerFactory factory) {
 
            JpaTransactionManager txManager = new JpaTransactionManager();
            txManager.setEntityManagerFactory(factory);
            return txManager;
 
    }
}
