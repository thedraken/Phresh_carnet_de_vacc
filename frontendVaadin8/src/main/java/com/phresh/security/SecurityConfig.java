package com.phresh.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.provisioning.UserDetailsManager;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final PhreshUserDetailsManager userDetailsManager;

    @Autowired
    public SecurityConfig(PhreshUserDetailsManager userDetailsManager) {
        this.userDetailsManager = userDetailsManager;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        super.configure(http);
//        setLoginView(http, LoginView.class);
//    }

    @Bean
    public UserDetailsManager userDetailsManager() {
        return userDetailsManager;
    }

//    @Bean
//    public VaadinServiceInitListener loginConfigurer() {
//        return (serviceInitEvent) -> {
//            RouteConfiguration routeConfiguration = RouteConfiguration.forApplicationScope();
//            routeConfiguration.setRoute(LoginView.LOGIN_PATH, LoginView.class);
//        };
//    }

    @Bean
    protected DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider(userDetailsManager());
        daoAuthenticationProvider.setPasswordEncoder(userDetailsManager.getPasswordEncryptor());
        return daoAuthenticationProvider;
    }

    @Bean
    public ProviderManager providerManager() {
        return new ProviderManager(daoAuthenticationProvider());
    }

    @Bean
    public AuthenticationManager authManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder authenticationManagerBuilder =
                http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(daoAuthenticationProvider());
        return authenticationManagerBuilder.build();
    }
}
