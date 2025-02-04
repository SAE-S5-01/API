/*
 * Itineraire.java                                                                                          04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;

@Setter
@Getter
@Document(collection = "itineraire")
@TypeAlias("ItineraireMongo")
public class Itineraire {

    @Id
    private String _id;

    private String nomItineraire;

    private long idCreateur;

    private ArrayList<Long> listeIdContact;

    private GeoJsonLineString lineStringCoordonnees;
}

