package tacos.repository.order;

import tacos.entity.TacoOrder;

public interface OrderRepository {

    TacoOrder save(TacoOrder order);
}
