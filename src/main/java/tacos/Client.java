package tacos;

import jakarta.validation.groups.ConvertGroup;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.entity.Ingredient;

import java.time.Duration;

public class Client {

    /*
        Если хотим писать рест клиента, тут собраны примеры Методов
     */

    public void get(){
        WebClient webClient = WebClient.create("https://localhost:8443");//Можно вместо этой строки зарегистрировать бин
        Flux<Ingredient> ingredients = webClient
                .get()
                .uri("/ingredients")//Если зарегистрировали бин, можем писать относительный путь вместо абсолютного
                .retrieve()
                .bodyToFlux(Ingredient.class);

        ingredients
                .timeout(Duration.ofSeconds(1))
                .subscribe(
                        i -> { /* обработка */ },
                        e -> {
                        });
    }

    public void post(){
        WebClient webClient = WebClient.create("https://localhost:8443");//Можно вместо этой строки зарегистрировать бин
        Mono<Ingredient> ingredientMono = Mono.just(
                new Ingredient("INGC", "Ingredient C", Ingredient.Type.VEGGIES));
        Mono<Ingredient> result = webClient
                .post()
                .uri("/ingredients")
                .body(ingredientMono, Ingredient.class)
                .retrieve()
                .bodyToMono(Ingredient.class);
        result.subscribe(i -> { /* обработка */  });
    }
}
