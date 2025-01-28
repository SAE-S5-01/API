package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterractionMongoItineraire extends MongoRepository<Itineraire, String> {

}