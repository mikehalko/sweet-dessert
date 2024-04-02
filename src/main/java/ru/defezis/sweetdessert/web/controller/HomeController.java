package ru.defezis.sweetdessert.web.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
public class HomeController {

    /**
     * Главная страница.
     *
     * @return представление главной страницы
     */
    @GetMapping("/")
    public String home() {
        log.info("Home");
        return "home";
    }
}
