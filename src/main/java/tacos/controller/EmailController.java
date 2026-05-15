package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tacos.email.EmailOrder;
import tacos.entity.TacoOrder;
import tacos.repository.OrderRepository;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(path = "/orders/fromEmail",
                produces = "application/json")
@RequiredArgsConstructor
@CrossOrigin("https://localhost:8443")
public class EmailController {

    private final OrderRepository orderRepo;

    @GetMapping
    public Iterable<TacoOrder> getOrders(){
        Iterable<TacoOrder> orders = orderRepo.findAll();
        return orders;
    }

    @PostMapping
    public void postOrder(@RequestBody EmailOrder order){
        TacoOrder tacoOrder = TacoOrder.builder()
                .tacos(order.getTacos())
                .deliveryName("dsmjf")
                .deliveryStreet("mfsdk")
                .deliveryCity("dnjcksjnd")
                .deliveryState("mvflkd")
                .deliveryZip("mdkls")
                .ccNumber("123")
                .ccExpiration("12/30")
                .ccCVV("123")
                .build();
        orderRepo.save(tacoOrder);
    }
}
