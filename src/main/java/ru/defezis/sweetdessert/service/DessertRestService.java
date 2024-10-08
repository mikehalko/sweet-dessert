package ru.defezis.sweetdessert.service;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.RestTemplate;
import ru.defezis.sweetdessert.model.Dessert;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class DessertRestService {

    private static final String ROOT = "http://localhost:8081/api/desserts";
    private static final String CHECK_URL = "http://localhost:8081/api/check";

    private static final String GET_URL = ROOT + "/{dessertId}";
    private static final String GET_ALL_URL = ROOT;
    private static final String CREATE_URL = ROOT;
    private static final String UPDATE_URL = ROOT + "/{dessertId}";
    private static final String DELETE_URL = ROOT + "/{dessertId}";

    private final RestTemplate rest;

    public DessertRestService(String accessToken) {
        this.rest = new RestTemplate();
        if (Objects.nonNull(accessToken)) {
            this.rest
                    .getInterceptors()
                    .add(getBearerTokenInterceptor(accessToken));
        }
    }

    private ClientHttpRequestInterceptor getBearerTokenInterceptor(String accessToken) {
        ClientHttpRequestInterceptor interceptor = new ClientHttpRequestInterceptor() {
            @Override
            public ClientHttpResponse intercept(HttpRequest request, byte[] bytes,
                                                ClientHttpRequestExecution execution) throws IOException {
                request.getHeaders().add("Authorization", "Bearer " + accessToken);
                return execution.execute(request, bytes);
            }
        };
        return interceptor;
    }

    // TODO process response, status code

    /**
     * Получить список всех Десертов.
     *
     * @return список десертов
     */
    @NotNull
    public List<Dessert> getAll() {
        ResponseEntity<List<Dessert>> response = rest.exchange(GET_ALL_URL,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                });
        List<Dessert> result = response.getBody();

        if (Objects.isNull(result)) {
            throw new RuntimeException("Response for getAll is null");
        }

        return result;
    }

    /**
     * Метод вернет Десерт по заданному идентификатору.
     *
     * @param dessertId идентификатор Десерта
     * @return модель Десерта
     */
    @NotNull
    public Dessert getDessert(long dessertId) {
        ResponseEntity<Dessert> response = rest.exchange(GET_URL,
                HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                }, dessertId);
        Dessert result = response.getBody();
        if (Objects.isNull(result)) {
            throw new RuntimeException("No dessert with id " + dessertId);
        }

        return result;
    }

    /**
     * Сохранить новый Десерт.
     *
     * @param dessertNew новый Десерт
     * @return идентификатор сохраненного десерта
     */
    @NotNull
    public Long create(@NotNull Dessert dessertNew) {
        Long result = rest.postForObject(CREATE_URL, dessertNew, Long.class);
        if (Objects.isNull(result)) {
            throw new RuntimeException("Failed to create dessert");
        }

        return result;
    }

    /**
     * Обновить существующий Десерт.
     *
     * @param dessertNew Десерт с данными для обновления
     * @param dessertId  идентификатор Десерта для обновления
     */
    public void update(@NotNull Dessert dessertNew, long dessertId) {
        rest.put(UPDATE_URL, dessertNew, dessertId);
    }

    /**
     * Удалить Десерт по заданному идентификатору.
     *
     * @param dessertId идентификатор удаляемого Десерта
     */
    public void delete(long dessertId) {
        rest.delete(DELETE_URL, dessertId);
    }

    public HttpStatusCode check() {
        try {
            ResponseEntity<ResponseEntity<String>> exchange =
                    rest.exchange(CHECK_URL, HttpMethod.GET, null, new ParameterizedTypeReference<>() {
                    });
            return Objects.requireNonNull(exchange.getBody()).getStatusCode();
        } catch (Exception e) {
            return HttpStatusCode.valueOf(500);
        }
    }
}
