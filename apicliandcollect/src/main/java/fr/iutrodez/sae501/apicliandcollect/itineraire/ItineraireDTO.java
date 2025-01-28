package fr.iutrodez.sae501.apicliandcollect.itineraire;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

import java.util.LinkedHashMap;

@Getter
@Setter
public class ItineraireDTO {

    // Non obligatoire
    @Size(max = 50, message = "Le nom de l'itinéraire ne doit pas dépasser 50 caractères")
    private String nomItineraire;


    @NotNull(message = "L'identifiant du créateur de l'itinéraire est obligatoire")
    private long idCreateur;

    @NotNull(message = "La liste des clients ordonnée est obligatoire")
    @ValidateMap
    private LinkedHashMap<Long , Point> ordreClients;
}

