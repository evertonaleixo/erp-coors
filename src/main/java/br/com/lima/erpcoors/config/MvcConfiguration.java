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
		String usrName = "root";
		String pwd = "test";
		String url = "jdbc:mysql://localhost/coors";
		String driver = "com.mysql.jdbc.Driver";
		
		DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
		driverManagerDataSource.setDriverClassName(driver);
		driverManagerDataSource.setUrl(url);
		driverManagerDataSource.setUsername(usrName);
		driverManagerDataSource.setPassword(pwd);
		return driverManagerDataSource;
	}
}