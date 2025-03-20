/*
 * ControleurContact.java                                12 fev. 2025
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ControleurContact {

    private final static String SUCCES_SUPPRESSION = "Client supprimé avec succès";

    @Autowired
    ContactService service;

    /**
     * Représente une réponse indiquant si un client est proche ou non.
     * @param estProche Un booléen indiquant si le client est proche ou non
     */
    private record ReponseEstProche(boolean estProche) {}

    /**
     * Récupère la liste des contacts de l'utilisateur connecté.
     *
     * @param Utilisateur L'utilisateur connecté
     * @return Une entité de réponse contenant la liste des contacts de l'utilisateur
     */
    @GetMapping("/contact")
    public ResponseEntity<List<ContactDTO>> obtenirContacts(Authentication Utilisateur) {
        Utilisateur u = (Utilisateur) Utilisateur.getPrincipal();
        List<ContactDTO> contact = service.listeContact(u);
        return new ResponseEntity<>(contact, HttpStatus.OK);
    }

    /**
     * Récupère la liste des contacts proches de l'utilisateur connecté.
     * @param id L'identifiant du client à vérifier
     * @param longitude La longitude à comparer
     * @param latitude La latitude à comparer
     * @param utilisateur L'utilisateur connecté
     * @return Une entité de réponse contenant la liste des contacts proches de l'utilisateur
     */
    @GetMapping("/contact/{id}/proche")
    public ResponseEntity<List<ContactDTO>> obtenirContactsProches(@PathVariable Long id, @RequestParam double longitude,
                                                                   @RequestParam double latitude, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        return new ResponseEntity<>(service.getContactsProches(id, longitude, latitude, u), HttpStatus.OK);
    }

    /**
     * Récupère la liste des prospects proches de l'utilisateur connecté.
     * @param longitude La longitude à comparer
     * @param latitude La latitude à comparer
     * @param utilisateur L'utilisateur connecté
     * @return Une entité de réponse contenant la liste des prospects proches de l'utilisateur
     */
    @GetMapping("/contact/prospect/proche")
    public ResponseEntity<List<ContactDTO>> obtenirProspectsProches(@RequestParam double longitude,
                                                                    @RequestParam double latitude, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        return new ResponseEntity<>(service.getProspectsProches(longitude, latitude, u), HttpStatus.OK);
    }

    /**
     * Vérifie si un client est proche de l'utilisateur connecté.
     * @param id L'identifiant du client
     * @param longitude La longitude à comparer
     * @param latitude La latitude à comparer
     * @return Une entité de réponse indiquant si le client est proche ou non
     */
    @GetMapping("/contact/client/{id}/proche")
    public ResponseEntity<ReponseEstProche> estClientProche(@PathVariable Long id, @RequestParam double longitude,
                                                            @RequestParam double latitude) {
        return new ResponseEntity<>(new ReponseEstProche(service.isClientProche(id, longitude, latitude)),
                                    HttpStatus.OK);
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
     * Modifie un contact de l'utilisateur connecté.
     * Si l'adresse a changé, supprime tous les itinéraires contenant ce contact.
     * @param contactModifier Le contact à modifier
     * @param id L'identifiant du contact à supprimer (passé dans l'URL)
     * @param utilisateur L'utilisateur connecté
     * @return Une liste des ID des itinéraires supprimés
     */
    @PutMapping("/contact")
    public ResponseEntity<ArrayList> modifierClient(@Valid @RequestBody ContactDTO contactModifier, @RequestParam String id, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long ID = Long.parseLong(id);
        ArrayList<String> idItinerairesSupprimes = service.modifierContact(contactModifier, u, ID);
        return new ResponseEntity<>(idItinerairesSupprimes, HttpStatus.OK);
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
