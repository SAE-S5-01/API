/*
 * ParcoursDTO.java                                      13 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.parcours;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import lombok.Getter;
import lombok.Setter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Date;

@Setter
@Getter
public class ParcoursDTO {

    public final static String STATUT_INVALIDE = "Le statut fourni n'est pas valide "
        + "(EN_COURS, EN_PAUSE, ARRETE, TERMINE)";

    private Long id;

    @ValidStatutParcours
    private StatutParcours statut;

    private Date dateCreation = new Date();

    private String idItineraire;

    private Long idDernierContactVisite;

    /**
     * Annotation pour valider le statut d'un parcours
     */
    @Target({ ElementType.FIELD, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    @Constraint(validatedBy = StatutParcoursValidator.class)
    public @interface ValidStatutParcours {
        String message() default STATUT_INVALIDE;  // Message d'erreur par défaut
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    /**
     * Validateur pour le statut d'un parcours
     */
    public static class StatutParcoursValidator implements ConstraintValidator<ValidStatutParcours, StatutParcours> {

        @Override
        public void initialize(ValidStatutParcours constraintAnnotation) {
            // Pas d'initialisation nécessaire
        }

        @Override
        public boolean isValid(StatutParcours statut, ConstraintValidatorContext context) {
            if (statut == null) {
                return false;  // Si le statut est nul
            }

            // Vérification que le statut appartient bien à l'énumération
            try {
                StatutParcours.valueOf(statut.name());
                return true;
            } catch (IllegalArgumentException e) {
                return false;
            }
        }
    }
    
}