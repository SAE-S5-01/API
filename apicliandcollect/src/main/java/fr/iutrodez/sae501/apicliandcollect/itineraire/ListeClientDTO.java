/*
 * ListeClientDTO.java                                                                                      04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;
import java.util.LinkedHashMap;

@Setter
@Getter
public class ListeClientDTO {

    private Point domicile;

    private String nomItineraire;

    @ValidateMap
    private LinkedHashMap<Long, Point> listePoint;

}


