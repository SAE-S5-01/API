package fr.iutrodez.sae501.apicliandcollect.contact;

import jakarta.validation.Constraint;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import jakarta.validation.Payload;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
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
    @NotBlank(message = "Le nom de votre contact ne peut pas etre vide")
    @Size(max = 50, message = "Le nom de votre contact peut contenir au maximum de 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom de votre contact ne peut pas etre vide")
    @Size(max = 50, message = "Le prénom de votre contact peut contenir au maximum de 50 caractères")
    private String prenom;

    @NotBlank(message = "Le numero de telephone du contact ne peut pas etre vide")
    @Pattern(regexp = "^(?:\\+33|0)?([1-9]\\d{8})$",

            message = "Le numéro de téléphone du client doit etre au format francais. Exemple : +33 6 12 34 56 78"
    )
    private String telephone;

    // TODO verifier existence de l'adresse
    @NotBlank(message = "L'adresse postale du contact ne peut pas etre vide")
    @Size(max = 100, message = "L'adresse postale du contact peut  contenir au maximum de 100 caractères")
    private String adresse;

    @Size(max = 500, message = "La description du contact peut contenir au maximum de 500 caractères")
    private String description;

    // TODO coordonnées de l'utilisateur
    // peut etre pas necessaire si c'est le back de l'app mobile qui s'assure de ca ?
    private double longitude;
    private double latitude;

    
}