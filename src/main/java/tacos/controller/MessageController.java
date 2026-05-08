package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tacos.entity.TacoOrder;

import java.util.LinkedList;
import java.util.Queue;

@RestController
@CrossOrigin("https://localhost:8443")
@RequestMapping(path = "/message",
                produces = "application/json")
@RequiredArgsConstructor
public class MessageController {

    public Queue<TacoOrder> orders = new LinkedList<>();

    @GetMapping
    public TacoOrder showLastOrder(){
        if(orders.isEmpty()) return null;
        return orders.poll();
    }
}
