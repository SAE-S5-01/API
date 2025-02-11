/*
 * ControleurUtilisateur.java                            07 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import fr.iutrodez.sae501.apicliandcollect.ReponseTextuelle;
import fr.iutrodez.sae501.apicliandcollect.securite.ServiceAuthentification;
import fr.iutrodez.sae501.apicliandcollect.securite.ServiceJwt;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

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
    private UtilisateurService service;

    @Autowired
    private ServiceAuthentification serviceAuthentification;

    @Autowired
    private ServiceJwt serviceJwt;

    private static final String SUCCES_CONNEXION = "Utilisateur connecté avec succès";
    private static final String SUCCES_SUPPRESSION = "Compte supprimé avec succès";
    private static final String SUCCES_MODIFICATION = "Utilisateur modifié avec succès";

    /**
     * Récupère les informations de l'utilisateur connecté.
     * @param authentication les informations d'authentification de l'utilisateur
     * @return une entité de réponse avec les informations de l'utilisateur connecté
     */
    @GetMapping
    public ResponseEntity<UtilisateurDTO> getUtilisateur(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        return ResponseEntity.ok(service.getUtilisateur(utilisateur));
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
        UtilisateurDTO utilisateurDonnees = service.getUtilisateur(utilisateurAauthentifier);
        Map <String, String> reponse = new HashMap<>();
        reponse.put("message", SUCCES_CONNEXION);
        reponse.put("token", token);
        reponse.put("y", String.valueOf(utilisateurDonnees.getLatitude()));
        reponse.put("x", String.valueOf(utilisateurDonnees.getLongitude()));
        return ResponseEntity.ok(reponse);
    }

    /**
     * Inscrit un nouvel utilisateur.
     * @param utilisateurInscrit l'objet de transfert de données de l'utilisateur contenant les détails de l'utilisateur
     * @return une entité de réponse avec un message de création
     */
    @PostMapping("/inscription")
    public ResponseEntity<Map<String, String>> inscrireUtilisateur(@Valid @Validated(GroupValidationDTO.CreationUtilisateur.class) @RequestBody UtilisateurDTO utilisateurInscrit) {
        UtilisateurDTO utilisateurDTO = service.creerUtilisateur(utilisateurInscrit);
        Map <String, String> reponse = new HashMap<>();
        reponse.put("mail", utilisateurDTO.getMail());
        reponse.put("motDePasse", utilisateurDTO.getMotDePasse());
        Utilisateur utilisateur = serviceAuthentification.authenticate(utilisateurInscrit.getMail(), utilisateurInscrit.getMotDePasse());
        String token = serviceJwt.generateToken(utilisateur);
        reponse.put("token", token);
        return new ResponseEntity<>(reponse, HttpStatus.CREATED);
    }

    /**
     * Modifie les informations de l'utilisateur connecté
     * @param utilisateurModifie les nouvelles informations de l'utilisateur
     * @param authentication les informations d'authentification de l'utilisateur
     * @return une entité de réponse avec un message de succès pour la modification
     */
    @PutMapping
    public ResponseEntity<Map<String, ?>> modifierCompte(@Valid @Validated(GroupValidationDTO.ModificationUtilisateur.class)
                                                         @RequestBody UtilisateurDTO utilisateurModifie,
                                                         Authentication authentication) {
        Map<String, String> reponse = new HashMap<>();
        Utilisateur u = (Utilisateur) authentication.getPrincipal();
        service.modifierUtilisateur(utilisateurModifie, u);
        reponse.put("message", SUCCES_MODIFICATION);
        String token = serviceJwt.generateToken(u);
        reponse.put("token", token);
        return new ResponseEntity<>(reponse, HttpStatus.OK);
    }

    /**
     * Supprime un utilisateur
     * @param authentication : les informations d'authentification de l'utilisateur
     * @return un message de succès et un code de statut 200 (OK)
     */
    @DeleteMapping
    public ResponseEntity<ReponseTextuelle> supprimerCompte(Authentication authentication) {
        Utilisateur utilisateur = (Utilisateur) authentication.getPrincipal();
        service.supprimerUtilisateur(utilisateur);
        return new ResponseEntity<>(new ReponseTextuelle(SUCCES_SUPPRESSION) ,HttpStatus.OK);
    }

}