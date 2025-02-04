package fr.iutrodez.sae501.apicliandcollect.itineraire;


import fr.iutrodez.sae501.apicliandcollect.ReponseTextuelle;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.LinkedHashMap;

@RestController
@RequestMapping("/api")
public class    ControleurItineraire {

    private static final String SUCCES_MODIFICATION = "Itinéraire modifié avec succès";

    private static final String SUCCES_SUPPRESSION = "Itinéraire supprimé avec succès";

    @Autowired
    private ItineraireService itineraireService;

    @GetMapping("/itineraire")
    public ResponseEntity<ArrayList<ItineraireToApp>> getItineraire(Authentication utilisateur) {
        Utilisateur u = (Utilisateur) utilisateur.getPrincipal();
        Long idCreateur = u.getId();
        ArrayList<ItineraireToApp> listeItineraire = itineraireService.listeItineraire(idCreateur);
        return new ResponseEntity<>(listeItineraire, HttpStatus.OK);
    }

    @PostMapping("/itineraire/calculer")
    public ResponseEntity<LinkedHashMap<Long, Point>> verifierListe(@Valid @RequestBody ListeClientDTO utilisateurInscrit) {
        LinkedHashMap<Long, Point> itineraireCalcule = itineraireService.calculerItineraire(utilisateurInscrit.getListePoint());
        return new ResponseEntity<>(itineraireCalcule, HttpStatus.OK);
    }

    @PostMapping("/itineraire")
    public ResponseEntity<ItineraireToApp> creerItineraire(@Valid @RequestBody ItineraireDTO itineraire) {
        ItineraireToApp itineraireCree = itineraireService.creerItineraire(itineraire);
        return new ResponseEntity<>(itineraireCree, HttpStatus.OK);
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
