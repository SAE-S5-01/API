package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.InterractionBdContact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.InterractionBdUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class ItineraireService {

    @Autowired
    private InterractionMongoItineraire interractionMongoItineraire;

    // TODO appel de la classe utilitaire ou seront stockes les methodes de calcul d'itineraire
    public LinkedHashMap<Long, Point> calculerItineraire(
            LinkedHashMap<Long, Point> listeClients) {
        return listeClients; // STUB
    }

}
