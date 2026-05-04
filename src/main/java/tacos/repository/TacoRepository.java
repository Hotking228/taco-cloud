package tacos.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import tacos.entity.Taco;

import java.util.List;
import java.util.Optional;

public interface TacoRepository extends CrudRepository<Taco, Long> {

    List<Taco> findAll(Pageable pageable);
}
