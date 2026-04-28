package tacos.repository.ingredient;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import tacos.entity.Ingredient;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
/*
Данный класс реализует интерфейс IngredientRepository с использованием голого
Spring JDBC, для того чтобы всё работало необходимо убрать extends в IngredientRepository
и убрать закомментированные Override в данном классе
На данный момент класс не используется.
Спринг автоматически находит реализацию IngredientRepository.
Аналогично работает JdbcOrderRepository
 */
//@Repository
public class JdbcIngredientRepository /*implements IngredientRepository*/{

    private JdbcTemplate jdbcTemplate;

    public JdbcIngredientRepository(JdbcTemplate jdbcTemplate){
        this.jdbcTemplate = jdbcTemplate;
    }

//    @Override
    public Iterable<Ingredient> findAll() {
        return jdbcTemplate.query(
                "select id, name, type from ingredient",
                this::mapRowToIngredient
        );
    }

    private Ingredient mapRowToIngredient(ResultSet row, int rowNum)
            throws SQLException {
        return new Ingredient(
                row.getString("id"),
                row.getString("name"),
                Ingredient.Type.valueOf(row.getString("type"))
        );
    }

//    @Override
    public Optional<Ingredient> findById(String id) {
        List<Ingredient> results = jdbcTemplate.query(
                "select id, name, type from ingredient where id=?",
                this::mapRowToIngredient,
                id
        );

        return results.isEmpty() ?
                Optional.empty() :
                Optional.of(results.get(0));
    }

//    @Override
    @Transactional
    public Ingredient save(Ingredient ingredient) {
        jdbcTemplate.update(
                "insert into ingredient (id, name, type) values (?, ?, ?)",
                ingredient.getId(),
                ingredient.getName(),
                ingredient.getType().toString()
        );

        return ingredient;
    }
}
