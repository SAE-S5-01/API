/*
 * ControleurItineraire.java                                                                                04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import com.fasterxml.jackson.core.JsonProcessingException;
import fr.iutrodez.sae501.apicliandcollect.ReponseTextuelle;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api")
public class    ControleurItineraire {

    private static final String SUCCES_MODIFICATION = "Itinéraire modifié avec succès";

    private static final String SUCCES_SUPPRESSION = "Itinéraire supprimé avec succès";

    @Autowired
    private ItineraireService itineraireService;

    @GetMapping("/itineraire")
    public ResponseEntity<String> getItineraire(Authentication utilisateur) throws JsonProcessingException {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long idCreateur = u.getId();
        String listeItineraire = itineraireService.listeItineraire(idCreateur);
        return new ResponseEntity<>(listeItineraire, HttpStatus.OK);
    }

    @PostMapping("/itineraire/calculer")
    public ResponseEntity<String> verifierListe(@Valid @RequestBody ListeClientDTO listePoint) throws JsonProcessingException {
        LinkedHashMap<Long, Point> liste = listePoint.getListePoint();
        /*
         * Ajout du domicile en premier élément de la liste , id -1 car sera retiré plus tard.
         */
        liste.putFirst(-1L, listePoint.getDomicile());
        String itineraireCalcule = itineraireService.calculerItineraire(liste);
        return new ResponseEntity<>(itineraireCalcule, HttpStatus.OK);

    }

    @PostMapping("/itineraire")
    public ResponseEntity<String> creerItineraire(Authentication utilisateur, @Valid @RequestBody ListeClientDTO itineraire) throws JsonProcessingException {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long idCreateur = u.getId();

        String itineraireCree = itineraireService.creerItineraire(idCreateur, itineraire);
        return new ResponseEntity<>(itineraireCree, HttpStatus.OK);
    }

    @PutMapping("/itineraire/{id}")
    public ResponseEntity<String> modifierItineraire(Authentication utilisateur, @PathVariable String id, @Valid @RequestBody ListeClientDTO itineraire) throws JsonProcessingException {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long idCreateur = u.getId();

        String itineraireModifie = itineraireService.modifierItineraire(idCreateur, id, itineraire);
        return new ResponseEntity<>(itineraireModifie, HttpStatus.OK);
    }
    
    /**
     * Supprime un itinéraire de l'utilisateur connecté.
     * @param id L'identifiant de l'itinéraire à supprimer (passé dans l'URL)
     * @param utilisateur L'utilisateur connecté
     * @return Un message de succès
     */
    @DeleteMapping("/itineraire/{id}")
    public ResponseEntity<ReponseTextuelle> supprimerItineraire(@PathVariable String id, Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        itineraireService.supprimerItineraire(u, id);
        return new ResponseEntity<>(new ReponseTextuelle(SUCCES_SUPPRESSION), HttpStatus.OK);
    }
}
