package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tacos.entity.TacoOrder;
import tacos.repository.order.OrderRepository;

@RestController
@RequestMapping(path = "/api/order",
                produces = "application/json")
@CrossOrigin(origins = "https://tacocloud:8443")
@RequiredArgsConstructor
public class TacoOrderController {

    private final OrderRepository orderRepo;

    @PutMapping(path = "/{orderId}", consumes = "application/json")//полностью изменяем объект
    public TacoOrder putOrder(
            @PathVariable("orderId") Long orderId,
            @RequestBody TacoOrder order){
        order.setId(orderId);
        return orderRepo.save(order);
    }

    @PatchMapping(path = "/{orderId}", consumes = "application/json")//Изменяем только некоторые поля
    public TacoOrder patchOrder(@PathVariable("orderId") Long orderId,
                                @RequestBody TacoOrder patch){
        TacoOrder order = orderRepo.findById(orderId).orElseThrow();
        if(patch.getDeliveryName() != null){
            order.setDeliveryName(patch.getDeliveryName());
        }

        if(patch.getDeliveryStreet() != null){
            order.setDeliveryStreet(patch.getDeliveryStreet());
        }

        if(patch.getDeliveryCity() != null){
            order.setDeliveryCity(patch.getDeliveryCity());
        }

        if(patch.getDeliveryState() != null){
            order.setDeliveryState(patch.getDeliveryState());
        }

        if(patch.getDeliveryZip() != null){
            order.setDeliveryZip(patch.getDeliveryZip());
        }

        if(patch.getCcNumber() != null){
            order.setCcNumber(patch.getCcNumber());
        }

        if(patch.getCcExpiration() != null){
            order.setCcExpiration(patch.getCcExpiration());
        }

        if(patch.getCcCVV() != null){
            order.setCcCVV(patch.getCcCVV());
        }

        return orderRepo.save(order);
    }

    @DeleteMapping("/{orderId}")//Удаляем контент
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteOrder(@PathVariable("orderId") Long orderId){
        try {
            orderRepo.deleteById(orderId);
        } catch (EmptyResultDataAccessException e) {}
    }
}
