package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.contact.InterractionBdContact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.InterractionBdUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Optional;

@Service
public class ItineraireService {

    @Autowired
    private InterractionMongoItineraire interractionMongoItineraire;

    @Autowired
    private InterractionBdUtilisateur interractionBdUtilisateur;

    @Autowired
    private InterractionBdContact interractionBdContact;


    // TODO appel de la classe utilitaire ou seront stockes les methodes de calcul d'itineraire
    public LinkedHashMap<Long, Point> calculerItineraire(
            LinkedHashMap<Long, Point> listeClients) {
        return listeClients; // STUB
    }

    public ItineraireToApp creerItineraire(ItineraireDTO itineraire) {

        long idCreateur = itineraire.getIdCreateur();
        Collection<Point> listeCoordonne = itineraire.getOrdreClients().values();
        String nomItineraire = itineraire.getNomItineraire();

        if (!interractionBdUtilisateur.existsById(idCreateur)) {
            // TODO Meilleure gestion de l'erreur renvoie une erreur 500  , renvoyer une erreur 401 avec une exception personnalisée
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        }

        Itineraire insertion = new Itineraire();
        if (nomItineraire != null) {
            insertion.setNomItineraire(nomItineraire);
        }
        insertion.setIdCreateur(idCreateur);

        ArrayList<Long> listeContact = new ArrayList<>(itineraire.getOrdreClients().keySet());
        insertion.setListeIdContact(listeContact);

        GeoJsonLineString geoJsonLineString = new GeoJsonLineString(new ArrayList<>(listeCoordonne));
        insertion.setLineStringCoordonnees(geoJsonLineString);

        interractionMongoItineraire.save(insertion);
        return formattageItineraire(insertion); // STUB
    }

    /**
     * Supprime le contact d'id id
     * @param id l'id du contact à supprimer
     */
    public void supprimerItineraire(Utilisateur u, String id) {
        Itineraire itineraire = interractionMongoItineraire.findBy_idAndIdCreateur(id, u.getId());
        interractionMongoItineraire.delete(itineraire);
        // TODO : vérifier si besoin suppression autre...
    }

    public ArrayList<ItineraireToApp> listeItineraire(Long idCreateur) {
        ArrayList<Itineraire> listeItineraire = interractionMongoItineraire.findByIdCreateur(idCreateur);
        ArrayList<ItineraireToApp> listeItineraireFormatte = new ArrayList<>();
        for(Itineraire i : listeItineraire) {
            listeItineraireFormatte.add(formattageItineraire(i));
        }
        return listeItineraireFormatte;

    }

    private ItineraireToApp formattageItineraire(Itineraire i) {
        ItineraireToApp itineraire = new ItineraireToApp();
        itineraire.setNomItineraire(i.getNomItineraire());
        itineraire.setIdItineraire(i.get_id());
        LinkedHashMap<Long , String> listeClient = new LinkedHashMap<>();
        // associe le nom a l'id dans l'ordre des clients ou null si le nom est vide
        i.getListeIdContact().forEach(id ->
            listeClient.put(id, Optional.ofNullable(interractionBdContact.findNameById(id))
                .filter(name -> !name.trim().isEmpty()).orElse(null))
        );

        itineraire.setOrdreClients(listeClient);
        itineraire.setGeoJsonLineString(i.getLineStringCoordonnees());
        return itineraire;
    }
}
