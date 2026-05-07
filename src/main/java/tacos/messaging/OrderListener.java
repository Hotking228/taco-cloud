package tacos.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import tacos.entity.TacoOrder;

@Component
@RequiredArgsConstructor
public class OrderListener {

 //    private final KitchenUI ui;


    /*
        Аналогично JmsListener, смотреть коммит "Пассивная модель mq" или как то так,
    Он идет до этого коммита
     */
    @RabbitListener(queues = "tacocloud.order")
    public void receiveObject(TacoOrder order){
//        ui.display(order);
    }
}
