package tacos.messaging;

import lombok.RequiredArgsConstructor;
import org.ietf.jgss.MessageProp;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessageProperties;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.stereotype.Service;
import tacos.entity.TacoOrder;

@Service
@RequiredArgsConstructor
public class RabbitOrderMessagingService
    implements OrderMessagingService {
    private final RabbitTemplate rabbit;

    @Override
    public void sendOrder(TacoOrder order){
        MessageConverter converter = rabbit.getMessageConverter();
        MessageProperties props = new MessageProperties();
        props.setHeader("X_ORDER_SOURCE", "WEB");
        Message message = converter.toMessage(order, props);
        rabbit.send("tacocloud.order", "kitchen.central",message);
    }

    @Override
    public void convertAndSend(TacoOrder order) {
        rabbit.convertAndSend("tacocloud.order","kitchen.central",order, (m) -> {
            MessageProperties props = m.getMessageProperties();
            props.setHeader("X_ORDER_SOURCE", "WEB");
            return m;
        });
    }
}
