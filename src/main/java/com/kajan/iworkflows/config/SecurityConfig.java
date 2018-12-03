package com.kajan.iworkflows.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Collections;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Bean
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public FilterRegistrationBean corsFilterRegistrationBean() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.applyPermitDefaultValues();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Collections.singletonList("*"));
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setAllowedMethods(Collections.singletonList("*"));
        config.setExposedHeaders(Collections.singletonList("content-length"));
        config.setExposedHeaders(Collections.singletonList("content-type"));
        config.setExposedHeaders(Collections.singletonList("access-control-allow-origin"));
        config.setMaxAge(3600L);
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeRequests()
                .antMatchers("/login")
                .permitAll()
                .antMatchers(HttpMethod.OPTIONS, "/login", "/register").permitAll()
                .anyRequest().authenticated()
                .and().csrf().disable()
                .logout().logoutUrl("/api/logout");
        //http.headers().frameOptions().disable();
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

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().requestMatchers(CorsUtils::isPreFlightRequest);
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    //@Bean
    //@Profile("testing")
    //WebSecurityConfigurerAdapter testing() {
    //    return new WebSecurityConfigurerAdapter() {
    //        @Override
    //        protected void configure(HttpSecurity http) throws Exception {
    //            http
    //                    .httpBasic()
    //                    .and()
    //                    .authorizeRequests()
    //                    .antMatchers("/login")
    //                    .permitAll()
    //                    .antMatchers(HttpMethod.OPTIONS, "/login", "/register").permitAll()
    //                    .anyRequest().authenticated()
    //                    .and().csrf().disable()
    //                    .logout().logoutUrl("/api/logout");
    //            //http.headers().frameOptions().disable();
    //            //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    //            // TODO: Kajan, turn CSRF protection on
    //        }
    //
    //        @Override
    //        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //            auth.inMemoryAuthentication()
    //                    .passwordEncoder(passwordEncoder())
    //                    .withUser("kajan")
    //                    .password(passwordEncoder().encode("kajan"))
    //                    .roles("USER")
    //                    .and()
    //                    .withUser("admin")
    //                    .password(passwordEncoder().encode("admin"))
    //                    .roles("ADMIN")
    //                    .and()
    //                    .withUser("kasthuri")
    //                    .password(passwordEncoder().encode("kasthuri"))
    //                    .roles("USER")
    //                    .and()
    //                    .withUser("ramiya")
    //                    .password(passwordEncoder().encode("ramiya"))
    //                    .roles("USER")
    //                    .and()
    //                    .withUser("kirisanth")
    //                    .password(passwordEncoder().encode("kirisanth"))
    //                    .roles("USER")
    //                    .and()
    //                    .withUser("shadhini")
    //                    .password(passwordEncoder().encode("shadhini"))
    //                    .roles("USER");
    //        }
    //
    //        @Override
    //        public void configure(WebSecurity web) {
    //            web.ignoring().requestMatchers(CorsUtils::isPreFlightRequest);
    //        }
    //
};

//@Bean
//@Profile("!testing")
//public WebSecurityConfigurerAdapter ldap() {
//    return new WebSecurityConfigurerAdapter() {
//
//        @SuppressWarnings("Duplicates")
//        @Override
//        protected void configure(HttpSecurity http) throws Exception {
//            http
//                    .httpBasic()
//                    .and()
//                    .authorizeRequests()
//                    .antMatchers("/login")
//                    .permitAll()
//                    .antMatchers(HttpMethod.OPTIONS, "/login", "/register").permitAll()
//                    .anyRequest().authenticated()
//                    .and().csrf().disable()
//                    .logout().logoutUrl("/api/logout");
//            http.headers().frameOptions().disable();
//            //.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
//            // TODO: Kajan, turn CSRF protection on
//        }
//
//        @Override
//        public void configure(WebSecurity web) {
//            web.ignoring().requestMatchers(CorsUtils::isPreFlightRequest);
//        }
//
//        @Override
//        public void configure(AuthenticationManagerBuilder auth) throws Exception {
//            auth
//                    .ldapAuthentication()
//                    .userDnPatterns("cn={0},ou=users")
//                    .groupSearchFilter("member={0}")
//                    .contextSource()
//                    .url("ldap://iworkflows.projects.mrt.ac.lk:389/dc=iworkflows,dc=projects,dc=mrt,dc=ac,dc=lk");
//        }
//    };
//}

