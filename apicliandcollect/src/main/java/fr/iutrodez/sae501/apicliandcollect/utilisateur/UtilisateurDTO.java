package fr.iutrodez.sae501.apicliandcollect.utilisateur;

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
public class UtilisateurDTO {
    // Getters and Setters
    @NotBlank(message = "Votre nom est obligatoire")
    @Size(max = 50, message = "Votre nom ne peut pas contenir plus de 50 caractères")
    private String nom;

    @NotBlank(message = "Votre prénom est obligatoire")
    @Size(max = 50, message = "Votre prénom ne peut pas contenir plus de 50 caractères")
    private String prenom;

    @NotBlank(message = "Votre email est obligatoire")
    @Email(message = "Adresse email non valide : ex. johndoe@gmail.com")
    // TODO revoir nombre max de caractères
    @Size(max = 50, message = "Votre email ne peut pas contenir plus de 50 caractères")
    @UniqueEmail
    private String mail;

    // TODO Réunion sur les caractères spéciaux à autoriser
    // TODO Retoucher message d'erreur  en fonction des caractères spéciaux autorisés
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,20}$",
            message = "Le mot de passe doit contenir entre 8 et 20 caractères, avec au moins une lettre majuscule, " +
                    "une lettre minuscule, un chiffre et un caractère spécial"
    )
    private String motDePasse;

    // TODO verifier existence de l'adresse
    @NotBlank(message = "Votre adresse est obligatoire")
    private String adresse;

    @Max(message = "La longitude ne peut pas dépasser 180°", value = 180)
    @Min(message = "La longitude ne peut pas être en dessous de -180°", value = -180)
    private double longitude;

    @Max(message = "La latitude ne peut pas dépasser 90°", value = 90)
    @Min(message = "La latitude ne peut pas être en dessous de -90°", value = -90)
    private double latitude;

    @Constraint(validatedBy = EmailUniqueValidator.class)
    @Target({ ElementType.METHOD, ElementType.FIELD, ElementType.ANNOTATION_TYPE, ElementType.PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UniqueEmail {
        String message() default "Mail déjà utilisé";
        Class<?>[] groups() default {};
        Class<? extends Payload>[] payload() default {};
    }

    @Component
    public static class EmailUniqueValidator implements ConstraintValidator<UniqueEmail, String> {
        @Autowired
        private InterractionBdUtilisateur interractionBdUtilisateur;

        @Override
        public boolean isValid(String mail, ConstraintValidatorContext context) {
            return mail != null && !interractionBdUtilisateur.existsByMail(mail);
        }
    }
}