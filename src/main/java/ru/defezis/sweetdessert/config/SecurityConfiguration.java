package ru.defezis.sweetdessert.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.context.annotation.RequestScope;
import ru.defezis.sweetdessert.service.DessertRestService;

import static org.springframework.security.config.Customizer.withDefaults;
import static ru.defezis.sweetdessert.enums.UserRole.USER;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

//    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/desserts", "/desserts/**").hasRole(USER.getName())
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

    @Bean
    public SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(authorizeRequests -> authorizeRequests.anyRequest().authenticated())
                .oauth2Login(oauth2Login -> oauth2Login.loginPage("/oauth2/authorization/sweet-dessert-client"))
                .oauth2Client(withDefaults());
        return http.build();
    }

    @Bean
    @RequestScope
    public DessertRestService ingredientService(OAuth2AuthorizedClientService clientService) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String accessToken = null;
        if (authentication.getClass().isAssignableFrom(OAuth2AuthenticationToken.class)) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String clientRegistrationId = oauthToken.getAuthorizedClientRegistrationId();

            if (clientRegistrationId.equals("sweet-dessert-client")) {
                OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(clientRegistrationId, oauthToken.getName());
                accessToken = client.getAccessToken().getTokenValue();
            }
        }
        return new DessertRestService(accessToken);
    }
}
