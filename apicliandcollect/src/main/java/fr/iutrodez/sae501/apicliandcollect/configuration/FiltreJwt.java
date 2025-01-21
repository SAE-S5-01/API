package fr.iutrodez.sae501.apicliandcollect.configuration;

import fr.iutrodez.sae501.apicliandcollect.securite.ServiceJwt;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;

/**
 * Classe `FiltreJwt` qui agit comme un filtre pour traiter les requêtes HTTP.
 * Ce filtre s'exécute une seule fois par requête (hérite de `OncePerRequestFilter`).
 * Il valide le jeton JWT présent dans l'en-tête "Authorization" et configure le contexte de sécurité.
 *
 * @Author Descriaud Lucas
 */
@Component
public class FiltreJwt extends OncePerRequestFilter {

    // Gestionnaire des exceptions, utilisé pour résoudre et traiter les erreurs.
    private final HandlerExceptionResolver handlerExceptionResolver;

    // Service pour manipuler et valider les tokens JWT.
    private final ServiceJwt serviceJwt;

    // Service pour charger les informations d'utilisateur à partir du username (email).
    private final UserDetailsService userDetailsService;

    /**
     * Constructeur pour injecter les dépendances nécessaires.
     *
     * @param jwtService Instance de `ServiceJwt` pour gérer les JWT.
     * @param userDetailsService Service pour charger les détails de l'utilisateur.
     * @param handlerExceptionResolver Gestionnaire pour résoudre les exceptions survenues dans le filtre.
     */
    public FiltreJwt(
            ServiceJwt jwtService,
            UserDetailsService userDetailsService,
            HandlerExceptionResolver handlerExceptionResolver
    ) {
        this.serviceJwt = jwtService;
        this.userDetailsService = userDetailsService;
        this.handlerExceptionResolver = handlerExceptionResolver;
    }

    /**
     * Méthode principale qui intercepte chaque requête HTTP et applique la logique de traitement du JWT.
     *
     * @param request Requête HTTP entrante.
     * @param response Réponse HTTP sortante.
     * @param filterChain Chaîne de filtres, pour déléguer la requête au prochain filtre.
     * @throws ServletException En cas d'erreur liée à un traitement des servlets.
     * @throws IOException En cas d'erreur d'entrée/sortie.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Récupère l'en-tête "Authorization" contenant le JWT.
        final String authHeader = request.getHeader("Authorization");

        // Si l'en-tête est absent ou ne commence pas par "Bearer ", passe au prochain filtre.
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // Extrait le token JWT de l'en-tête (sans "Bearer ").
            final String jwt = authHeader.substring(7);
            // Extrait l'email ou le nom d'utilisateur du token JWT.
            final String userEmail = serviceJwt.extractUsername(jwt);

            // Vérifie si l'utilisateur n'est pas déjà authentifié.
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            if (userEmail != null && authentication == null) {
                // Charge les détails de l'utilisateur à partir de l'email.
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                // Valide le token JWT en fonction des détails utilisateur.
                if (serviceJwt.isTokenValid(jwt, userDetails)) {
                    // Crée un token d'authentification pour l'utilisateur.
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null, // Aucun mot de passe requis.
                            userDetails.getAuthorities() // Autorisations associées à l'utilisateur.
                    );

                    // Associe les informations de la requête (comme l'adresse IP).
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    // Ajoute le token d'authentification au contexte de sécurité.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

            // Passe au prochain filtre dans la chaîne.
            filterChain.doFilter(request, response);
        } catch (Exception exception) {
            // Si une exception se produit, utilise le gestionnaire pour la traiter.
            handlerExceptionResolver.resolveException(request, response, null, exception);
        }
    }
}
