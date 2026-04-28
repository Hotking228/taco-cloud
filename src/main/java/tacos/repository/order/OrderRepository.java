package tacos.repository.order;

import tacos.entity.TacoOrder;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository
        extends CrudRepository<TacoOrder, Long> {
}
