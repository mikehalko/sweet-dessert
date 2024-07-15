package ru.defezis.sweetdessert.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.defezis.sweetdessert.dto.CreateUserRequest;
import ru.defezis.sweetdessert.model.User;

import java.util.List;

import static ru.defezis.sweetdessert.enums.UserRole.USER;

@Slf4j
@RequiredArgsConstructor
@Service
public class UserService {

    private final InMemoryUserDetailsManager userDetailsManager;
    private final PasswordEncoder passwordEncoder;

    public String save(CreateUserRequest request) {
        User newUser = new User(request.getUsername(), request.getPassword(), List.of(USER.getUserRole()),
                true, true, true, true);
        userDetailsManager.createUser(newUser);
        return newUser.getUsername();
    }
}
