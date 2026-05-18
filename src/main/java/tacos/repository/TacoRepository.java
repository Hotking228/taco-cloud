package tacos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import tacos.entity.Taco;

import java.util.List;
import java.util.Optional;

//Для использования реактивного веба необходимо унаследоваться от ReactiveCrudRepository
public interface TacoRepository extends ReactiveCrudRepository<Taco, Long> {
}
