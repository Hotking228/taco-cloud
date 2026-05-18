package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import tacos.entity.Taco;
import tacos.entity.TacoOrder;
import tacos.repository.TacoRepository;

import java.util.Optional;

@RestController //Все возвращаемые значения должны вставляться в тело ответа
    // а не переноситься в модель для отображения
@RequestMapping(path = "/api/tacos",
                produces = "application/json")//Указываем возвращаемый тип
@CrossOrigin(origins = "https://tacocloud:8443")//Разрешает межсайтовые запросы
@RequiredArgsConstructor
public class TacoController {

    private final TacoRepository tacoRepo;

    @GetMapping(params = "recent") //Получаем объект от сервера
    public Flux<Taco> recentTacos(){
        return tacoRepo.findAll().take(12);
    }

    @GetMapping("/{id}")
    public Mono<Taco> tacoById(@PathVariable("id") Long id){
        return tacoRepo.findById(id);
    }

    /*
        Мы получаем объект не дожидаясь его маппинга, запускаем параллельный поток для выполнения данной функции
    и при возвращении запускаем еще 1 параллельный поток который сохраняет объект
     */
    @PostMapping(consumes = "application/json") //Кладем новый объект на сервер
    @ResponseStatus(HttpStatus.CREATED)//Возвращаем статус 201(CREATED) вместо 200(OK)
    public Mono<Taco> postTaco(@RequestBody Mono<Taco> taco){//Указывает что тело запроса
                            //должно быть преобразовано в объект
                            //Taco
        return tacoRepo.saveAll(taco).next();
    }
}
