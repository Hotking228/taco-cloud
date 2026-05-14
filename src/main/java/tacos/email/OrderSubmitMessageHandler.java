package tacos.email;

import lombok.RequiredArgsConstructor;
import org.springframework.integration.core.GenericHandler;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import tacos.config.ApiProperties;

@Component
@RequiredArgsConstructor
public class OrderSubmitMessageHandler
        implements GenericHandler<EmailOrder> {

    private final RestTemplate rest;
    private final ApiProperties apiProps;


    @Override
    public Object handle(EmailOrder order, MessageHeaders headers) {

        System.out.println(order);
        rest.postForObject(apiProps.getUrl(), order, String.class);
        return null;
    }
}
