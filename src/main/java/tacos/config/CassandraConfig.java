package tacos.config;

import com.datastax.oss.driver.api.core.CqlSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.core.CassandraTemplate;
import org.springframework.data.cassandra.core.convert.MappingCassandraConverter;
import org.springframework.data.cassandra.core.mapping.CassandraMappingContext;

import java.net.InetSocketAddress;

@Configuration
public class CassandraConfig {

    @Bean
    public CqlSession cqlSession(){
        return CqlSession.builder()
                .addContactPoint(new InetSocketAddress("127.0.0.1", 9042))
                .withLocalDatacenter("datacenter1")
                .withKeyspace("tacocloud")
                .build();
    }

    public CassandraMappingContext cassandraMappingContext(){
        return new CassandraMappingContext();
    }

    @Bean
    public MappingCassandraConverter cassandraConverter(CassandraMappingContext context){
        return new MappingCassandraConverter(context);
    }

    @Bean
    public CassandraTemplate cassandraTemplate(
            CqlSession session,
            MappingCassandraConverter converter){
        return new CassandraTemplate(session, converter);
    }
}
