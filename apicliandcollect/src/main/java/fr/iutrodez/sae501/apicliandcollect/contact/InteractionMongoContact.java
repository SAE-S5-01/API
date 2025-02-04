/*
 * InteractionMongoContact.java                                                                             04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.contact;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InteractionMongoContact extends MongoRepository<ContactMongo, String> {
    ContactMongo findBy_id(Long id);
}