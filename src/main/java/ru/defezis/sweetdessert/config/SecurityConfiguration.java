package ru.defezis.sweetdessert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static ru.defezis.sweetdessert.enums.UserRole.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/dessert", "/dessert/**").hasRole(USER.getName())
                        .requestMatchers("/token/**").permitAll()
                        .requestMatchers("/login","/login/new", "/user/login/create").permitAll()
                        .anyRequest().authenticated())
                .formLogin(form -> {
                    form.loginPage("/login");
                    form.defaultSuccessUrl("/", true);
                    form.failureUrl("/login?error");
                    form.usernameParameter("username");
                    form.passwordParameter("password");
                });

        return http.build();
    }
}
