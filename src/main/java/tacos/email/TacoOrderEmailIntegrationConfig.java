package tacos.email;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.mail.ImapMailReceiver;
import org.springframework.integration.mail.dsl.Mail;

import java.util.Properties;

@Configuration
public class TacoOrderEmailIntegrationConfig {

    @Bean
    public ImapMailReceiver imapMailReceiver(EmailProperties emailProperties) {
        String imapUrl = String.format("imaps://%s:%s@%s:993/%s",
                emailProperties.getUsername(),
                emailProperties.getPassword(),
                emailProperties.getHost(),
                emailProperties.getMailbox());

        System.out.println("Configuring IMAP receiver with URL: " +
                imapUrl.replaceAll(":[^:]*@", ":****@"));

        ImapMailReceiver receiver = new ImapMailReceiver(imapUrl);

        // ВАЖНО: Добавляем JavaMail свойства обратно!
        Properties javaMailProperties = new Properties();
        javaMailProperties.setProperty("mail.imap.ssl.enable", "true");
        javaMailProperties.setProperty("mail.imap.ssl.trust", emailProperties.getHost());
        javaMailProperties.setProperty("mail.imap.auth.mechanisms", "PLAIN");
        javaMailProperties.setProperty("mail.imap.connectiontimeout", "10000");
        javaMailProperties.setProperty("mail.imap.timeout", "10000");

        receiver.setJavaMailProperties(javaMailProperties);
        receiver.setShouldMarkMessagesAsRead(true);

        return receiver;
    }

    @Bean
    public IntegrationFlow tacoOrderEmailFlow(
            EmailProperties emailProps,
            ImapMailReceiver imapMailReceiver,
            EmailToOrderTransformer emailToOrderTransformer,
            OrderSubmitMessageHandler orderSubmitHandler) {
        return IntegrationFlow
                .from(Mail.imapInboundAdapter(imapMailReceiver),
                        e -> e.poller(Pollers.fixedDelay(emailProps.getPollRate())
                                .maxMessagesPerPoll(1)
                                .errorHandler(t -> System.err.println("Poller error: " + t.getMessage()))))
                .log()  // Добавляем логирование сообщений
                .transform(emailToOrderTransformer)
                .handle(orderSubmitHandler)
                .get();
    }
}