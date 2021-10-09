package com.app.APICode.security;

import com.app.APICode.security.jwt.JWTAuthenticationFilter;
import com.app.APICode.security.jwt.JWTAuthorizationFilter;
import com.app.APICode.security.jwt.JWTHelper;

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
    private JWTHelper jwtHelper;

    @Autowired
    public SecurityConfig(UserDetailsService userSvc, JWTHelper jwtHelper) {
        this.userDetailsService = userSvc;
        this.jwtHelper = jwtHelper;
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(encoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable().headers().disable().httpBasic().disable().authorizeRequests()

                .antMatchers(HttpMethod.GET, "/users", "users/*").hasRole("ADMIN")
                .antMatchers(HttpMethod.PUT, "/users", "users/*").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.DELETE, "users/*").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.POST, "/users").hasAnyRole("ADMIN")

                .antMatchers(HttpMethod.POST, "/refreshToken").permitAll()

                .antMatchers(HttpMethod.GET, "/restaurants", "/restaurants/**").permitAll()
                .antMatchers(HttpMethod.PUT, "/restaurants/**").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.POST, "/restaurants", "/restaurants/**").hasAnyRole("ADMIN", "BUSINESS")
                .antMatchers(HttpMethod.DELETE, "/restaurants/**").hasAnyRole("ADMIN", "BUSINESS")

                .antMatchers(HttpMethod.POST, "/register").permitAll().antMatchers("/h2-console/**").permitAll()

                .anyRequest().authenticated().and()

                .addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtHelper))
                .addFilterBefore(new JWTAuthorizationFilter(jwtHelper), UsernamePasswordAuthenticationFilter.class)

                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)

                .and().formLogin().disable();
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }

    @Bean
    public BCryptPasswordEncoder encoder() {
        // auto-generate a random salt internally
        return new BCryptPasswordEncoder();
    }
}
