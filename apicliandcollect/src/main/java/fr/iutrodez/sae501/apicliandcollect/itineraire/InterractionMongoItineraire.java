package fr.iutrodez.sae501.apicliandcollect.itineraire;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterractionMongoItineraire extends MongoRepository<Itineraire, String> {

}