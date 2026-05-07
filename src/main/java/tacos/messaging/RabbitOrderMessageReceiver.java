package tacos.messaging;


import lombok.RequiredArgsConstructor;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Component;
import tacos.entity.TacoOrder;

@Component
public class RabbitOrderMessageReceiver {

    private final RabbitTemplate rabbit;
    private final MessageConverter messageConverter;

    public RabbitOrderMessageReceiver(RabbitTemplate rabbit,
                                      MessageConverter messageConverter){
        this.rabbit = rabbit;
        this.messageConverter = messageConverter;
    }

    public TacoOrder receiveOrder(){
        Message message = rabbit.receive("tacocloud.order", 100);//ждем 100 мс нового сообщения
        return message != null ?
                (TacoOrder) messageConverter.fromMessage(message) :
                null;
    }

    public TacoOrder receiveAndConvertOrder(){
        return rabbit.receiveAndConvert("tacocloud.orders",
                new ParameterizedTypeReference<TacoOrder>() {});
    }
}

/*
    Чтобы принять сообщение необходимо методы внутри User аннотировать @JsonIgnore из jackson
*/

/*
    Правильная настройка rabbitmq :
        1) Заходим в браузере в rabbitmq
        2) Создаем обменник с именем указанным в application.yml(tacocloud.order)
        3) Биндим на этот обменник очередь с routingKey указанным в application.yml(kitchen.central)
        4) Принимаем сообщения по имени очереди указанном при биндинге обменника на очередь(tacocloud.ordder)

        !! Важно понимать что tacocloud.order из 2 и 4 пункта различные !!
 */