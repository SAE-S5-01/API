/*
 * ValidateMap.java                                                                                         04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation pour vérifier que les clés d'une map représentant des <contacts , coordonnées>
 * sont bien présentes dans la table de contact.
 *
 * @author Lucas DESCRIAUD
 */
@Constraint(validatedBy = MapKeyValidator.class)
@Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateMap {
    String message() default "${message}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
