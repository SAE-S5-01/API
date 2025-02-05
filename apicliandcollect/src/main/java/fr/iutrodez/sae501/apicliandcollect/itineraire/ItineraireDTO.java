package fr.iutrodez.sae501.apicliandcollect.itineraire;


import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.geo.Point;

import java.util.LinkedHashMap;

@Getter
@Setter
public class ItineraireDTO {

    // Non obligatoire
    @Size(max = 50, message = "Le nom de l'itinéraire ne doit pas dépasser 50 caractères")
    private String nomItineraire;

    @NotNull(message = "La liste des clients ordonnée est obligatoire")
    @ValidateMap
    private LinkedHashMap<Long , Point> ordreClients;
}

