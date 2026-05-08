package tacos.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import tacos.controller.MessageController;
import tacos.entity.TacoOrder;

/*
У Кафки нет активной модели принятия сообщений
 */
@Service
@RequiredArgsConstructor
public class OrderListener {

    private final MessageController messageController;

    @KafkaListener(topics = "tacocloud.orders.topic",
                   groupId = "tacocloud.cloud.group")
    public void handle(TacoOrder order){
        messageController.orders.add(order);
    }
}
