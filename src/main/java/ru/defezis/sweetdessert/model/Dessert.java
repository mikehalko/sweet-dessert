package ru.defezis.sweetdessert.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class Dessert {
    private Long id;
    private String name;
    private List<Ingredient> ingredients;
}
