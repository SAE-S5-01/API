/*
 * InteractionMongoItineraire.java                                                                          04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;

public interface InteractionMongoItineraire extends MongoRepository<Itineraire, String> {

    boolean existsByNomItineraireAndIdCreateur(String nomItineraire, Long idCreateur);

    ArrayList<Itineraire> findByIdCreateur(Long idCreateur);

    Itineraire findBy_idAndIdCreateur(String id, Long idCreateur);

    /**
     * Récupérer tous les itinéraires dont la liste de contacts contient un certain id
     * @param idContact ID du contact
     * @return Liste des itinéraires correspondants
     */
    @Query(value = "{ 'listeIdContact': ?0 }")
    ArrayList<Itineraire> findByIdContact(long idContact);

    void deleteByIdCreateur(long idCreateur);

    @Query(value = "{ 'listeIdContact': ?0 }", delete = true)
    void deleteByIdContact(long idContact);
}