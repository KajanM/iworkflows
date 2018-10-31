package com.kajan.iworkflows.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated()
                .and().csrf()
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("kajan.14@cse.mrt.ac.lk")
                .password(passwordEncoder().encode("kajan"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .and()
                .withUser("Kasthuri")
                .password(passwordEncoder().encode("kachu123"))
                .roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    //@Override
    //@Profile("ldap")
    //public void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth
    //            .ldapAuthentication()
    //            .userDnPatterns("uid={0},ou=people")
    //            .groupSearchBase("ou=groups")
    //            .contextSource()
    //            .url("ldap://localhost:8389/dc=springframework,dc=org")
    //            .and()
    //            .passwordCompare()
    //            .passwordEncoder(new LdapShaPasswordEncoder())
    //            .passwordAttribute("userPassword");
    //}
}
