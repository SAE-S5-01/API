package fr.iutrodez.sae501.apicliandcollect.parcours;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.UtilisateurMongo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface InterractionMongoParcours extends MongoRepository<ParcoursMongo, String> {
    ParcoursMongo findBy_id(String id);

    void deleteBy_id(String id);

    ParcoursMongo findByIdParcours(Long id);
}
