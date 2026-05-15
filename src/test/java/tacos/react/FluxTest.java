package tacos.react;

import jakarta.persistence.Tuple;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.*;
import java.util.stream.Stream;

public class FluxTest {

    @Test
    public void createAFlux_just(){
        Flux<String> fruitFlux = Flux//создание потока данных
                .just("Apple", "Orange", "Grape", "Banana", "Strawberry");

        fruitFlux.subscribe(
                f -> System.out.println("Here's some fruit: " + f)
        );//Подписка на поток

        StepVerifier.create(fruitFlux)//Проверяем шаги
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromArray(){
        String[] fruits = new String[]{
            "Apple", "Orange", "Grape", "Banana", "Strawberry"
        };

        Flux<String> fruitFlux = Flux.fromArray(fruits);

        StepVerifier.create(fruitFlux)//Проверяем шаги
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromIterable(){
        List<String> fruitList = new ArrayList<>();

        fruitList.add("Apple");
        fruitList.add("Orange");
        fruitList.add("Grape");
        fruitList.add("Banana");
        fruitList.add("Strawberry");

        Flux<String> fruitFlux = Flux.fromIterable(fruitList);

        StepVerifier.create(fruitFlux)//Проверяем шаги
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_fromStream(){
        Stream<String> fruitStream= Stream.of(
                "Apple", "Orange", "Grape", "Banana", "Strawberry"
        );

        Flux<String> fruitFlux = Flux.fromStream(fruitStream);

        StepVerifier.create(fruitFlux)//Проверяем шаги
                .expectNext("Apple")
                .expectNext("Orange")
                .expectNext("Grape")
                .expectNext("Banana")
                .expectNext("Strawberry")
                .verifyComplete();
    }

    @Test
    public void createAFlux_range(){
        Flux<Integer> intervalFlux = Flux.range(1, 5);//Создается счетчик от 1 до 5 включительно

        StepVerifier.create(intervalFlux)
                .expectNext(1)
                .expectNext(2)
                .expectNext(3)
                .expectNext(4)
                .expectNext(5)
                .verifyComplete();
    }

    @Test
    public void createAFlux_interval(){
        Flux<Long> intervalFlux =
                Flux.interval(Duration.ofSeconds(1))
                        .take(5);

        StepVerifier.create(intervalFlux)
                .expectNext(0L)
                .expectNext(1L)
                .expectNext(2L)
                .expectNext(3L)
                .expectNext(4L)
                .verifyComplete();
    }

    @Test
    public void mergeFlux(){
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        Flux<String> mergedFlux = characterFlux.mergeWith(foodFlux);//объединяем 2 flux

        StepVerifier.create(mergedFlux)//Когда StepVerifier подписывается на mergedFlux, mergedFlux, в свою очередь, подписывается на characterFlux и foodFlux
                .expectNext("Garfield")
                .expectNext("Lasagna")
                .expectNext("Kojak")
                .expectNext("Lollipops")
                .expectNext("Barbossa")
                .expectNext("Apples")
                .verifyComplete();

    }

    @Test
    public void zipFlux(){
        Flux<String> characterFlux = Flux
                .just("Garfield", "Kojak", "Barbossa")
                .delayElements(Duration.ofMillis(500));

        Flux<String> foodFlux = Flux
                .just("Lasagna", "Lollipops", "Apples")
                .delaySubscription(Duration.ofMillis(250))
                .delayElements(Duration.ofMillis(500));

        /*
            Zip Flux, в отличие от Merge Flux складывает данные из 2 потоков в 1
        и создает новый поток
         */
        Flux<Tuple2<String, String>> zippedFlux =
                Flux.zip(characterFlux, foodFlux);

        StepVerifier.create(zippedFlux)//Когда StepVerifier подписывается на mergedFlux, mergedFlux, в свою очередь, подписывается на characterFlux и foodFlux
                .expectNextMatches(p ->
                        p.getT1().equals("Garfield") &&
                        p.getT2().equals("Lasagna"))
                .expectNextMatches(p ->
                        p.getT1().equals("Kojak") &&
                        p.getT2().equals("Lollipops"))
                .expectNextMatches(p ->
                        p.getT1().equals("Barbossa") &&
                        p.getT2().equals("Apples"))
                .verifyComplete();

        /*
            Можно передать свою функцию упаковки данных в zip
         */
        Flux<String> zippedFlux2 =
                Flux.zip(characterFlux, foodFlux, (c, f) -> c + " eats " + f);

        StepVerifier.create(zippedFlux2)
                .expectNext("Garfield eats Lasagna")
                .expectNext("Kojak eats Lollipops")
                .expectNext("Barbossa eats Apples")
                .verifyComplete();
    }

    @Test
    public void firstWithSignalFlux(){
        /*
            Операция firstWithSignal выбирает поток который первыми начинает публиковать свои значения
       и пересылает его значения дальше
         */

        Flux<String> slowFlux = Flux.just("tortoise", "snail", "sloth")
                .delaySubscription(Duration.ofMillis(100));
        Flux<String> fastFlux = Flux.just("hare", "cheetah", "squirrel");

        Flux<String> firstFlux = Flux.firstWithSignal(slowFlux, fastFlux);

        StepVerifier.create(firstFlux)
                .expectNext("hare")
                .expectNext("cheetah")
                .expectNext("squirrel")
                .verifyComplete();
    }

    @Test
    public void skipAFew(){
        Flux<String> countFlux = Flux.just(
                "one", "two", "skip a few", "ninety nine", "one hundred"
        ).skip(3);

        StepVerifier.create(countFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete();
    }

    @Test
    public void skipAFewSeconds(){
        Flux<String> countFlux = Flux.just(
            "one", "two", "skip a few", "ninety nine", "one hundred"
        )
                .delayElements(Duration.ofSeconds(1))
                .skip(Duration.ofMillis(4_000));

        StepVerifier.create(countFlux)
                .expectNext("ninety nine")
                .expectNext("one hundred")
                .verifyComplete();
    }

    @Test
    public void take(){
        Flux<String> nationalParkFlux = Flux.just(
                "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia"
        )
                .take(3);

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone")
                .expectNext("Yosemite")
                .expectNext("Grand Canyon")
                .verifyComplete();
    }

    @Test
    public void takeForWhile(){
        Flux<String> nationalParkFlux = Flux.just(
                        "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia"
                )
                .delayElements(Duration.ofSeconds(1))
                .take(Duration.ofMillis(3500));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone")
                .expectNext("Yosemite")
                .expectNext("Grand Canyon")
                .verifyComplete();
    }

    @Test
    public void filter(){
        Flux<String> nationalParkFlux = Flux.just(
                "Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Acadia"
        )
                .filter(p -> !p.contains(" "));

        StepVerifier.create(nationalParkFlux)
                .expectNext("Yellowstone")
                .expectNext("Yosemite")
                .expectNext("Zion")
                .expectNext("Acadia")
                .verifyComplete();
    }

    @Test
    public void distinct(){
        Flux<String> nationalParkFlux = Flux.just(
                        "dog", "cat", "bird", "dog", "bird", "anteater"
                )
                .distinct();

        StepVerifier.create(nationalParkFlux)
                .expectNext("dog")
                .expectNext("cat")
                .expectNext("bird")
                .expectNext("anteater")
                .verifyComplete();
    }

    @Test
    public void map(){
        Flux<Integer> flux = Flux.just(1, 5, 10, 15)
                .map(v -> v + 1);

        StepVerifier.create(flux)
                .expectNext(2)
                .expectNext(6)
                .expectNext(11)
                .expectNext(16)
                .verifyComplete();

    }

    @Test
    public void flatMap(){
        Flux<Integer> flux = Flux.just(1, 5, 10, 15)
                .flatMap(m -> Mono.just(m)//flatMap принимает Flux или Mono, что позволяет во первых, сделать несколько
                                                 //объектов из одного, во вторых их можно через subscribeOn запустить в разные потоки
                                            //Такая конфигурация запускает в параллельное выполнение
                                            //данные из потока
                        .map(v -> v + 1)
                        .subscribeOn(Schedulers.parallel()));

        flux.subscribe(v -> System.out.println(v));

        Set<Integer> set = Set.of(2, 6, 11, 16);

        StepVerifier.create(flux)
                .expectNextMatches(v -> set.contains(v))
                .expectNextMatches(v -> set.contains(v))
                .expectNextMatches(v -> set.contains(v))
                .expectNextMatches(v -> set.contains(v))
                .verifyComplete();

    }

    @Test
    public void buffer(){
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry"
        );

        Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);

        StepVerifier.create(bufferedFlux)
                .expectNext(Arrays.asList("apple", "orange", "banana"))
                .expectNext(Arrays.asList("kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void bufferAndFlatMap() throws Exception {
        Flux.just(
                        "apple", "orange", "banana", "kiwi", "strawberry")
                .buffer(3)
                .flatMap(x ->
                        Flux.fromIterable(x)
                                .map(y -> y.toUpperCase())
                                .subscribeOn(Schedulers.parallel())
                                .log()
                ).subscribe();
    }

    @Test
    public void collectList() {
        Flux<String> fruitFlux = Flux.just(
                "apple", "orange", "banana", "kiwi", "strawberry");
        Mono<List<String>> fruitListMono = fruitFlux.collectList();
        StepVerifier
                .create(fruitListMono)
                .expectNext(Arrays.asList(
                        "apple", "orange", "banana", "kiwi", "strawberry"))
                .verifyComplete();
    }

    @Test
    public void collectMap() {
        Flux<String> animalFlux = Flux.just(
                "aardvark", "elephant", "koala", "eagle", "kangaroo");
        Mono<Map<Character, String>> animalMapMono =
                animalFlux.collectMap(a -> a.charAt(0));
        StepVerifier
                .create(animalMapMono)
                .expectNextMatches(map -> {
                    return
                            map.size() == 3 &&
                                    map.get('a').equals("aardvark") &&
                                    map.get('e').equals("eagle") &&
                                    map.get('k').equals("kangaroo");
                })
                .verifyComplete();
    }

    @Test
    public void all(){
        Flux<String> animalFlux = Flux.just(
                "aardvark", "elephant", "koala", "eagle", "kangaroo");

        Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));

        StepVerifier.create(hasAMono)
                .expectNext(true)
                .verifyComplete();
    }
}
