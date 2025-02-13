/*
 * ItineraireService.java                                                                                   04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.iutrodez.sae501.apicliandcollect.contact.InteractionBdContact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.InteractionBdUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonLineString;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

@Service
public class ItineraireService {

    @Autowired
    private InteractionMongoItineraire interactionMongoItineraire;

    @Autowired
    private InteractionBdUtilisateur interactionBdUtilisateur;

    @Autowired
    private InteractionBdContact interactionBdContact;


    // TODO appel de la classe utilitaire ou seront stockes les methodes de calcul d'itineraire
    public String calculerItineraire(
            LinkedHashMap<Long, Point> listeClients) throws JsonProcessingException {
        LinkedHashMap<String , Point> listeClientsFormatte = new LinkedHashMap<>();
        return formattageItineraire(listeClients);
    }

    /**
     * Créer un itinéraire
     * @param idCreateur ID de l'utilisateur créant l'itinéraire
     * @param itineraire Objet contenant les informations de l'itinéraire
     * @return L'itinéraire créé
     * @throws JsonProcessingException Erreur de formatage JSON
     */
    public String creerItineraire(long idCreateur , ListeClientDTO itineraire) throws JsonProcessingException {

        Collection<Point> listeCoordonne = itineraire.getListePoint().values();
        String nomItineraire = itineraire.getNomItineraire();

        if (!interactionBdUtilisateur.existsById(idCreateur)) {
            // TODO Meilleure gestion de l'erreur renvoie une erreur 500  , renvoyer une erreur 401 avec une exception personnalisée
            throw new IllegalArgumentException("L'utilisateur n'existe pas");
        }

        Itineraire insertion = new Itineraire();
        if (nomItineraire != null) {
            insertion.setNomItineraire(nomItineraire);
        }
        insertion.setIdCreateur(idCreateur);

        ArrayList<Long> listeContact = new ArrayList<>(itineraire.getListePoint().keySet());
        insertion.setListeIdContact(listeContact);

        GeoJsonLineString geoJsonLineString = new GeoJsonLineString(new ArrayList<>(listeCoordonne));
        insertion.setLineStringCoordonnees(geoJsonLineString);

        interactionMongoItineraire.save(insertion);
        return formattageItineraire(insertion);
    }

    /**
     * Modifie un itinéraire
     * @param idCreateur ID de l'utilisateur modifiant l'itinéraire
     * @param id ID de l'itinéraire à modifier
     * @param itineraire Objet contenant les informations de l'itinéraire
     * @return L'itinéraire modifié
     * @throws JsonProcessingException Erreur de formatage JSON
     */
    public String modifierItineraire(long idCreateur, String id, ListeClientDTO itineraire) throws JsonProcessingException {
        Itineraire itineraireAModifier = interactionMongoItineraire.findBy_idAndIdCreateur(id, idCreateur);
        if (itineraireAModifier == null) {
            throw new IllegalArgumentException("L'itinéraire n'existe pas");
        }

        Collection<Point> listeCoordonne = itineraire.getListePoint().values();
        String nomItineraire = itineraire.getNomItineraire();

        if (nomItineraire != null) {
            itineraireAModifier.setNomItineraire(nomItineraire);
        }

        ArrayList<Long> listeContact = new ArrayList<>(itineraire.getListePoint().keySet());
        itineraireAModifier.setListeIdContact(listeContact);

        GeoJsonLineString geoJsonLineString = new GeoJsonLineString(new ArrayList<>(listeCoordonne));
        itineraireAModifier.setLineStringCoordonnees(geoJsonLineString);

        interactionMongoItineraire.save(itineraireAModifier);
        return formattageItineraire(itineraireAModifier);
    }

    /** 
     * Supprime le contact d'id id
     * @param id l'id du contact à supprimer
     */
    public void supprimerItineraire(Utilisateur u, String id) {
        Itineraire itineraire = interactionMongoItineraire.findBy_idAndIdCreateur(id, u.getId());
        interactionMongoItineraire.delete(itineraire);
        // TODO : vérifier si besoin suppression autre...
    }

    /**
     * Récupérer la liste des itinéraires d'un utilisateur
     * @param idCreateur ID de l'utilisateur
     * @return La liste des itinéraires de l'utilisateur
     * @throws JsonProcessingException Erreur de formatage JSON
     */
    public String listeItineraire(Long idCreateur) throws JsonProcessingException {
        ArrayList<Itineraire> listeItineraire = interactionMongoItineraire.findByIdCreateur(idCreateur);
        ArrayList<ItineraireSerializer> itineraireList = new ArrayList<>();
        for (Itineraire i : listeItineraire) {
            itineraireList.add(new ItineraireSerializer(i , interactionBdContact));
        }
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(itineraireList);
    }

    /**
     * Formater un itinéraire en JSON
     * @param i Itinéraire à formater
     * @return L'itinéraire formaté en JSON
     * @throws JsonProcessingException
     */
    private String formattageItineraire(Itineraire i) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(new ItineraireSerializer(i , interactionBdContact));
    }

    /**
     * Formater une liste d'étape d'itinéraire en JSON
     * @param listeClients Liste des étapes de l'itinéraire
     * @return La liste des étapes formatée en JSON
     * @throws JsonProcessingException
     */
    public String formattageItineraire(LinkedHashMap<Long, Point> listeClients) throws JsonProcessingException {
        ArrayList<ListeEtapesItineraireSerializer> itineraireList = new ArrayList<>();
        // Le domicile est une étape mais non un CONTACT d'où l'id "bidon"
        Point domicile = listeClients.get(-1L);

        // Ajouter le point de départ
        itineraireList.add(new ListeEtapesItineraireSerializer(-1L, "Départ", domicile.getY(), domicile.getX()));

        // Enlever le domicile de la liste pour éviter une null pointer dans la boucle
        listeClients.remove(-1L);

        // Ajouter les clients avec leurs ID
        for (Map.Entry<Long, Point> entry : listeClients.entrySet()) {
            Long id = entry.getKey();
            Point point = entry.getValue();
            itineraireList.add(new ListeEtapesItineraireSerializer(id, interactionBdContact.findNameById(id), point.getY(), point.getX()));
        }

        // Ajouter le point d'arrivée
        itineraireList.add(new ListeEtapesItineraireSerializer(-1L, "Arrivée", domicile.getY(), domicile.getX()));

        // Convertir la liste en JSON et l'encapsuler dans un objet
        ObjectMapper objectMapper = new ObjectMapper();

        // Créer un objet avec la clé "itineraire" qui contient la liste
        LinkedHashMap<String, Object> result = new LinkedHashMap<>();
        result.put("itineraire", itineraireList);

        // Convertir l'objet final en JSON
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(result);
    }

}
