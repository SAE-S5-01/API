package fr.iutrodez.sae501.apicliandcollect.contact;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ControleurContact {

    @Autowired
    InterractionBdContact interractionBdClient;



    @PostMapping("/nouveauContact")
    public ResponseEntity<Map<String, String>> nouveauClient(@Valid @RequestBody ContactDTO contactAajoute)  {
        return null;
    }
}
