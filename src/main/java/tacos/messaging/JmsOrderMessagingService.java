package tacos.messaging;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import tacos.entity.TacoOrder;

@Service
@RequiredArgsConstructor
public class JmsOrderMessagingService implements OrderMessagingService{

    private final JmsTemplate jms;
    private final Destination orderQueue;

    @Override
    public void sendOrder(TacoOrder order) {
        jms.send(orderQueue, session -> {
            Message message = session.createObjectMessage(order);
            message.setStringProperty("X_ORDER_SOURCE", "WEB");//указываем что заказ сделан из веба
            return message;
        });
    }

    @Override
    public void convertAndSend(TacoOrder order){
        jms.convertAndSend("tacocloud.order.queue", order, (m) -> {
            m.setStringProperty("X_ORDER_SOURCE", "WEB");
            return m;
        });
    }
}
