package com.example.inkzone.config;


import com.example.inkzone.repository.UserRepository;
import com.example.inkzone.service.ApplicationUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;


@Configuration

public class SecurityConfiguration {
    private final UserRepository userRepository;

    public SecurityConfiguration(UserRepository userRepository) {
        this.userRepository = userRepository;
    }



    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                .antMatchers("/").permitAll()
                .antMatchers( "/users/login", "/users/register", "/forgot_password", "/reset_password").anonymous()
                .antMatchers("/users/logout").authenticated()
                .antMatchers("/gallery/","/gallery/add","/calendar", "/stock/**" , "/users", "/users/edit", "users/edit/firstName", "/api/**").authenticated()
                .antMatchers("/tasks").hasRole("MODERATOR")
                .antMatchers("/admin","/admin/**").hasRole("ADMIN")
                .and()
                .formLogin()
                .loginPage("/users/login")
                .usernameParameter("email")
                .passwordParameter("password")
                .defaultSuccessUrl("/")
                .failureForwardUrl("/users/login?error=true")
                .and()
                .logout()
                .logoutUrl("/users/logout")
                .clearAuthentication(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl("/")
                .and()
                .rememberMe()
                .rememberMeParameter("remember-me")
                .key("uniqueAndSecret")
                .rememberMeCookieName("rememberMeCookieName")
                .tokenValiditySeconds(86400)
                .userDetailsService(userDetailsService(userRepository));




        return http.build();
    }



    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();

    }

    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository){
        return new ApplicationUserDetailsService(userRepository);
    }








}
