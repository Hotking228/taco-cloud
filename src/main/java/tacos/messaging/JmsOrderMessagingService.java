package tacos.messaging;

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

    @Override
    public void sendOrder(TacoOrder order) {
        jms.send(new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createObjectMessage(order);
            }
        });
    }
}
