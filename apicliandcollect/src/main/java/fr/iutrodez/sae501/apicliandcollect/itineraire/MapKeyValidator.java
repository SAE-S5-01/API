/*
 * MapKeyValidator.java                                                                                     04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.InteractionBdContact;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;

import java.util.Map;

/**
 * Méthode de validation pour l'annotation MapKeyValidator
 * @author Descriaud Lucas
 */
public class MapKeyValidator implements ConstraintValidator<ValidateMap, Map<Long, Point>> {

    @Autowired
    private InteractionBdContact client;

    private static final int MAX_X = 180;
    private static final int MIN_X = -180;
    private static final int MAX_Y = 90;
    private static final int MIN_Y = -90;

    @Override
    public boolean isValid(Map<Long, Point> value, ConstraintValidatorContext context) {

        context.disableDefaultConstraintViolation(); // Désactiver les violations par défaut
        StringBuilder listeErreurs = new StringBuilder(); // Accumulateur pour les messages d'erreur
        boolean erreur = false;

        for (Map.Entry<Long, Point> entry : value.entrySet()) {
            Long key = entry.getKey();
            Point point = entry.getValue();

            // Vérification si le client existe
            if (!client.existsById(key)) {
                listeErreurs.append("Le client : ").append(key).append(" n'existe pas dans la base de données. ");
                erreur = true;
            }

            // Vérification des coordonnées
            if (point.getX() < MIN_X || point.getX() > MAX_X || point.getY() < MIN_Y || point.getY() > MAX_Y) {
                listeErreurs.append("Les coordonnées du client : ").append(key)
                        .append(" sont incorrectes (x: ").append(point.getX())
                        .append(", y: ").append(point.getY()).append("). ");
                erreur = true;
            }
        }

        // Ajout du message d'erreur global si des erreurs sont détectées
        if (erreur) {
            context.buildConstraintViolationWithTemplate(listeErreurs.toString().trim())
                    .addConstraintViolation();
        }

        return !erreur; // Retourne false si une erreur est détectée
    }
}
