package tacos.email;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "tacocloud.email")
public class EmailProperties {

    private String username;
    private String password;
    private String host;
    private String mailbox;
    private Long pollRate = 30_000L;

    public String getImapUrl(){
        return String.format("imaps://%s:%s@%s:993/%s",
                username, password, host, mailbox);
    }
}
