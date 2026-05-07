package tacos.messaging;

import tacos.entity.TacoOrder;

public interface OrderMessagingService {

    void sendOrder(TacoOrder order);

    void convertAndSend(TacoOrder order);
}
