package fr.iutrodez.sae501.apicliandcollect.contact;



import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class ControleurContact {

    @Autowired
    ContactService service;

    private static final String SUCCES_MODIFICATION = "Client modifié avec succès";

    /**
     * Récupère la liste des contacts de l'utilisateur connecté.
     *
     * @param Utilisateur l'utilisateur connecté
     * @return une entité de réponse contenant la liste des contacts de l'utilisateur
     */
    @GetMapping("/contact")
    public ResponseEntity<List<ContactDTO>> obtenirContact(Authentication Utilisateur) {
        Utilisateur u = (Utilisateur) Utilisateur.getPrincipal();
        List<ContactDTO> contact = service.listeContact(u);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    /**
     * Crée un nouveau contact pour l'utilisateur connecté.
     *
     * @param contactAajoute le contact à ajouter
     * @param utilisateur l'utilisateur connecté
     * @return une entité de réponse contenant le contact ajouté
     */
    @PostMapping("/contact")
    public ResponseEntity<ContactDTO> nouveauClient(@Valid @RequestBody ContactDTO contactAajoute ,
                                                    Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        ContactDTO contact  = service.creerContact(contactAajoute, u);
        return new ResponseEntity<>(contact, HttpStatus.CREATED);
    }

    @PutMapping("/contact")
    public ResponseEntity<String> modifierClient(@Valid @RequestBody ContactDTO contactModifier,@RequestParam String id,Authentication utilisateur){
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long ID = Long.parseLong(id);
        service.modifierContact(contactModifier,u, ID);
        return new ResponseEntity<>(SUCCES_MODIFICATION,HttpStatus.OK);
    }
}
