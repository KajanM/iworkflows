package com.kajan.iworkflows.config;

import com.kajan.iworkflows.service.impl.UserDetailsServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static constants.SecurityConstants.SIGN_UP_URL;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    //// @formatter:off
    //@Override
    //protected void configure(HttpSecurity http) throws Exception {
    //    http
    //            .authorizeRequests()
    //                .antMatchers(
    //                        "/js/**",
    //                        "/css/**",
    //                        "/img/**",
    //                        "/webjars/**", "/h2/**", "/dist/**", "/vendor/**", "/favicon.ico").permitAll()
    //                .antMatchers("/user/**").hasRole("USER")
    //                .anyRequest().authenticated()
    //            .and()
    //            .formLogin().permitAll()
    //            .and()
    //            .csrf().disable()
    //            .headers()
    //            .frameOptions().disable()
    //            .and()
    //            .logout().logoutUrl("/logout");
    //}
    //// @formatter:on

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    //    auth.inMemoryAuthentication()
    //            .passwordEncoder(passwordEncoder())
    //            .withUser("kajan")
    //            .password(passwordEncoder().encode("kajan"))
    //            .roles("USER")
    //            .and()
    //            .withUser("admin")
    //            .password(passwordEncoder().encode("admin"))
    //            .roles("ADMIN")
    //            .and()
    //            .withUser("Kasthuri")
    //            .password(passwordEncoder().encode("kachu123"))
    //            .roles("USER");
    //
    //
    //}

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

    //@Override
    //protected void configure(AuthenticationManagerBuilder auth)
    //        throws Exception {
    //    auth
    //            .inMemoryAuthentication()
    //            .withUser("user")
    //            .password(passwordEncoder().encode("password"))
    //            .roles("USER");
    //}
    //
    //@Override
    //protected void configure(HttpSecurity http)
    //        throws Exception {
    //    http.csrf().disable()
    //            .authorizeRequests()
    //            .antMatchers("/login").permitAll()
    //            .anyRequest()
    //            .authenticated()
    //            .and()
    //            .httpBasic();
    //}
    //
    //@Bean
    //public PasswordEncoder passwordEncoder() {
    //    return new BCryptPasswordEncoder();
    //}

    private UserDetailsServiceImpl userDetailsService;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(UserDetailsServiceImpl userDetailsService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDetailsService = userDetailsService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL).permitAll()
                .anyRequest().authenticated()
                .and()
                .addFilter(new JwtAuthenticationFilter(authenticationManager()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager()))
                // this disables session creation on Spring Security
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

}
