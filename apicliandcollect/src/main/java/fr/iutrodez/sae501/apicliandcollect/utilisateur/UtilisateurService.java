package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.contact.InterractionMongoContact;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    @Autowired
    private InterractionBdUtilisateur interractionBdUtilisateur;

    @Autowired
    private InterractionMongoUtilisateur interractionMongoUtilisateur;

    @Autowired
    private PasswordEncoder encoderMotPasse;
    @Autowired
    private InterractionMongoContact interractionMongoContact;


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
        Utilisateur resultat = interractionBdUtilisateur.save(utilisateur);
        utilisateurMongo.set_id(resultat.getId());
        utilisateurMongo.setLocation(new GeoJsonPoint(utilisateurInscrit.getLongitude(), utilisateurInscrit.getLatitude()));
        UtilisateurMongo localisation = interractionMongoUtilisateur.save(utilisateurMongo);
        return utilisateurEnJson(utilisateur, localisation);
    }


    /**
     * Supprime un utilisateur de la base de données
     * @param utilisateurASupprimer l'utilisateur à supprimer
     */
    @Transactional
    public void supprimerUtilisateur(Utilisateur utilisateurASupprimer) {
        interractionBdUtilisateur.delete(utilisateurASupprimer);
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
