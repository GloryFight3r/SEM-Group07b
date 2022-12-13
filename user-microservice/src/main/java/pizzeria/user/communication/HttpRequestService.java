package pizzeria.user.communication;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pizzeria.user.domain.user.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Service
public class HttpRequestService {
    private final transient RestTemplate restTemplate;

    /**
     * Dependency injection
     *
     * @param restTemplateBuilder builder for RestTemplate
     */
    public HttpRequestService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    /**
     * Sends a http request to the authentication microservice telling it to register a user with given id, password and role
     * @param user User object from which we extract the id and the role
     * @param password Password of the user
     * @return True or False depending on the response of the HTTP request
     */
    public boolean registerUser(User user, String password) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create headers
        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("id", user.getId());
        map.put("password", password);
        map.put("role", user.getRole());

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity response = this.restTemplate.postForEntity("http://localhost:8081/register", entity, ResponseEntity.class);

        // check response status code
        return response.getStatusCode() == HttpStatus.CREATED;
    }
    /**
     * Sends a http request to the authentication microservice requesting to authenticate
     * a user with given id and password
     * @param id ID of the user we want to authenticate
     * @param password Password of the user
     * @return Optional with the jwtToken or none, depending on whether we successfully authenticated
     */
    public Optional<String> loginUser(String id, String password) {
        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set `content-type` header
        headers.setContentType(MediaType.APPLICATION_JSON);
        // set `accept` header
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        // create headers
        // create a map for post parameters
        Map<String, Object> map = new HashMap<>();
        map.put("id", id);
        map.put("password", password);

        // build the request
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(map, headers);

        // send POST request
        ResponseEntity<String> response = this.restTemplate.postForEntity("http://localhost:8081/authenticate", entity, String.class);

        // check response status code
        if (response.getStatusCode() == HttpStatus.OK) {
            String toParse = response.getBody();
            String jwtToken = toParse.split("\"")[3];
            return Optional.of(jwtToken);
        } else {
            return Optional.empty();
        }
    }
}
