package tacos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tacos.entity.Ingredient;
import tacos.repository.ingredient.IngredientRepository;

@Component
@RequiredArgsConstructor
@Profile({"dev", "test", "qa"}) // Или:
//@Profile("!prod")
public class DataLoader implements CommandLineRunner {

    private final IngredientRepository ingredientRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        ingredientRepo.save(new Ingredient("FLTO", "Flour Tortilla", Ingredient.Type.WRAP));
        ingredientRepo.save(new Ingredient("COTO", "Corn Tortilla", Ingredient.Type.WRAP));
        ingredientRepo.save(new Ingredient("GRBF", "Ground Beef", Ingredient.Type.PROTEIN));
        ingredientRepo.save(new Ingredient("CARN", "Carnitas", Ingredient.Type.PROTEIN));
        ingredientRepo.save(new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES));
        ingredientRepo.save(new Ingredient("LETC", "Lettuce", Ingredient.Type.VEGGIES));
        ingredientRepo.save(new Ingredient("CHED", "Cheddar", Ingredient.Type.CHEESE));
        ingredientRepo.save(new Ingredient("JACK", "Monterrey Jack", Ingredient.Type.CHEESE));
        ingredientRepo.save(new Ingredient("SLSA", "Salsa", Ingredient.Type.SAUCE));
        ingredientRepo.save(new Ingredient("SRCR", "Sour Cream", Ingredient.Type.SAUCE));
    }
}
