package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    public Iterable<Taco> recentTacos(){
        PageRequest page = PageRequest.of(
                0, 12, Sort.by("createdAt").descending());
        return tacoRepo.findAll(page);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Taco> tacoById(@PathVariable("id") Long id){
        Optional<Taco> optTaco = tacoRepo.findById(id);
        if(optTaco.isPresent()){
            return new ResponseEntity<>(optTaco.get(), HttpStatus.OK);
        }

        return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
    }

    @PostMapping(consumes = "application/json") //Кладем новый объект на сервер
    @ResponseStatus(HttpStatus.CREATED)//Возвращаем статус 201(CREATED) вместо 200(OK)
    public Taco postTaco(@RequestBody Taco taco){//Указывает что тело запроса
                            //должно быть преобразовано в объект
                            //Taco
        return tacoRepo.save(taco);
    }
}
