package fr.iutrodez.sae501.apicliandcollect.utilisateur;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

/**
 * Contrôleur REST pour la gestion des utilisateurs.
 *
 * @version 1.0
 * @Author SAE501
 */
@RestController
@RequestMapping("/api")
public class ControleurUtilisateur {

    @Autowired
    private InterractionBdUtilisateur interractionBdUtilisateur;

    private static final String MESSAGE_ERREUR = "Erreur interne au serveur";
    private static final String SUCCES_INSCRIPTION = "Utilisateur inscrit avec succès";
    private static final String SUCCES_CONNEXION = "Utilisateur connecté avec succès";
    private static final String ERREUR_CONNEXION = "Mail ou mot de passe incorrect";
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
    public ResponseEntity<Map<String, String>> inscrireUtilisateur(@Valid @RequestBody UtilisateurDTO utilisateurDTO) throws NoSuchAlgorithmException {
        String nom = utilisateurDTO.getNom();
        String motDePasse = utilisateurDTO.getMotDePasse();
        String token = generationToken(nom, motDePasse);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setNom(nom);
        utilisateur.setPrenom(utilisateurDTO.getPrenom());
        utilisateur.setMail(utilisateurDTO.getMail());
        utilisateur.setMotDePasse(motDePasse);
        utilisateur.setAdresse(utilisateurDTO.getAdresse());
        utilisateur.setToken(token);
        interractionBdUtilisateur.save(utilisateur);
        Map <String, String> reponse = new HashMap<>();
        reponse.put("message", SUCCES_INSCRIPTION);
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
        Utilisateur utilisateur = interractionBdUtilisateur.findByMail(mail);
        System.out.println("coucou");
        if ( utilisateur == null || !utilisateur.getMotDePasse().equals(motDePasse)) {
            throw new ErreurControleurUtilisateur(ERREUR_CONNEXION);
        }
        // else
        Map <String, String> reponse = new HashMap<>();
        reponse.put("message", SUCCES_CONNEXION);
        reponse.put("token", utilisateur.getToken());
        return new ResponseEntity<>(reponse , HttpStatus.OK);
    }

    @PutMapping("/suppresionCompte")
    public ResponseEntity<String> supprimerCompte(@RequestParam String token) {
        System.out.println(token);
        Utilisateur utilisateur = interractionBdUtilisateur.findByToken(token);
        if (utilisateur == null) {
            throw new ErreurControleurUtilisateur(ERREUR_SUPPRESSION);
        }
        // else 
        interractionBdUtilisateur.delete(utilisateur);
        return new ResponseEntity<>(SUCCES_SUPPRESSION ,HttpStatus.OK);
    }

    /**
     * Gère les exceptions de validation.
     * Est appelé automatiquement par le @Valid dans la méthode inscrireUtilisateur.
     * @param ex l'exception MethodArgumentNotValidException
     * @return une entité de réponse avec les messages d'erreur de validation et un status 400 BAD REQUEST
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> gestionValidationRequete(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.put(error.getField(), error.getDefaultMessage());
        }
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ErreurControleurUtilisateur.class)
    public ResponseEntity<String> gestionErreurAuthentification(ErreurControleurUtilisateur ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ex.getMessage());
    }

    /**
     * Capture toutes les exceptions non gérées.
     *
     * @return une entité de réponse avec un message d'erreur générique et un status 500 INTERNAL SERVER ERROR
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> gestionException() {
        return ResponseEntity.internalServerError().body(MESSAGE_ERREUR);
    }

    /**
     * Méthode de génération de token.
     *
     * @param nom : nom de l'utilisateur
     * @param motDePasse : mot de passe de l'utilisateur
     * @return un token généré sous la forme nom + motDePasse + timestamp , le tout hashé en base 64
     * @throws NoSuchAlgorithmException : exception levée si l'algorithme de hashage n'est pas trouvé
     */
    private static String generationToken(String nom , String motDePasse) throws NoSuchAlgorithmException {
        String token = nom + motDePasse + System.currentTimeMillis();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(hash);
    }
}