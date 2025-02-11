/*
 * UtilisateurService.java                               11 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.Contact;
import fr.iutrodez.sae501.apicliandcollect.contact.InteractionBdContact;
import fr.iutrodez.sae501.apicliandcollect.contact.InteractionMongoContact;
import fr.iutrodez.sae501.apicliandcollect.itineraire.InteractionMongoItineraire;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service de l'utilisateur
 *
 * @author Lucas DESCRIAUD
 * @author Loïc FAUGIERES
 * @author Simon GUIRAUD
 * @author Noah MIQUEL
 */
@Service
public class UtilisateurService {

    @Autowired
    private InteractionBdUtilisateur interactionBdUtilisateur;

    @Autowired
    private InteractionBdContact interactionBdContact;

    @Autowired
    private InteractionMongoUtilisateur interactionMongoUtilisateur;

    @Autowired
    private InteractionMongoContact interactionMongoContact;

    @Autowired
    private InteractionMongoItineraire interactionMongoItineraire;

    @Autowired
    private PasswordEncoder encoderMotPasse;

    /**
     * Récupère l'utilisateur connecté avec sa localisation
     * @param u l'utilisateur connecté
     * @return l'utilisateur connecté au format Json
     */
    public UtilisateurDTO getUtilisateur(Utilisateur u) {
        long id = u.getId();
        Utilisateur utilisateur = interactionBdUtilisateur.findById(id).orElseThrow();
        UtilisateurMongo localisation = interactionMongoUtilisateur.findBy_id(id);
        return utilisateurEnJson(utilisateur, localisation);
    }

    /**
     * Crée l'utilisateur en base de données
     * @param utilisateurInscrit : les données de l'utilisateur à inscrire
     * @return l'utilisateur inscrit au format Json
     */
    @Transactional
    public UtilisateurDTO creerUtilisateur(UtilisateurDTO utilisateurInscrit) {
        Utilisateur utilisateur = new Utilisateur();
        UtilisateurMongo utilisateurMongo = new UtilisateurMongo();
        utilisateur.setNom(utilisateurInscrit.getNom());
        utilisateur.setPrenom(utilisateurInscrit.getPrenom());
        utilisateur.setMail(utilisateurInscrit.getMail());
        utilisateur.setMotDePasse(encoderMotPasse.encode(utilisateurInscrit.getMotDePasse()));
        utilisateur.setAdresse(utilisateurInscrit.getAdresse());
        Utilisateur resultat = interactionBdUtilisateur.save(utilisateur);
        utilisateurMongo.set_id(resultat.getId());
        utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurInscrit.getLongitude(), utilisateurInscrit.getLatitude()));
        UtilisateurMongo localisation = interactionMongoUtilisateur.save(utilisateurMongo);
        return utilisateurEnJson(utilisateur, localisation);
    }

    /**
     * Modifie l'utilisateur stocké
     * @param utilisateurModifie : les données de l'utilisateur à modifier
     * @return l'utilisateur modifié au format Json
     */
    @Transactional
    public void modifierUtilisateur(UtilisateurDTO utilisateurModifie, Utilisateur utilisateur) {
        utilisateur.setNom(utilisateurModifie.getNom());
        utilisateur.setPrenom(utilisateurModifie.getPrenom());
        utilisateur.setMail(utilisateurModifie.getMail());
        utilisateur.setMotDePasse(encoderMotPasse.encode(utilisateurModifie.getMotDePasse()));
        utilisateur.setAdresse(utilisateurModifie.getAdresse());
        interactionBdUtilisateur.save(utilisateur);

        UtilisateurMongo utilisateurMongo = interactionMongoUtilisateur.findBy_id(utilisateur.getId());
        utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurModifie.getLongitude(), utilisateurModifie.getLatitude()));
        interactionMongoUtilisateur.save(utilisateurMongo);
    }


    /**
     * Supprime l'utilisateur connecté de la base de données
     * @param utilisateurASupprimer l'utilisateur à supprimer
     */
    @Transactional
    public void supprimerUtilisateur(Utilisateur utilisateurASupprimer) {
        List<Long> idContacts
        = interactionBdContact.findByUtilisateur(utilisateurASupprimer).stream()
                              .map(Contact::getId).toList();

        interactionBdUtilisateur.delete(utilisateurASupprimer);
        interactionBdContact.deleteByUtilisateur(utilisateurASupprimer);

        interactionMongoUtilisateur.deleteBy_id(utilisateurASupprimer.getId());
        for (Long id : idContacts) {
            interactionMongoContact.deleteBy_id(id);
        }
        interactionMongoItineraire.deleteByIdCreateur(utilisateurASupprimer.getId());
    }

    /**
     * Revoie les informations de l'utilisateur au format Json
     * @param utilisateur l'utilisateur dont les données sont passées au format Json
     * @return les informations de l'utilisateur au format Json
     */
    @Transactional
    public UtilisateurDTO utilisateurEnJson(Utilisateur utilisateur, UtilisateurMongo localisation) {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setMail(utilisateur.getMail());
        utilisateurDTO.setMotDePasse(utilisateur.getMotDePasse());
        utilisateurDTO.setAdresse(utilisateur.getAdresse());
        utilisateurDTO.setLatitude(localisation.getLocation().getY());
        utilisateurDTO.setLongitude(localisation.getLocation().getX());
        return utilisateurDTO;
    }
}