package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface InterractionMongoUtilisateur extends MongoRepository<UtilisateurMongo, String> {
}
