package fr.iutrodez.sae501.apicliandcollect.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Classe de configuration de la sécurité de l'application.
 * Configure les paramètres de sécurité, y compris les autorisations,
 * la gestion des sessions, le filtre JWT, et les règles CORS.
 *
 * @Author Descriaud Lucas
 */
@Configuration
@EnableWebSecurity
public class ConfigurationSecurite {

    // Dépendance pour le fournisseur d'authentification utilisé par Spring Security.
    private final AuthenticationProvider authenticationProvider;

    // Filtre personnalisé pour la validation des tokens JWT.
    private final FiltreJwt filtreJwt;

    /**
     * Constructeur avec injection des dépendances nécessaires à la configuration de sécurité.
     *
     * @param filtreJwt Filtre JWT personnalisé.
     * @param authenticationProvider Fournisseur d'authentification.
     */
    public ConfigurationSecurite(
            FiltreJwt filtreJwt,
            AuthenticationProvider authenticationProvider
    ) {
        this.authenticationProvider = authenticationProvider;
        this.filtreJwt = filtreJwt;
    }

    /**
     * Configure la chaîne de filtres de sécurité (SecurityFilterChain) pour l'application.
     * Cette méthode spécifie les règles de gestion des requêtes, désactive la protection CSRF,
     * et ajoute un filtre pour la gestion des tokens JWT.
     *
     * @param http Objet HttpSecurity pour personnaliser la configuration.
     * @return Une instance configurée de SecurityFilterChain.
     * @throws Exception Si une erreur se produit pendant la configuration.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.csrf(AbstractHttpConfigurer::disable) // Désactivation de la protection CSRF.
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests
                            // Permet un accès sans authentification aux endpoints spécifiés.
                            .requestMatchers("/api/utilisateur/connexion", "/api/utilisateur/inscription", "/api/apijoignable").permitAll()
                            // Toutes les autres requêtes nécessitent une authentification.
                            .anyRequest().authenticated()
                )
                .sessionManagement(sessionManagement ->
                        // Utilisation de sessions sans état (stateless) pour une API REST sécurisée.
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider) // Configuration du fournisseur d'authentification.
                .addFilterBefore(filtreJwt, UsernamePasswordAuthenticationFilter.class) // Ajout du filtre JWT avant le filtre d'authentification standard.
                .build();
    }

    /**
     * Configure les règles CORS (Cross-Origin Resource Sharing) pour l'application.
     * Permet aux requêtes provenant de domaines spécifiques (ex. localhost:8080) d'accéder à l'API.
     *
     * @return Une instance de `CorsConfigurationSource` configurée.
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        // Définition des origines autorisées.
        configuration.setAllowedOrigins(List.of("http://localhost:8080"));
        // Définition des méthodes HTTP autorisées.
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE"));
        // Définition des en-têtes autorisés dans les requêtes.
        configuration.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Association des règles CORS à tous les endpoints de l'application.
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }
}
