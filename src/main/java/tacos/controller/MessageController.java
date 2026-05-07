package tacos.controller;

import jakarta.jms.JMSException;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.entity.TacoOrder;
import tacos.messaging.JmsOrderReceiver;

@RestController
@RequestMapping(path = "/message",
                produces = "application/json")
@CrossOrigin(origins = "https://tacocloud:8443")
@RequiredArgsConstructor
public class MessageController {

    private final JmsOrderReceiver receiver;

    @GetMapping
    public TacoOrder showLastOrder() throws JMSException {
        return receiver.receiveAndConvertOrder();
    }
}
