package ua.kr.accounting.configurations;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ua.kr.accounting.services.CustomUserDetailsService;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    private final CustomUserDetailsService customUserDetailsService;

    @Autowired
    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder(8);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authenticationManager(
                http.getSharedObject(AuthenticationManagerBuilder.class)
                        .userDetailsService(customUserDetailsService)
                        .passwordEncoder(passwordEncoder())
                        .and()
                        .build());

        http
                .csrf()
                .disable()
                .authorizeRequests()
                .requestMatchers("/", "/registration", "/login").permitAll()
                .anyRequest().authenticated()
                .and()
                .formLogin((form) -> form
                        .loginPage("/login")
                        .permitAll()
                        .defaultSuccessUrl("/incomeExpense")
                )
                .logout((logout) -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .invalidateHttpSession(true)
                        .deleteCookies("JSESSIONID")
                        .permitAll());

        return http.build();
    }
}
