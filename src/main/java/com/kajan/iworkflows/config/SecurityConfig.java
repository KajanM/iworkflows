package com.kajan.iworkflows.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.LdapShaPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // @formatter:off
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                    .antMatchers(
                            "/js/**",
                            "/css/**",
                            "/img/**",
                            "/webjars/**", "/h2/**").permitAll()
                    .antMatchers("/user/**").hasRole("USER")
                    .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .csrf().disable()
                .headers()
                .frameOptions().disable()
                .and()
                .logout().logoutUrl("/logout");
    }
    // @formatter:on

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
                .roles("ADMIN");
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

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
