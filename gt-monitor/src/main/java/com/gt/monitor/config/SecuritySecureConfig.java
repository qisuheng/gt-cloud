package com.gt.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import org.springframework.boot.actuate.web.exchanges.InMemoryHttpExchangeRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import static org.springframework.security.config.Customizer.withDefaults;

/**
 * @description: SecuritySecureConfig
 * @date: 2022/12/21 19:16
 * @author: qisuheng
 */
@Configuration
@EnableWebSecurity
public class SecuritySecureConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        successHandler.setDefaultTargetUrl("/");
        http.authorizeHttpRequests()
                //1.配置所有静态资源和登录页可以公开访问
                .requestMatchers("/assets/**").permitAll()
                .requestMatchers("/login", "/css/**", "/js/**", "/image/*").permitAll()
                .requestMatchers("/**").permitAll()
                //其他所有请求需要登录
                .anyRequest().authenticated()
                .and()
                //2.配置登录和登出路径
                .formLogin().loginPage("/login").successHandler(successHandler).and()
                //登出页面配置，用于替换security默认页面
                .logout().logoutUrl("/logout").and()
                .httpBasic().and()
                .csrf()
                //4.开启基于cookie的csrf保护
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                .ignoringRequestMatchers(
                        "/instances",
                        "/actuator/**"
                );
        return http.build();
    }

}
