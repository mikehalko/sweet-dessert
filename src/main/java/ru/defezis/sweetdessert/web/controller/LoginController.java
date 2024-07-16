package ru.defezis.sweetdessert.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.defezis.sweetdessert.dto.CreateUserRequest;
import ru.defezis.sweetdessert.service.UserService;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/user/login")
public class LoginController {

    private final UserService userService;

    @PostMapping("/create")
    public String create(CreateUserRequest createUserRequest) { // TODO шифровать пароль сразу!, // TODO нужен id
        log.info("Create user");
        userService.save(createUserRequest);
        return "redirect:/user/login";
    }
}