package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "itineraire")
@TypeAlias("ItineraireMongo")
public class ItineraireMongo {

    private long _id;
    private GeoJsonPoint locationDepart;
    private GeoJsonPoint locationArrive;
}
