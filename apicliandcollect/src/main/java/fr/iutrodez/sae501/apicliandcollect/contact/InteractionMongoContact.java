package fr.iutrodez.sae501.apicliandcollect.contact;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InteractionMongoContact extends MongoRepository<ContactMongo, String> {
    ContactMongo findBy_id(Long id);
}