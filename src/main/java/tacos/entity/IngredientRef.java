package tacos.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Table(name = "ingredient_ref")
public class IngredientRef {
    private final String ingredient;
}
