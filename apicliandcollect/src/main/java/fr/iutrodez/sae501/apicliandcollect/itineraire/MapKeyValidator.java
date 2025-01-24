package fr.iutrodez.sae501.apicliandcollect.itineraire;

import fr.iutrodez.sae501.apicliandcollect.contact.InterractionBdContact;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;

/**
 * Méthode de validation pour l'annotation MapKeyValidator
 * @author Descriaud Lucas
 */
public class MapKeyValidator implements ConstraintValidator<ValidateMapKeysExistInTable, Map<Long, ?>> {

    @Autowired
    private InterractionBdContact client;

    @Override
    public boolean isValid(Map<Long, ?> value, ConstraintValidatorContext context) {

        for (Long key : value.keySet()) {
            if (!client.existsById(key)) {
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(
                                "Le client : " + key + " n\'existe pas dans la base de données")
                        .addConstraintViolation();
                return false;

            }
        }
        return true ;
    }
}
