package it.ifttt.configuration;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan("it.ifttt")
@PropertySource("classpath:application.properties")
public class RootConfig {
	
}
