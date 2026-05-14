package tacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.channel.QueueChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlowBuilder;
import org.springframework.integration.dsl.IntegrationFlowDslKt;
import org.springframework.integration.dsl.MessageChannels;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.dsl.Files;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.List;

@Configuration
public class FileWriterIntegrationConfig {

    /*
     PublishSubscribeChannel – сообщения, опубликованные в Pub
    lishSubscribeChannel, передаются одному или нескольким по
    лучателям. Если получателей несколько, то все они получат со
    общение;

     QueueChannel – сообщения, опубликованные в QueueChannel, хра
    нятся в очереди, пока не будут извлечены получателем в поряд
    Обзор ландшафта Spring Integration
    293
    ке их поступления (FIFO). Если получателей несколько, то только
    один из них получит сообщение;

     PriorityChannel – действует аналогично QueueChannel, но со
    общения извлекаются получателями не в порядке поступления,
    а с учетом приоритетов, указанных в заголовках priority сооб
    щений;

     RendezvousChannel – действует аналогично QueueChannel, за ис
    ключением того, что попытка отправителя послать следующее
    сообщение блокируется, пока получатель не извлечет предыду
    щее сообщение, что позволяет эффективно синхронизировать
    работу отправителя и получателя;

     DirectChannel – действует аналогично PublishSubscribeChannel,
    но отправляет сообщение одному получателю, вызывая его в том
    же потоке выполнения, в котором действует отправитель. Это
    позволяет передавать транзакции через весь канал;

     ExecutorChannel – действует аналогично DirectChannel, но от
    правка сообщения происходит через TaskExecutor в отдельном
    потоке выполнения. Каналы этого типа не поддерживают транз
    акции, охватывающие каналы целиком;

     FluxMessageChannel – канал сообщений Reactive Streams Publisher
    на основе Flux Project Reactor.
     */
    @Bean
    public MessageChannel orderChannel(){
        return new DirectChannel();
    }

    @Bean
    public IntegrationFlow fileWriterFlow(){
        return IntegrationFlow
                .from(MessageChannels.direct("textInChannel"))//Адаптер канала
                .channel("orderChannel")
                .<String>filter(s -> !s.isEmpty())//фильтр
                .<String, String>transform(t -> t.toUpperCase())//маппер
                .<String, String>route(s -> s.contains("f") ? "contains" : "dont contains", //Позволяет перенаправить сообщения в разные потоки
                        mapping -> mapping
                        .subFlowMapping("contains", sf -> sf
                                .handle(MessageChannels.direct("containsF")))
                        .subFlowMapping("dont contains", sf -> sf
                                .handle(MessageChannels.direct("dontContainsF"))))
                .split()//сплитит одно сообщение на несколько, аналог flatMap из stream
                        //если использовать совместно с route можно одно сообщение разделить на несколько потоков
                .handle(Files //Активирует какую - либо службу, в данном случае - запись данных в файл
                        .outboundAdapter(new File("/tmp/sia6/files"))
                        .fileExistsMode(FileExistsMode.REPLACE)
                        .appendNewLine(true))
                .get();
    }
}
