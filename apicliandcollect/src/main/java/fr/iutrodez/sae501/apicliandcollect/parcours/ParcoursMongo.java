package fr.iutrodez.sae501.apicliandcollect.parcours;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;


@Getter
@Setter
@Document(collection = "parcours")
@TypeAlias("parcours")
public class ParcoursMongo {

    private String _id;
    private long idParcours;
    private GeoJsonPoint[] precedentesPositionsGps;

}
