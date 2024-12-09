package fr.iutrodez.sae501.apicliandcollect.configuration;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.InterractionBdUtilisateur;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * Classe de configuration de l'application pour la gestion de l'authentification
 * et de la sécurité. Fournit des beans nécessaires à la gestion des utilisateurs,
 * du chiffrement des mots de passe et de l'authentification.
 *
 * @Author Descriaud Lucas
 */
@Configuration
public class ApplicationConfiguration {

    // Dépendance pour interagir avec la base de données des utilisateurs.
    @Autowired
    private final InterractionBdUtilisateur interractionBdUtilisateur;

    /**
     * Constructeur de la classe avec injection de la dépendance `InterractionBdUtilisateur`.
     *
     * @param interractionBdUtilisateur Composant pour interagir avec la base de données des utilisateurs.
     */
    public ApplicationConfiguration(InterractionBdUtilisateur interractionBdUtilisateur) {
        this.interractionBdUtilisateur = interractionBdUtilisateur;
    }

    /**
     * Bean fournissant un `UserDetailsService` personnalisé pour la gestion des utilisateurs.
     * Ce service permet de charger un utilisateur par son adresse e-mail depuis la base de données.
     *
     * @return Un `UserDetailsService` configuré.
     * @throws UsernameNotFoundException Si l'utilisateur n'est pas trouvé.
     */
    @Bean
    UserDetailsService userDetailsService() {
        return username -> interractionBdUtilisateur.findByMail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Bean fournissant un encodeur de mot de passe basé sur l'algorithme BCrypt.
     * Cet encodeur est utilisé pour hacher les mots de passe avant leur stockage.
     *
     * @return Une instance de `BCryptPasswordEncoder`.
     */
    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Bean fournissant un `AuthenticationManager` utilisé pour gérer les processus
     * d'authentification dans l'application. Ce composant est requis par Spring Security.
     *
     * @param config Configuration d'authentification utilisée pour construire le manager.
     * @return Une instance d'`AuthenticationManager`.
     * @throws Exception Si une erreur se produit lors de la création du manager.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Bean fournissant un `AuthenticationProvider` basé sur le `DaoAuthenticationProvider`.
     * Ce provider est responsable de l'authentification des utilisateurs en utilisant
     * un service `UserDetailsService` et un encodeur de mot de passe.
     *
     * @return Une instance d'`AuthenticationProvider`.
     */
    @Bean
    AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        // Configuration du service utilisateur et de l'encodeur de mot de passe.
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }
}
