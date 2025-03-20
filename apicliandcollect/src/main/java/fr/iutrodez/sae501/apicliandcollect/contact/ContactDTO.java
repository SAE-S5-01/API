/*
 * ContactDTO.java                                                                                          04 fev. 2025
 * IUT de Rodez, pas de copyright ni de "copyleft".
 */

package fr.iutrodez.sae501.apicliandcollect.contact;

import fr.iutrodez.sae501.apicliandcollect.utilisateur.InteractionBdUtilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.Utilisateur;
import fr.iutrodez.sae501.apicliandcollect.utilisateur.UtilisateurDTO;
import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.Nullable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Setter
@Getter
public class ContactDTO {

    @NotBlank(message = "Le nom de l'entreprise ne peut pas être vide")
    @Size(max = 50, message = "Le nom de l'entreprise peut contenir au plus 50 caractères")
    private String nomEntreprise;

    @Size(max = 50, message = "Le nom de votre contact peut contenir au plus 50 caractères")
    private String nomContact;

    @Size(max = 50, message = "Le prénom de votre contact peut contenir au plus 50 caractères")
    private String prenomContact;

    @Nullable
    @Pattern(regexp = "^((?:\\+33|0)?([1-9]\\d{8}))$|^$",
        message = "Le numéro de téléphone du client doit être au format français (ex. : +33612345678)"
    )
    private String telephone;

    private Long ID;

    @NotBlank(message = "L'adresse postale de l'entreprise ne peut pas être vide")
    @Size(max = 300, message = "L'adresse postale de l'entreprise peut contenir au plus 300 caractères")
    private String adresse;

    @Size(max = 500, message = "La description du contact peut contenir au plus de 500 caractères")
    private String description;

    @Max(message = "La longitude ne peut pas dépasser 180°", value = 180)
    @Min(message = "La longitude ne peut pas être en-dessous de -180°", value = -180)
    private double longitude;

    @Max(message = "La latitude ne peut pas dépasser 90°", value = 90)
    @Min(message = "La latitude ne peut pas être en-dessous de -90°", value = -90)
    private double latitude;

    private boolean prospect;

}