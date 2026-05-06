package tacos.config;

import jakarta.jms.Destination;
import org.apache.activemq.artemis.jms.client.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import tacos.entity.TacoOrder;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class MessagingConfig {

    @Bean
    public Destination orderQueue(){
        return new ActiveMQQueue("tacocloud.order.queue");
    }


    /*
        Если не выполнить того что указано ниже, то на стороне которая принимает сообщения
    придется иметь такой же класс как и отправляемый, с теми же полями и полным именем, чтобы
    это избежать используем маппинги типов.
     */
    @Bean
    public MappingJackson2MessageConverter messageConverter(){//конвертер сообщений
        MappingJackson2MessageConverter messageConverter =
                new MappingJackson2MessageConverter();
        messageConverter.setTypeIdPropertyName("_typeId");//указываем, в какой тип нужно преобразовать входящее сообщение
        Map<String, Class<?>> typeIdMappings = new HashMap<>();
        typeIdMappings.put("order", TacoOrder.class);
        messageConverter.setTypeIdMappings(typeIdMappings);
        return messageConverter;
    }
}
