package com.skoneczny;


import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter {

	@Autowired
	private DataSource dataSource;

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource)
		.usersByUsernameQuery("select email as principal, password as credentails, true from user where email=?")
		.authoritiesByUsernameQuery("select user_email as principal, role_name as role from user_roles where user_email=?")
		.passwordEncoder(passwordEncoder()).rolePrefix("ROLE_");  
		
	}
   
	@Bean
	public PasswordEncoder passwordEncoder() {
		// TODO Auto-generated method stub
		return new BCryptPasswordEncoder();
	}	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception{  	

//
//		http.authorizeRequests().antMatchers("/register", "/", "/about", "/login", "/css/**", "/webjars/**").permitAll()
//		.anyRequest().authenticated().and().formLogin().loginPage("/login").permitAll()
//		.defaultSuccessUrl("/profile").and().logout().logoutSuccessUrl("/login");
//		
		
				http.authorizeRequests().antMatchers("/register", "/", "/about", "/login", "/css/**", "/webjars/**","/webjars/").permitAll()
		    	.antMatchers("/profile").hasAnyRole("USER,ADMIN")
		    	.antMatchers("/personalSettings").hasAnyRole("USER,ADMIN")
		    	.antMatchers("/addTask").hasAnyRole("USER,ADMIN")
				.antMatchers("/users").hasRole("ADMIN")
				.and().formLogin().loginPage("/login").permitAll()
				.defaultSuccessUrl("/profile").and().logout().logoutSuccessUrl("/login");


		//    	
		//        http.authorizeRequests()
		//        .anyRequest().permitAll();
	}
	@Bean
	public LayoutDialect layoutDialect() {
		return new LayoutDialect();
	}	
}
