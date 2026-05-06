package tacos;

import org.springframework.web.client.RestTemplate;
import tacos.entity.Ingredient;

import java.util.Arrays;

public class RestIngredientService {

    private RestTemplate restTemplate;

    public RestIngredientService(){
        restTemplate = new RestTemplate();
    }

    public Iterable<Ingredient> findAll(){
        return Arrays.asList(restTemplate.getForObject(
            "https://localhost:8443/api/ingredients",
                Ingredient.class
        ));
    }

    public Ingredient addIngredient(Ingredient ingredient){
        restTemplate.postForObject(
                "https://localhost:8443/api/ingredients",
                ingredient,
                Ingredient.class
        );

        return ingredient;
    }
}
