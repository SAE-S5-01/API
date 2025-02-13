package fr.iutrodez.sae501.apicliandcollect.securite;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service responsable de la gestion des tokens JWT (JSON Web Tokens).
 * Fournit des méthodes pour générer, extraire, et valider des tokens
 * dans le contexte de l'authentification et de la sécurité.
 *
 * @author Descriaud Lucas
 */
@Service
public class ServiceJwt {

    // Clé secrète utilisée pour signer et valider les tokens, injectée depuis les propriétés de configuration.
    @Value("${security.jwt.secret-key}")
    private String secretKey;

    // Temps d'expiration des tokens JWT (en millisecondes), injecté depuis les propriétés de configuration.
    @Value("${security.jwt.expiration-time}")
    private long jwtExpiration;

    /**
     * Extrait le nom d'utilisateur (ou tout autre identifiant) du token JWT.
     *
     * @param token Le token JWT dont on souhaite extraire le nom d'utilisateur.
     * @return Le nom d'utilisateur contenu dans le token.
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extrait une donnée spécifique (claim) du token JWT en utilisant une fonction de transformation.
     *
     * @param token Le token JWT dont on souhaite extraire un claim.
     * @param claimsResolver Fonction qui spécifie le claim à extraire.
     * @param <T> Type du claim extrait.
     * @return La valeur du claim extrait.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Génère un token JWT sans claims supplémentaires, à partir des détails de l'utilisateur.
     *
     * @param userDetails Les détails de l'utilisateur pour lequel le token est généré.
     * @return Un token JWT signé.
     */
    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Génère un token JWT avec des claims supplémentaires, à partir des détails de l'utilisateur.
     *
     * @param extraClaims Claims supplémentaires à inclure dans le token.
     * @param userDetails Les détails de l'utilisateur pour lequel le token est généré.
     * @return Un token JWT signé.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpiration);
    }

    /**
     * Récupère la durée d'expiration configurée pour les tokens JWT.
     *
     * @return La durée d'expiration en millisecondes.
     */
    public long getExpirationTime() {
        return jwtExpiration;
    }

    /**
     * Construit un token JWT en incluant des claims supplémentaires, une date d'expiration,
     * et les détails de l'utilisateur.
     *
     * @param extraClaims Claims supplémentaires à inclure dans le token.
     * @param userDetails Les détails de l'utilisateur pour lequel le token est généré.
     * @param expiration Durée d'expiration du token en millisecondes.
     * @return Un token JWT signé.
     */
    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expiration
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis())) // Date de création du token.
                .setExpiration(new Date(System.currentTimeMillis() + expiration)) // Date d'expiration.
                .signWith(getSignInKey(), SignatureAlgorithm.HS256) // Signature avec l'algorithme HMAC SHA-256.
                .compact();
    }

    /**
     * Vérifie si un token JWT est valide.
     * La validité dépend de la correspondance entre le nom d'utilisateur dans le token
     * et celui dans les `UserDetails`, ainsi que de l'expiration du token.
     *
     * @param token Le token JWT à valider.
     * @param userDetails Les détails de l'utilisateur correspondant au token.
     * @return `true` si le token est valide, `false` sinon.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    /**
     * Vérifie si un token JWT a expiré.
     *
     * @param token Le token JWT à vérifier.
     * @return `true` si le token a expiré, `false` sinon.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extrait la date d'expiration du token JWT.
     *
     * @param token Le token JWT dont on souhaite extraire la date d'expiration.
     * @return La date d'expiration du token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Extrait l'ensemble des claims (données) contenus dans un token JWT.
     *
     * @param token Le token JWT à analyser.
     * @return Les claims contenus dans le token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey()) // Clé utilisée pour la signature.
                .build()
                .parseClaimsJws(token) // Analyse et vérifie la signature du token.
                .getBody();
    }

    /**
     * Récupère la clé secrète utilisée pour signer les tokens JWT.
     * La clé est générée à partir d'une chaîne encodée en Base64.
     *
     * @return La clé secrète sous forme d'objet `Key`.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey); // Décodage de la clé depuis sa représentation Base64.
        return Keys.hmacShaKeyFor(keyBytes); // Génération de la clé compatible avec HMAC SHA-256.
    }
}
