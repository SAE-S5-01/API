package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;
import java.util.LinkedHashMap;


@Setter
@Getter
public class ListeClientDTO {

    @ValidateMap
    private LinkedHashMap<Long , Point> listePoint;

}


