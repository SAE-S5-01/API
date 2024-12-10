package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UtilisateurService {

    @Autowired
    private InterractionBdUtilisateur interractionBdUtilisateur;

    @Autowired
    private PasswordEncoder encoderMotPasse;


    /**
     * Crée l'utilisateur en base de données
     * @param utilisateurInscrit : les données de l'utilisateur à inscrire
     * @return l'utilisateur inscrit au format Json
     */
    @Transactional
    public UtilisateurDTO creerUtilisateur(UtilisateurDTO utilisateurInscrit) {
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(utilisateurInscrit.getNom());
        utilisateur.setPrenom(utilisateurInscrit.getPrenom());
        utilisateur.setMail(utilisateurInscrit.getMail());
        //TODO encoder mot de passe avant envoi
        utilisateur.setMotDePasse(encoderMotPasse.encode(utilisateurInscrit.getMotDePasse()));
        utilisateur.setAdresse(utilisateurInscrit.getAdresse());
        interractionBdUtilisateur.save(utilisateur);
        return utilisateurEnJson(utilisateur);
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
    public UtilisateurDTO utilisateurEnJson(Utilisateur utilisateur) {
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setNom(utilisateur.getNom());
        utilisateurDTO.setPrenom(utilisateur.getPrenom());
        utilisateurDTO.setMail(utilisateur.getMail());
        utilisateurDTO.setMotDePasse(utilisateur.getMotDePasse());
        utilisateurDTO.setAdresse(utilisateur.getAdresse());
        return utilisateurDTO;
    }


}
