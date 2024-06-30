package ru.defezis.sweetdessert.model;

import lombok.Builder;
import lombok.Data;
import ru.defezis.sweetdessert.enums.Flavor;

@Data
@Builder
public class Ingredient {
    private Long id;
    private String name;
    private Flavor flavor;
}
