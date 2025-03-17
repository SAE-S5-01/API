package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface InteractionMongoUtilisateur extends MongoRepository<UtilisateurMongo, String> {
    UtilisateurMongo findBy_id(Long id);

    void deleteBy_id(Long id);
}
