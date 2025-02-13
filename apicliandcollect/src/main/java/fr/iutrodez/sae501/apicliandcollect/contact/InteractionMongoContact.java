/*
 * InteractionMongoContact.java                                                                             04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.contact;

import org.springframework.data.geo.Distance;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InteractionMongoContact extends MongoRepository<ContactMongo, String> {
    ContactMongo findBy_id(Long id);

    /**
     * Recherche les contacts proches d'une localisation
     * @param location La localisation de référence
     * @param distance La distance
     * @return La liste des contacts proches
     */
    List<ContactMongo> findByLocationNear(GeoJsonPoint location, Distance distance);

    void deleteBy_id(Long id);
}