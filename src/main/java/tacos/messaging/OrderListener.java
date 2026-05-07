package tacos.messaging;

import org.springframework.context.annotation.Profile;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;
import tacos.entity.TacoOrder;

@Profile("jms-listener")
@Component
public class OrderListener {
//    private KitchenUI ui;

    //Пассивная модель принятия сообщений, JmsListener работает аналогично аннотациям GetMapping/PostMapping,
    //т.е. асинхронно ждет сообщения и вызывается когда такое сообщение пришло
    @JmsListener(destination = "tacocloud.order.queue")
    public void receiveOrder(TacoOrder order){
//        ui.displayOrder(order);
    }
}
