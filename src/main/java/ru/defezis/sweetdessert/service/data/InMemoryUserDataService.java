package ru.defezis.sweetdessert.service.data;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.stereotype.Service;
import ru.defezis.sweetdessert.enums.UserRole;
import ru.defezis.sweetdessert.model.User;
import ru.defezis.sweetdessert.service.exception.UserAlreadyExist;
import ru.defezis.sweetdessert.service.exception.UserNotFound;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import static ru.defezis.sweetdessert.enums.UserRole.*;

// TODO race condition
@Slf4j
@Service
public class InMemoryUserDataService extends InMemoryUserDetailsManager {

    private final PasswordEncoder passwordEncoder;
    private final Map<String, User> userMap;

    public InMemoryUserDataService(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
        this.userMap = new HashMap<>(Map.of(
                "admin", makeUser("admin", "admin", ADMIN, USER),
                "user", makeUser("user", "user", USER)));
    }

    @Override
    public void createUser(UserDetails user) {
        if (!userMap.containsKey(user.getUsername())) {
            userMap.put(user.getUsername(), makeNewUser(user));
            log.info("Created user: {}", user.getUsername());
        } else {
            throw new UserAlreadyExist(user.getUsername());
        }
    }

    @Override
    public void updateUser(UserDetails user) {
        if (userMap.containsKey(user.getUsername())) { // TODO race condition
            UserDetails stored = userMap.remove(user.getUsername());
            userMap.put(user.getUsername(), makeUpdated(stored, user));
            log.info("Updated user: {}", user.getUsername());
        } else {
            throw new UserNotFound(user.getUsername());
        }
    }

    @Override
    public void deleteUser(String username) {
        if (userMap.containsKey(username)) { // TODO race condition
            userMap.remove(username);
            log.info("Removed user: {}", username);
        } else {
            throw new UserNotFound(username);
        }
    }

    @Override
    public boolean userExists(String username) {
        return userMap.containsKey(username);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userMap.get(username);
    }

    private User makeNewUser(UserDetails user) {
        return makeUser(user.getUsername(), user.getPassword(), UserRole.USER);
    }

    private User makeUser(String username, String password, UserRole... userRoles) {
        return makeUser(username, password,
                Stream.of(userRoles).map(UserRole::getUserRole).toList(),
                true, true, true, true);
    }

    private User makeUser(String username, String password, Collection<? extends GrantedAuthority> userRoles,
                          boolean accountNonExpired, boolean  accountNonLocked,
                          boolean  credentialsNonExpired, boolean enabled) {
        return new User(username, passwordEncoder.encode(password), userRoles,
                accountNonExpired, accountNonLocked, credentialsNonExpired, enabled);
    }

    private User makeUpdated(UserDetails stored, UserDetails toUpdate) {
        return makeUser(toUpdate.getUsername(), toUpdate.getPassword(), stored.getAuthorities(),
                stored.isAccountNonExpired(), stored.isAccountNonLocked(),
                stored.isCredentialsNonExpired(), stored.isEnabled());
    }
}
