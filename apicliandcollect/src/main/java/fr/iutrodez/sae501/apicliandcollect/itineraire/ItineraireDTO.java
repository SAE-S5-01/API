/*
 * ItineraireDTO.java                                                                                      04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.ContactDTO;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.InteractionBdUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.UtilisateurDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.LinkedHashMap;

@Setter
@Getter
public class ItineraireDTO {

    private Point domicile;

    @UniqueName
    @Size(max = 50, message = "Le nom de l'itinéraire ne doit pas dépasser 50 caractères")
    private String nomItineraire;

    @NotNull(message = "La liste des clients ordonnée est obligatoire")
    @ValidateMap
    @Size(max = 8, message = "L'itinéraire doit contenir au plus 8 clients")
    private LinkedHashMap<Long , Point> listePoint;

    @Constraint(validatedBy = ItineraireDTO.NameUniqueValidator.class)
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UniqueName {
        String message() default "Vous avez déjà utilisé ce nom d'itinéraire";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Component
    public static class NameUniqueValidator implements ConstraintValidator<ItineraireDTO.UniqueName, String> {
        @Autowired
        private InteractionMongoItineraire interactionMongoItineraire;

        @Override
        public boolean isValid(String nomItineraire, ConstraintValidatorContext context) {

            Utilisateur utilisateur = (Utilisateur) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            return nomItineraire != null && !interactionMongoItineraire.existsByNomItineraireAndIdCreateur(nomItineraire, utilisateur.getId());
        }
    }
}


