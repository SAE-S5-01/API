package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.exceptions.ErreurControleurUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.securite.ServiceAuthentification;
import fr.iutrodez.sae501.apicliandcollect.securite.ServiceJwt;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 *
 * @version 1.0
 * @Author SAE501
 */
@RestController
@RequestMapping("/api/utilisateur")
public class ControleurUtilisateur {

    @Autowired
    private InterractionBdUtilisateur interractionBdUtilisateur;

    @Autowired
    private ServiceAuthentification serviceAuthentification;

    @Autowired
    private ServiceJwt serviceJwt;

    @Autowired
    private PasswordEncoder encoderMotPasse;

    private static final String SUCCES_INSCRIPTION = "Utilisateur inscrit avec succès";
    private static final String SUCCES_CONNEXION = "Utilisateur connecté avec succès";
    private static final String SUCCES_SUPPRESSION = "Compte supprimé avec succès";
    private static final String ERREUR_SUPPRESSION = "Suppression du compte impossible";
    
    /**
     * Inscrit un nouvel utilisateur.
     *
     * @param utilisateurDTO l'objet de transfert de données de l'utilisateur contenant les détails de l'utilisateur
     * @return une entité de réponse avec un message de succès
     * @throws NoSuchAlgorithmException : si une exception survient lors de la génération du hash , elle est propagé et
     * sera géré par gestionException
     */
    @PostMapping("/inscription")
    public ResponseEntity<Map<String, String>> inscrireUtilisateur(@Valid @RequestBody UtilisateurDTO utilisateurDTO) {
        String nom = utilisateurDTO.getNom();
        String motDePasse = utilisateurDTO.getMotDePasse();
        String mail = utilisateurDTO.getMail();
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(mail);
        utilisateur.setMotDePasse(encoderMotPasse.encode(motDePasse));
        utilisateur.setAdresse(utilisateurDTO.getAdresse());
        interractionBdUtilisateur.save(utilisateur);
        Map <String, String> reponse = new HashMap<>();
        reponse.put("message", SUCCES_INSCRIPTION);
        utilisateur = serviceAuthentification.authenticate(mail, motDePasse);
        String token = serviceJwt.generateToken(utilisateur);
        reponse.put("token", token);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

    /**
     * Authentifie un utilisateur.
     * @param mail: le mail de l'utilisateur
     * @param motDePasse: le mot de passe de l'utilisateur
     * @return une entité de réponse avec un message de succès , un status 200 OK et le token de l'utilisateur
     * si les informations sont correctes sinon un status 401 UNAUTHORIZED et un message d'erreur
     */
    @GetMapping("/connexion")
    public ResponseEntity<Map<String, String>> connexionUtilisateur(@RequestParam String mail, @RequestParam String motDePasse) {
        Utilisateur utilisateurAauthentifier =  serviceAuthentification.authenticate(mail, motDePasse);
        String token = serviceJwt.generateToken(utilisateurAauthentifier);
        Map <String, String> reponse = new HashMap<>();
        reponse.put("message", SUCCES_CONNEXION);
        reponse.put("token", token);
        return ResponseEntity.ok(reponse);
    }

    @PutMapping("/suppresionCompte")
    public ResponseEntity<String> supprimerCompte(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        interractionBdUtilisateur.delete(utilisateur);
        return new ResponseEntity<>(SUCCES_SUPPRESSION ,HttpStatus.OK);
    }
}