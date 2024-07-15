package ru.defezis.sweetdessert.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import ru.defezis.sweetdessert.service.DessertRestService;

@RequiredArgsConstructor
@Controller
@Slf4j
public class HomeController {

    private final DessertRestService service;

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

    @GetMapping("/check")
    public ResponseEntity<String> check() {
        HttpStatusCode dessertRestServiceResponse = service.check();
        return new ResponseEntity<>(
                String.format("<h1>OK<h1><br><h2>dessert-rest-service: %s<h2>", dessertRestServiceResponse.value()),
                HttpStatus.OK);
    }
}
