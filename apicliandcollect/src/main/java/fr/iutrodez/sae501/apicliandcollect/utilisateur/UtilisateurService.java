package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.InteractionMongoContact;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    @Autowired
    private InteractionBdUtilisateur interactionBdUtilisateur;

    @Autowired
    private InteractionMongoUtilisateur interactionMongoUtilisateur;

    @Autowired
    private PasswordEncoder encoderMotPasse;
    @Autowired
    private InteractionMongoContact interactionMongoContact;

    private static final String MAIL_EXISTANT = "Mail déjà utilisé";


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
        //TODO encoder mot de passe avant envoi
        utilisateur.setMotDePasse(encoderMotPasse.encode(utilisateurInscrit.getMotDePasse()));
        utilisateur.setAdresse(utilisateurInscrit.getAdresse());
        Utilisateur resultat = interactionBdUtilisateur.save(utilisateur);
        utilisateurMongo.set_id(resultat.getId());
        utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurInscrit.getLongitude(), utilisateurInscrit.getLatitude()));
        UtilisateurMongo localisation = interactionMongoUtilisateur.save(utilisateurMongo);
        return utilisateurEnJson(utilisateur, localisation);
    }

    /**
     * Modifie l'utilisateur en base de données
     * @param utilisateurModifie : les données de l'utilisateur à modifier
     * @return l'utilisateur modifié au format Json
     */
    @Transactional
    public void modifierUtilisateur(UtilisateurDTO utilisateurModifie, Utilisateur utilisateur) throws IllegalArgumentException {

        if (!interactionBdUtilisateur.existsByMail(utilisateurModifie.getMail()) || utilisateur.getMail().equals(utilisateurModifie.getMail())) {
            utilisateur.setPrenom(utilisateurModifie.getPrenom());
            utilisateur.setMail(utilisateurModifie.getMail());
            utilisateur.setMotDePasse(encoderMotPasse.encode(utilisateurModifie.getMotDePasse()));
            utilisateur.setAdresse(utilisateurModifie.getAdresse());

            interactionBdUtilisateur.save(utilisateur);

            UtilisateurMongo utilisateurMongo = interactionMongoUtilisateur.findBy_id(utilisateur.getId());
            utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurModifie.getLongitude(), utilisateurModifie.getLatitude()));
            interactionMongoUtilisateur.save(utilisateurMongo);
        } else {
            throw new IllegalArgumentException(MAIL_EXISTANT);
        }
    }


    /**
     * Supprime un utilisateur de la base de données
     * @param utilisateurASupprimer l'utilisateur à supprimer
     */
    @Transactional
    public void supprimerUtilisateur(Utilisateur utilisateurASupprimer) {
        interactionBdUtilisateur.delete(utilisateurASupprimer);
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
