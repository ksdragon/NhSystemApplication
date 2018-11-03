package com.skoneczny;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import nz.net.ultraq.thymeleaf.LayoutDialect;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration  extends WebSecurityConfigurerAdapter{
    @Override
    protected void configure(HttpSecurity http) throws Exception{
    	
    	
    	
//    	http.authorizeRequests().antMatchers("/register", "/", "/about", "/login", "/css/**", "/webjars/**").permitAll();
//		.antMatchers("/profile").hasAnyRole("USER,ADMIN")
//		.antMatchers("/users","/addTask").hasRole("ADMIN")
//		.and().formLogin().loginPage("/login").permitAll()
//		.defaultSuccessUrl("/profile").and().logout().logoutSuccessUrl("/login");

    	
    	
        http.authorizeRequests()
        .anyRequest().permitAll();
    }
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect();
    }
}
