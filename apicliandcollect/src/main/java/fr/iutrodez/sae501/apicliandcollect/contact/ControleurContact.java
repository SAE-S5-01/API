/*
 * ControleurContact.java                                                                                   04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.ReponseTextuelle;
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

    private static final String SUCCES_SUPPRESSION = "Client supprimé avec succès";

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
     * @param contactAAjouter le contact à ajouter
     * @param utilisateur l'utilisateur connecté
     * @return une entité de réponse contenant le contact ajouté
     */
    @PostMapping("/contact")
    public ResponseEntity<ContactDTO> nouveauClient(@Valid @RequestBody ContactDTO contactAAjouter ,
                                                    Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        ContactDTO contact  = service.creerContact(contactAAjouter, u);
        return new ResponseEntity<>(contact, HttpStatus.CREATED);
    }

    /**
     * Modifie un contact de l'utilisateur connecté
     * @param contactModifier Le contact à modifier
     * @param id L'identifiant du contact à supprimer (passé dans l'URL)
     * @param utilisateur L'utilisateur connecté
     * @return Un message de succès
     */
    @PutMapping("/contact")
    public ResponseEntity<ReponseTextuelle> modifierClient(@Valid @RequestBody ContactDTO contactModifier, @RequestParam String id, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long ID = Long.parseLong(id);
        service.modifierContact(contactModifier, u, ID);
        return new ResponseEntity<>(new ReponseTextuelle(SUCCES_MODIFICATION), HttpStatus.OK);
    }

    /**
     * Supprime un contact de l'utilisateur connecté.
     * @param id L'identifiant du contact à supprimer (passé dans l'URL)
     * @param utilisateur L'utilisateur connecté
     * @return Un message de succès
     */
    @DeleteMapping("/contact/{id}")
    public ResponseEntity<ReponseTextuelle> supprimerContact(@PathVariable Long id, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        service.supprimerContact(u, id);
        return new ResponseEntity<>(new ReponseTextuelle(SUCCES_SUPPRESSION), HttpStatus.OK);
    }
}
