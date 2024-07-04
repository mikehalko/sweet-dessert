package ru.defezis.sweetdessert.web.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.defezis.sweetdessert.model.Dessert;
import ru.defezis.sweetdessert.service.DessertRestService;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/dessert")
public class DessertController {

    private final DessertRestService service;

    @GetMapping()
    public String list(Model model) {
        log.info("List of desserts");
        List<Dessert> result = service.getAll();

        model.addAttribute("dessertList", result);
        log.info("List size={}", result.size());

        return "dessert/list";
    }

    @GetMapping("/{dessertId}")
    public String get(Model model, @PathVariable("dessertId") long dessertId) {
        log.info("Get dessert by id={}", dessertId);
        Dessert result = service.getDessert(dessertId);
        model.addAttribute("dessert", result);

        return "dessert/dessert";
    }

    @GetMapping("/new")
    public String create(Model model) {
        log.info("Get new dessert");
        model.addAttribute("dessertNew", Dessert.builder().build());

        return "dessert/new";
    }

    @PostMapping("/create")
    public String save(@ModelAttribute("dessertNew") Dessert dessertNew) {
        log.info("Save new dessert={}", dessertNew);
        Long dessertId = service.create(dessertNew);

        return "redirect:/dessert/" + dessertId;
    }

    @GetMapping("/edit/{dessertId}")
    public String edit(@PathVariable long dessertId, Model model) {
        log.info("Edit dessert");
        model.addAttribute("dessertOld", service.getDessert(dessertId));
        model.addAttribute("dessertUpdated", Dessert.builder().build());
        return "dessert/edit";
    }

    @PostMapping("/update/{dessertId}")
    public String update(@PathVariable long dessertId, @ModelAttribute("dessertUpdated") Dessert dessertUpdated) {
        log.info("Update dessert={} with id={}", dessertUpdated, dessertId);
        service.update(dessertUpdated, dessertId);

        return "redirect:/dessert/" + dessertId;
    }

    @PostMapping("/delete/{dessertId}")
    public String delete(@PathVariable long dessertId) {
        log.info("Delete dessert by id={}", dessertId);
        service.delete(dessertId);

        return "redirect:/dessert";
    }
}
