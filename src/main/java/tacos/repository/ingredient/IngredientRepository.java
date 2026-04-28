package tacos.repository.ingredient;

import tacos.entity.Ingredient;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface IngredientRepository
        extends CrudRepository<Ingredient, String> {
}
