package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.ArrayList;

public interface InterractionMongoItineraire extends MongoRepository<Itineraire, String> {

    ArrayList<Itineraire> findByIdCreateur(Long idCreateur);
}