package tacos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.integration.annotation.Filter;
import org.springframework.integration.annotation.Router;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.annotation.Splitter;
import org.springframework.integration.annotation.Transformer;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.GenericTransformer;
import org.springframework.integration.file.FileWritingMessageHandler;
import org.springframework.integration.file.support.FileExistsMode;
import org.springframework.integration.router.AbstractMessageRouter;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Configuration
public class FileWriterIntegrationConfig {

    @Bean
    @Transformer(inputChannel = "textInChannel",//Преобразователь
                 outputChannel = "fileWriterChannel")
    public GenericTransformer<String, String> upperCaseTransformer(){
        return text -> text.toUpperCase();
    }

    @Bean
    @ServiceActivator(inputChannel = "textOutChannel")//Обработчик записи в файл
    public FileWritingMessageHandler fileHandle(){
        FileWritingMessageHandler handler =
                new FileWritingMessageHandler(new File("/tmp/sia6/files"));
        handler.setExpectReply(false);
        handler.setFileExistsMode(FileExistsMode.REPLACE);
        handler.setAppendNewLine(true);
        return handler;
    }

    @Bean
    @Filter(inputChannel = "fileWriterChannel",
            outputChannel = "routeChannel")
    public boolean fFilter(String str){
        return str.contains("f");
    }

    @Bean
    @Router(inputChannel = "routeChannel")
    public AbstractMessageRouter router(String str){
        return new AbstractMessageRouter() {
            @Override
            protected Collection<MessageChannel>
                determineTargetChannels(Message<?> message) {
                if(str.contains("r")){
                    return Collections.singleton(rChannel());
                }

                return Collections.singleton(nRChannel());
            }
        };
    }

    @Bean
    @Splitter(inputChannel = "rChannel",
              outputChannel = "splitOutChannel")
    public tacos.config.Splitter split(String str){
        return new tacos.config.Splitter();
    }

    @Bean
    public MessageChannel rChannel(){
        return new DirectChannel();
    }

    @Bean
    public MessageChannel nRChannel(){
        return new DirectChannel();
    }
}
