package tacos.messaging;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.stereotype.Component;
import tacos.entity.TacoOrder;

@Component
@RequiredArgsConstructor
public class JmsOrderReceiver implements OrderReceiver{

    private final JmsTemplate jms;
    private final MessageConverter converter;

    public TacoOrder receiveOrder() throws JMSException {
        Message message = jms.receive("tacocloud.order.queue");
        return (TacoOrder) converter.fromMessage(message);
    }

    public TacoOrder receiveAndConvertOrder() throws JMSException {
        return (TacoOrder) jms.receiveAndConvert("tacocloud.order.queue");
    }
}
