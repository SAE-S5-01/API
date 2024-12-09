package fr.iutrodez.sae501.apicliandcollect.contact;


import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/contact")
public class ControleurContact {

    @Autowired
    ContactService service;

    @PostMapping("/nouveauContact")
    public ResponseEntity<ContactDTO> nouveauClient(@Valid @RequestBody ContactDTO contactAajoute ,
                                                    Authentication utilisateur) {

        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Contact contact  = service.creerContact(contactAajoute, u);
        Map <String, Object> reponse = new HashMap<>();
        reponse.put("contact", contact);
        return new ResponseEntity<>(contactAajoute, HttpStatus.CREATED);
    }
}
