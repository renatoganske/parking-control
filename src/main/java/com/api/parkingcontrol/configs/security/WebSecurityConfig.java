package com.api.parkingcontrol.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    UserDetailsServiceImpl userDetailsService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .httpBasic()
                .and()
                .authorizeHttpRequests()
                .antMatchers(HttpMethod.GET,"/parking-spot/**").authenticated()
                .antMatchers(HttpMethod.POST,"/parking-spot/**").hasRole("USER")
                .antMatchers(HttpMethod.DELETE,"/parking-spot/**").hasRole("ADMIN")
//                //permite todos os acessos via requisição http
//                .anyRequest().permitAll();
                //exige a autentição em todas as requisições http
                .anyRequest().authenticated()
                .and()
                //desabilita proteção dos endpoints com métodos construtivos/destrutivos
                //se a proteção csrf for habilitada, é necessário incluir essa autorização no header da requisição
                .csrf().disable();
    }

    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());

    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
