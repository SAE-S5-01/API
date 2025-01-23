package fr.iutrodez.sae501.apicliandcollect.contact;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterractionMongoContact extends MongoRepository<ContactMongo, Long> {
    
}