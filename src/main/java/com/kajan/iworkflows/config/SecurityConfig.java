package com.kajan.iworkflows.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/login", "/register").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable();
        //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // TODO: Kajan, turn CSRF protection on
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(passwordEncoder())
                .withUser("kajan")
                .password(passwordEncoder().encode("kajan"))
                .roles("USER")
                .and()
                .withUser("admin")
                .password(passwordEncoder().encode("admin"))
                .roles("ADMIN")
                .and()
                .withUser("kasthuri")
                .password(passwordEncoder().encode("kasthuri"))
                .roles("USER")
                .and()
                .withUser("ramiya")
                .password(passwordEncoder().encode("ramiya"))
                .roles("USER")
                .and()
                .withUser("kirisanth")
                .password(passwordEncoder().encode("kirisanth"))
                .roles("USER")
                .and()
                .withUser("shadhini")
                .password(passwordEncoder().encode("shadhini"))
                .roles("USER");
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Override
//    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth
//                .ldapAuthentication()
//                .userDnPatterns("cn={0},ou=users")
//                .groupSearchFilter("member={0}")
//                .contextSource()
//                .url("ldap://iworkflows.projects.mrt.ac.lk:389/dc=iworkflows,dc=projects,dc=mrt,dc=ac,dc=lk");
//    }

}
