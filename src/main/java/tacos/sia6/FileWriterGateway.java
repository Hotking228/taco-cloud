package tacos.sia6;

import org.springframework.integration.annotation.MessagingGateway;
import org.springframework.integration.file.FileHeaders;
import org.springframework.messaging.handler.annotation.Header;
/*
Реализация создается автоматически, подобно jpa
 */
@MessagingGateway(defaultRequestChannel = "textInChannel")//Объявление шлюза сообщений
public interface FileWriterGateway {

    void writeToFile(//Выполняет запись
            @Header(FileHeaders.FILENAME) String filename, String data);
    //Параметр filename будет отправляться в заголовок сообщения
    //Параметр data будет отправляться в тело сообщения
}
