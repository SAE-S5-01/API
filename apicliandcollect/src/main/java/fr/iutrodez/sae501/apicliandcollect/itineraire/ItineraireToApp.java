package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;

import java.util.LinkedHashMap;

@Getter
@Setter
public class ItineraireToApp {

    private String nomItineraire;
    private String idItineraire;
    LinkedHashMap<Long , String> ordreClients;
    GeoJsonLineString geoJsonLineString;

}
