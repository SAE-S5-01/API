package fr.iutrodez.sae501.apicliandcollect;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ControleurDefaut {

    @GetMapping("/apijoignable")
    public String reponse_ok() {
        return "Api joignable";
    }
}