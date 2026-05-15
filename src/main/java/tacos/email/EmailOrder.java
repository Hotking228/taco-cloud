package tacos.email;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import tacos.entity.Taco;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmailOrder {
    private String email;
    private List<Taco> tacos = new ArrayList<>();

    public void addTaco(Taco taco){
         tacos.add(taco);
    }
}
