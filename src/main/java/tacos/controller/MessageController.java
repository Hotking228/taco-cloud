package tacos.controller;

import ch.qos.logback.classic.pattern.MessageConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tacos.entity.Taco;
import tacos.entity.TacoOrder;
import tacos.messaging.RabbitOrderMessageReceiver;

@RestController
@RequestMapping(path = "/message",
                produces = "application/json")
@CrossOrigin("https://localhost:8443")
@RequiredArgsConstructor
public class MessageController {

    private final RabbitOrderMessageReceiver receiver;

    /*
        В отличие от JmsTemplate, RabbitMQ сразу же вернет управление, если в очереди нет
     сообщения, тк передается параметр время ожидания сообщения, который по умолчанию 0 мс
     */

    @GetMapping
    public TacoOrder showMessage(){
        return receiver.receiveAndConvertOrder();
    }
}
