package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import tacos.repository.order.OrderRepository;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final OrderRepository orderRepo;

    @PostMapping("/delete-orders")
    @PreAuthorize("hasRole('ADMIN')")//Проверяем, есть ли такая привилегия у пользователя
    public String deleteAllOrders(){
        orderRepo.deleteAll();
        return "redirect:/";
    }
}
