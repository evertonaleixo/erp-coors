package br.com.lima.erpcoors.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private AccessDeniedHandler accessDeniedHandler;
    
    @Autowired
	private DataSource dataSource;

    // roles admin allow to access /admin/**
    // roles user allow to access /user/**
    // custom 403 access denied handler
    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .authorizeRequests()
					.antMatchers("/usuarios/**").hasAnyRole("ADMIN")
					.antMatchers("/**").hasAnyRole("USER", "ADMIN")
					.anyRequest().authenticated()
                .and()
                .formLogin()
					.loginPage("/login")
					.permitAll()
					.and()
                .logout()
					.permitAll()
					.and()
                .exceptionHandling().accessDeniedHandler(accessDeniedHandler);
    }

    // create two users, admin and user
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
    	auth.jdbcAuthentication().dataSource(dataSource)
    			.passwordEncoder(passwordEncoder())
    			.usersByUsernameQuery(
    					"select username,password, enabled from users where username=?")
    			.authoritiesByUsernameQuery(
    					"select username, role from user_roles where username=?");

    	/**
		CREATE  TABLE users (username VARCHAR(45) NOT NULL , password VARCHAR(100) NOT NULL , enabled TINYINT NOT NULL DEFAULT 1 , PRIMARY KEY (username));
		CREATE TABLE user_roles (user_role_id int(11) NOT NULL AUTO_INCREMENT, username varchar(45) NOT NULL, role varchar(45) NOT NULL, PRIMARY KEY (user_role_id), UNIQUE KEY uni_username_role (role,username), KEY fk_username_idx (username), CONSTRAINT fk_username FOREIGN KEY (username) REFERENCES users (username));
  
		INSERT INTO users(username,password,enabled) VALUES ('admin','$2a$10$gmDcQ6O6VelEImVNNrTSfukVoF9C4qgUpmEqY9CFyq0n6Xeftv3Jq', true);
		INSERT INTO user_roles (username, role)	VALUES ('admin', 'ROLE_ADMIN');
    	**/
    }
    
	@Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
}