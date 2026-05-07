package tacos.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import tacos.entity.TacoOrder;

@Service
@RequiredArgsConstructor
public class KafkaOrderMessagingService
            implements OrderMessagingService{

    private final KafkaTemplate<String, TacoOrder> kafkaTemplate;

    @Override
    public void sendOrder(TacoOrder order) {
        kafkaTemplate.send("tacocloud.orders.topic", order);
    }

    /*
    В кафке метода convertAndSend нет, все методы выполняют ковертацию
     */
    @Override
    public void convertAndSend(TacoOrder order) {
        sendOrder(order);
    }
}
