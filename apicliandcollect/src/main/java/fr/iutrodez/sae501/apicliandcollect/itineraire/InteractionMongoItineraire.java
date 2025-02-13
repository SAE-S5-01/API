/*
 * InteractionMongoItineraire.java                                                                          04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.ArrayList;

public interface InteractionMongoItineraire extends MongoRepository<Itineraire, String> {

    ArrayList<Itineraire> findByIdCreateur(Long idCreateur);

    Itineraire findBy_idAndIdCreateur(String id, Long idCreateur);

    void deleteByIdCreateur(long idCreateur);

    @Query(value = "{ 'listeIdContact': ?0 }", delete = true)
    void deleteByIdContact(long idContact);
}