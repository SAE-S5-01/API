package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface InteractionMongoItineraire extends MongoRepository<Itineraire, String> {

    ArrayList<Itineraire> findByIdCreateur(Long idCreateur);

    Itineraire findBy_idAndIdCreateur(String id, Long idCreateur);
}