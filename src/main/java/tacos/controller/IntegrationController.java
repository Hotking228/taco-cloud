package tacos.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tacos.sia6.FileWriterGateway;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/integration",
                produces = "application/json")
@CrossOrigin("https://localhost:8443")
public class IntegrationController {

    private final FileWriterGateway fileWriter;

    @GetMapping
    public void writeToFile(@RequestParam("data") String data){
        fileWriter.writeToFile("file", data);
    }
}
