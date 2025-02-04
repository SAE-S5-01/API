/*
 * ListeEtapesItineraireSerializer.java                                                                     04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ListeEtapesItineraireSerializer {
    @JsonProperty("id")
    private Long id;


    @JsonProperty("nom")
    private String nom;

    @JsonProperty("latitude")
    private double latitude;

    @JsonProperty("longitude")
    private double longitude;



    /**
     * Constructeur de la classe ItineraireToApp pour un point
     * @param id
     * @param nom
     * @param latitude
     * @param longitude
     */
    public ListeEtapesItineraireSerializer(Long id, String nom, double latitude, double longitude) {
        this.id = id;
        this.nom = nom;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Obligatoire pour Jackson
    public ListeEtapesItineraireSerializer() {}
}
