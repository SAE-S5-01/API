/*
 * ListeClientDTO.java                                                                                      04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;
import java.util.LinkedHashMap;

@Setter
@Getter
public class ListeClientDTO {

    private Point domicile;

    private String nomItineraire;

    @NotNull(message = "La liste des clients ordonnée est obligatoire")
    @ValidateMap
    @Size(max = 8, message = "L'itinéraire doit contenir au plus 8 clients")
    private LinkedHashMap<Long , Point> listePoint;

}


