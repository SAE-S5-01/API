/*
 * ControleurParcours.java                               12 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.ReponseTextuelle;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import fr.iutrodez.sae501.apicliandcollect.parcours.ParcoursDTO.ValidStatutParcours;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ControleurParcours {

    private final static String SUCCES_MODIFICATION = "Parcours modifié avec succès";

    @Autowired
    ParcoursService service;

    /**
     * Récupère la liste des parcours de l'utilisateur connecté.
     *
     * @param Utilisateur L'utilisateur connecté
     * @return Une entité de réponse contenant la liste des parcours de l'utilisateur
     */
    @GetMapping("/parcours")
    public ResponseEntity<?> obtenirParcours(Authentication Utilisateur,
                                                             @RequestParam(required = true) String statut) {
        try {
            StatutParcours statutEnum = StatutParcours.valueOf(statut);
            Utilisateur u = (Utilisateur) Utilisateur.getPrincipal();
            List<ParcoursDTO> parcours = service.listeParcours(u, statutEnum);
            return new ResponseEntity<>(parcours, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(
                new ReponseTextuelle(ParcoursDTO.STATUT_INVALIDE),
                HttpStatus.BAD_REQUEST
            );
        }
    }

    /**
     * Crée un nouveau parcours pour l'utilisateur connecté.
     *
     * @param parcoursAAjouter Le parcours à ajouter
     * @param utilisateur L'utilisateur connecté
     * @return Une entité de réponse contenant le parcours ajouté
     */
    @PostMapping("/parcours")
    public ResponseEntity<ParcoursDTO> nouveauParcours(@Valid @RequestBody ParcoursDTO parcoursAAjouter ,
                                                       Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        ParcoursDTO parcours  = service.creerParcours(parcoursAAjouter, u);
        return new ResponseEntity<>(parcours, HttpStatus.CREATED);
    }

    /**
     * Modifie un parcours de l'utilisateur connecté
     * @param parcoursModifie Le parcours à modifier
     * @param id L'identifiant du parcours à supprimer (passé dans l'URL)
     * @param utilisateur L'utilisateur connecté
     * @return Un message de succès
     */
    @PutMapping("/parcours/{id}")
    public ResponseEntity<ReponseTextuelle> modifierParcours(@Valid @RequestBody ParcoursDTO parcoursModifie,
                                                             @PathVariable Long id, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        service.modifierParcours(parcoursModifie, u, id);
        return new ResponseEntity<>(new ReponseTextuelle(SUCCES_MODIFICATION), HttpStatus.OK);
    }
}
