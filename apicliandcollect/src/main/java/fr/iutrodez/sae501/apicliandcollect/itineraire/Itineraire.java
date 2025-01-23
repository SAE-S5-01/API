package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.LinkedHashMap;


@Setter
@Getter
@Document(collection = "itineraire")
@TypeAlias("ItineraireMongo")
public class Itineraire {

    private long _id;

    private String nomItineraire;

    private LinkedHashMap<Long , GeoJsonPoint> listeClients;

    private long idCreateur;
}

