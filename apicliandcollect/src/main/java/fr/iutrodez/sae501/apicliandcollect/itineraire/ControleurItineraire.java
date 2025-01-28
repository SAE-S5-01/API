package fr.iutrodez.sae501.apicliandcollect.itineraire;


import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;

@RestController
@RequestMapping("api/itineraire")
public class    ControleurItineraire {

    @Autowired
    private ItineraireService itineraireService;

    @PostMapping("/calculer")
    public ResponseEntity<LinkedHashMap<Long, Point>> verifierListe(@Valid @RequestBody ListeClientDTO utilisateurInscrit) {

        LinkedHashMap<Long, Point> itineraireCalcule = itineraireService.calculerItineraire(utilisateurInscrit.getListePoint());
        return new ResponseEntity<>(itineraireCalcule, HttpStatus.OK);

    }
}
