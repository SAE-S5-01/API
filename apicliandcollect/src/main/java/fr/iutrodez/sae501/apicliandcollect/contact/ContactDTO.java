package fr.iutrodez.sae501.apicliandcollect.contact;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Setter
@Getter
    public class ContactDTO {
    // Getters and Setters
    @NotBlank(message = "Le nom de contact ne peut pas être vide")
    @Size(max = 50, message = "Le nom de contact/ d'entreprise peut contenir au maximum 50 caractères")
    private String entreprise;

    @NotBlank(message = "Le nom de votre contact ne peut pas eêtre vide")
    @Size(max = 50, message = "Le nom de votre contact peut contenir au maximum de 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom de votre contact ne peut pas être vide")
    @Size(max = 50, message = "Le prénom de votre contact peut contenir au maximum de 50 caractères")
    private String prenom;

    @NotBlank(message = "Le numero de telephone du contact ne peut pas être vide")
    @Pattern(regexp = "^(?:\\+33|0)?([1-9]\\d{8})$",

            message = "Le numéro de téléphone du client doit etre au format francais. Exemple : +33 6 12 34 56 78"
    )
    private String telephone;

    // TODO verifier existence de l'adresse
    @NotBlank(message = "L'adresse postale du contact ne peut pas être vide")
    @Size(max = 100, message = "L'adresse postale du contact peut  contenir au maximum de 100 caractères")
    private String adresse;

    @Size(max = 500, message = "La description du contact peut contenir au maximum de 500 caractères")
    private String description;

    @NotBlank(message = "La longitude ne peut pas être vide")
    @Max(message = "La longitude ne peut pas dépasser 180°", value = 180)
    @Min(message = "La longitude ne peut pas être en dessous de -180°", value = -180)
    private double longitude;

    @NotBlank(message = "La latitude ne peut pas être vide")
    @Max(message = "La latitude ne peut pas dépasser 90°", value = 90)
    @Min(message = "La latitude ne peut pas être en dessous de -90°", value = -90)
    private double latitude;

    
}