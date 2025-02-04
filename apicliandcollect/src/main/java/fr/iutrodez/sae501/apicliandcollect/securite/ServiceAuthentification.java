package fr.iutrodez.sae501.apicliandcollect.securite;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.InteractionBdUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Service gérant l'authentification des utilisateurs dans l'application.
 * Il s'appuie sur Spring Security pour valider les informations d'authentification
 * et interagit avec la base de données pour récupérer les détails des utilisateurs.
 *
 * @quthor Descriaud Lucas
 */
@Service
public class ServiceAuthentification {

    // Référence à la classe d'interaction avec la base de données pour les utilisateurs.
    private final InteractionBdUtilisateur interactionBdUtilisateur;

    // Encodeur utilisé pour chiffrer et comparer les mots de passe des utilisateurs.
    private final PasswordEncoder passwordEncoder;

    // Gestionnaire d'authentification de Spring Security pour valider les credentials.
    private final AuthenticationManager authenticationManager;

    /**
     * Constructeur du service d'authentification.
     *
     * @param interactionBdUtilisateur Interface pour interagir avec la base de données des utilisateurs.
     * @param authenticationManager Gestionnaire d'authentification de Spring Security.
     * @param passwordEncoder Composant pour encoder et comparer les mots de passe.
     */
    public ServiceAuthentification(
            InteractionBdUtilisateur interactionBdUtilisateur,
            AuthenticationManager authenticationManager,
            PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.interactionBdUtilisateur = interactionBdUtilisateur;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Authentifie un utilisateur en validant ses credentials (email et mot de passe).
     *
     * Étapes :
     * - Utilise le `AuthenticationManager` de Spring Security pour valider les informations fournies.
     * - Si l'authentification réussit, récupère l'utilisateur dans la base de données à partir de son email.
     *
     * @param mail L'email de l'utilisateur à authentifier.
     * @param mdp Le mot de passe fourni pour l'authentification.
     * @return L'utilisateur correspondant à l'email, si les credentials sont valides.
     * @throws org.springframework.security.core.AuthenticationException Si les informations d'authentification sont invalides.
     * @throws java.util.NoSuchElementException Si aucun utilisateur avec cet email n'est trouvé dans la base de données.
     */
    public Utilisateur authenticate(String mail, String mdp) {
        // Valide les informations d'authentification via Spring Security.
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(mail, mdp)
        );

        // Récupère l'utilisateur dans la base de données à partir de son email.
        return interactionBdUtilisateur.findByMail(mail)
                .orElseThrow(); // Lance une exception si l'utilisateur n'existe pas.
    }
}
