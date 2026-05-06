package tacos.messaging;

import tacos.entity.TacoOrder;

public interface OrderMessagingService {
    default void sendOrder(TacoOrder order){
        throw new RuntimeException();
    }

    default void convertAndSend(TacoOrder order){
        throw new RuntimeException();
    }
}
