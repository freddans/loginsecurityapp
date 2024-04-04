package com.example.basicauthtestproject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    // User Creation
    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder encoder) {

        // InMemoryUserDetailsManager
        UserDetails admin = User.withUsername("admin")
                .password(encoder.encode("admin"))
//                .roles("ADMIN")
                .roles("ADMIN", "USER")
                .build();

        UserDetails user = User.withUsername("user")
                .password(encoder.encode("user"))
                .roles("USER")
                .build();

        return new InMemoryUserDetailsManager(admin, user);
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authConfig -> {
//                    authConfig.requestMatchers("/auth/welcome").permitAll();
                    authConfig.requestMatchers("/auth/user/**").authenticated();
                    authConfig.requestMatchers("/auth/admin/**").authenticated();
                    authConfig.requestMatchers("/auth/site/**").authenticated();
                })
                .formLogin(withDefaults());
//                .csrf(Customizer.withDefaults());

        http
                .authorizeHttpRequests(auth2Config -> {
                    auth2Config.requestMatchers("/auth/welcome").permitAll();
                })
                .csrf(Customizer.withDefaults());
        return http.build();
    }

//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .authorizeHttpRequests(authConfig -> {
//                    authConfig.requestMatchers("/auth/welcome").permitAll();
//                    authConfig.requestMatchers("/auth/user/**").authenticated();
//                    authConfig.requestMatchers("/auth/admin/**").authenticated();
//                    authConfig.requestMatchers("/auth/login").authenticated();
//                    authConfig.requestMatchers("/auth/site/**").permitAll();
//                })
//                .formLogin(loginConfig -> {
//                    loginConfig.loginPage("/login");
//                    loginConfig.permitAll();
//                })
//                .logout(logoutConfig -> {
//                    logoutConfig.logoutSuccessUrl("/logout-success");
//                    logoutConfig.permitAll();
//                });
//
//        return http.build();
//    }

    // Password Encoding
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}