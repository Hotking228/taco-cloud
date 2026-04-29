package tacos.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import tacos.entity.Ingredient;
import tacos.entity.IngredientUDT;
import tacos.repository.ingredient.IngredientRepository;

@Component
public class IngredientByIdConverter implements Converter<String, IngredientUDT> {

    private IngredientRepository ingredientRepo;

    public IngredientByIdConverter(IngredientRepository ingredientRepo) {
        this.ingredientRepo = ingredientRepo;
    }

    @Override
    public IngredientUDT convert(String id) {
        return ingredientRepo.findById(id)
                .map(i -> IngredientUDT.builder()
                        .name(i.getName())
                        .type(i.getType())
                        .build()
                )
                .orElse(null);
    }
}
