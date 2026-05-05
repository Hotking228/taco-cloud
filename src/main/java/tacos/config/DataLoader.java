package tacos.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import tacos.entity.Ingredient;
import tacos.entity.Taco;
import tacos.entity.User;
import tacos.repository.TacoRepository;
import tacos.repository.UserRepository;
import tacos.repository.IngredientRepository;

import java.util.List;

@Component
@RequiredArgsConstructor
@Profile({"dev", "test", "qa"}) // Или:
//@Profile("!prod")
public class DataLoader implements CommandLineRunner {

    private final IngredientRepository ingredientRepo;
    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;
    private final TacoRepository tacoRepo;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        Ingredient flourTortilla = new Ingredient(
                "FLTO", "Flour Tortilla", Ingredient.Type.WRAP);
        Ingredient cornTortilla = new Ingredient(
                "COTO", "Corn Tortilla", Ingredient.Type.WRAP);
        Ingredient groundBeef = new Ingredient(
                "GRBF", "Ground Beef", Ingredient.Type.PROTEIN);
        Ingredient carnitas = new Ingredient(
                "CARN", "Carnitas", Ingredient.Type.PROTEIN);
        Ingredient tomatoes = new Ingredient("TMTO", "Diced Tomatoes", Ingredient.Type.VEGGIES);
        Ingredient lettuce = new Ingredient(
                "LETC", "Lettuce", Ingredient.Type.VEGGIES);
        Ingredient cheddar = new Ingredient(
                "CHED", "Cheddar", Ingredient.Type.CHEESE);
        Ingredient jack = new Ingredient(
                "JACK", "Monterrey Jack", Ingredient.Type.CHEESE);
        Ingredient salsa = new Ingredient(
                "SLSA", "Salsa", Ingredient.Type.SAUCE);
        Ingredient sourCream = new Ingredient(
                "SRCR", "Sour Cream", Ingredient.Type.SAUCE);
        ingredientRepo.save(flourTortilla);
        ingredientRepo.save(cornTortilla);
        ingredientRepo.save(groundBeef);
        ingredientRepo.save(carnitas);
        ingredientRepo.save(tomatoes);
        ingredientRepo.save(lettuce);
        ingredientRepo.save(cheddar);
        ingredientRepo.save(jack);
        ingredientRepo.save(salsa);
        ingredientRepo.save(sourCream);

        Taco taco1 = Taco.builder()
                .name("Carnivore")
                .ingredients(List.of(
                        flourTortilla, groundBeef, carnitas,
                        sourCream, salsa, cheddar))
                .build();
        tacoRepo.save(taco1);

        Taco taco2 = Taco.builder()
                .name("Bovine Bounty")
                .ingredients(List.of(
                        cornTortilla, groundBeef, cheddar,
                        jack, sourCream))
                .build();
        tacoRepo.save(taco2);

        Taco taco3 = Taco.builder()
                .name("Veg-Out")
                .ingredients(List.of(
                        flourTortilla, cornTortilla, tomatoes,
                        lettuce, salsa))
                .build();
        tacoRepo.save(taco3);

        userRepo.save(User.builder()
                .username("habuma")
                .password(passwordEncoder.encode("password"))
                .role(User.Role.ROLE_ADMIN)
                .build());

        userRepo.save(User.builder()
                .username("tacochef")
                .password(passwordEncoder.encode("password"))
                .role(User.Role.ROLE_ADMIN)
                .build());
    }
}
