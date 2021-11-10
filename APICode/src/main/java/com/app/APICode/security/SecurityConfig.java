package com.app.APICode.security;

import java.util.Arrays;

import com.app.APICode.security.jwt.JWTAuthenticationFilter;
import com.app.APICode.security.jwt.JWTAuthorizationFilter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
@Configuration
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true, jsr250Enabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    private UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(UserDetailsService userSvc) {
        this.userDetailsService = userSvc;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().headers().disable().httpBasic().disable().authorizeRequests()

                .antMatchers(HttpMethod.GET, "/users").hasRole("ADMIN")
                .antMatchers(HttpMethod.GET, "/users/*").hasAnyRole("ADMIN", "BUSINESS", "EMPLOYEE", "USER")
                .antMatchers(HttpMethod.PUT, "/users", "/users/*").hasAnyRole("ADMIN", "BUSINESS", "EMPLOYEE", "USER")
                .antMatchers(HttpMethod.DELETE, "/users/*").hasAnyRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/users").hasRole("ADMIN")

                .antMatchers(HttpMethod.GET, "/notifications").hasAnyRole("ADMIN", "BUSINESS", "EMPLOYEE", "USER")
                .antMatchers(HttpMethod.PUT, "/notifications", "/notifications/*").hasAnyRole("ADMIN", "BUSINESS", "EMPLOYEE", "USER")

                .antMatchers(HttpMethod.POST, "/refreshToken").permitAll()

                .antMatchers(HttpMethod.GET, "/restaurants", "/restaurants/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/restaurants/**").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.POST, "/restaurants", "/restaurants/**").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.DELETE, "/restaurants/**").hasAnyRole("ADMIN", "BUSINESS")

                .antMatchers(HttpMethod.GET, "/employee").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.POST, "/employee").hasAnyRole("ADMIN", "BUSINESS")

                .antMatchers(HttpMethod.GET, "/measures", "/measures/*").hasAnyRole("ADMIN", "BUSINESS", "EMPLOYEE", "USER")
                .antMatchers(HttpMethod.PUT, "/measures").hasRole("ADMIN")
                .antMatchers(HttpMethod.POST, "/measures").hasRole("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/measures", "/measures/*").hasRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/register").permitAll().antMatchers("/h2-console/**").permitAll()

                .and().addFilter(new JWTAuthenticationFilter(authenticationManager()))
                .addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().formLogin().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.setAllowedOrigins(Arrays.asList("http://localhost:3000"));
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
