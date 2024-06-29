package ru.defezis.sweetdessert.data;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.defezis.sweetdessert.exception.IncorrectDataException;
import ru.defezis.sweetdessert.model.Dessert;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class DessertDataService {

    private static long ids = 1;
    private static List<Dessert> dessertList = new ArrayList<>(List.of(
            Dessert.builder().id(ids++).name("Cheesecake").build(),
            Dessert.builder().id(ids++).name("Ice cream").build(),
            Dessert.builder().id(ids++).name("Tiramisu").build(),
            Dessert.builder().id(ids++).name("Brownies").build(),
            Dessert.builder().id(ids++).name("Pecan pie").build(),
            Dessert.builder().id(ids++).name("Chocolate").build(),
            Dessert.builder().id(ids++).name("Madeira cake").build(),
            Dessert.builder().id(ids++).name("Banana pudding").build()
    ));

    public List<Dessert> getAll() {
        return List.copyOf(dessertList);
    }

    /**
     * Метод вернет Десерт по заданному идентификатору.
     *
     * @param id идентификатор Десерта
     * @return модель Десерта, либо null, если Десерт с таким id не найден
     * @throws IncorrectDataException если Десертов с таким id более одного
     */
    public Dessert getById(long id) {
        List<Dessert> result = dessertList.stream()
                .filter(dessert -> dessert.getId().equals(id))
                .toList();

        if (result.size() < 2) {
            return result.get(0);
        } else {
            throw new IncorrectDataException("tooManyDessertsResults");
        }
    }
}
