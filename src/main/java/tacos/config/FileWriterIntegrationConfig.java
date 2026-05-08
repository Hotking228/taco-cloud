package tacos.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/META-INF/spring-integration-file.xml")
public class FileWriterIntegrationConfig {
}
