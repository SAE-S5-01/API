package fr.iutrodez.sae501.apicliandcollect.itineraire;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;

@Service
public class ItineraireService {

    @Autowired
    private InterractionMongoItineraire interractionMongoItineraire;

    // TODO appel de la classe utilitaire ou seront stockes les methodes de calcul d'itineraire
    public  LinkedHashMap<Long , GeoJsonPoint> creerItineraire(
            LinkedHashMap<Long , GeoJsonPoint> listeClients) {
        return listeClients; // STUB
    }

}
