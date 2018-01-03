package br.com.lima.erpcoors.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;

@Configuration
// @EnableWebMvc --> Not use this annotation. If you use, the WebMVC will force
// static-files resource configuration, and you lose access.
public class MvcConfiguration extends WebMvcConfigurerAdapter {
	private boolean dev = false;
	
	@Override
	public void addResourceHandlers(ResourceHandlerRegistry registry) {
		registry.addResourceHandler("/static").addResourceLocations("classpath:/static/");
	}

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		LocaleChangeInterceptor localeChangeInterceptor = new LocaleChangeInterceptor();
		localeChangeInterceptor.setParamName("lang");
		registry.addInterceptor(localeChangeInterceptor);
	}

	@Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
		
		
		String usrName = "lphhmajmocspmx";
		String pwd = "457580ed808a085833e476a147dec1d043ac27b1fff7fcb29196e04d4042be5d";
		String url = "jdbc:postgresql://ec2-54-235-85-127.compute-1.amazonaws.com:5432/de1lr14lshg058";
		String driver = "org.postgresql.Driver";

		if(dev) {
			usrName = "root";
			pwd = "test";
			url = "jdbc:mysql://localhost/coors";
			driver = "com.mysql.jdbc.Driver";
		}
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driver);
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(usrName);
		driverManagerDataSource.setPassword(pwd);
		return driverManagerDataSource;
	}
}