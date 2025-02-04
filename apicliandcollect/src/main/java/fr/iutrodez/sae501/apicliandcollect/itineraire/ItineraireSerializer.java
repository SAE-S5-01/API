package fr.iutrodez.sae501.apicliandcollect.itineraire;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.iutrodez.sae501.apicliandcollect.contact.ContactService;
import fr.iutrodez.sae501.apicliandcollect.contact.InterractionBdContact;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class ItineraireSerializer {

    @JsonProperty("idItineraire")
    private  String idItineraire;


    @JsonProperty("nomItineraire")
    private  String nom;

    @JsonProperty("ordreClients")
    private  LinkedHashMap<Long , String> listeContact;

    @JsonProperty("geoJsonLineString")
    private GeoJsonLineString lineStringCoordonnees;

    public ItineraireSerializer(Itineraire i ,  InterractionBdContact bd ) {
        this.idItineraire = i.get_id();
        this.nom = i.getNomItineraire();
        this.listeContact = new LinkedHashMap<>();

        // Récupérer les noms des contacts depuis un service (ou une base de données)
        ArrayList<Long> idsContacts = i.getListeIdContact();
        for (Long id : idsContacts) {
            String nomContact = bd.findNameById(id);
            this.listeContact.put(id, nomContact);
        }
        this.lineStringCoordonnees = i.getLineStringCoordonnees();
    }


    public ItineraireSerializer() {}
}
