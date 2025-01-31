package fr.iutrodez.sae501.apicliandcollect.contact;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
    public class ContactDTO {
    // Getters and Setters
    @NotBlank(message = "Le nom de contact ne peut pas être vide")
    @Size(max = 50, message = "Le nom de contact/ d'entreprise peut contenir au maximum 50 caractères")
    private String nomEntreprise;

    //@NotBlank(message = "Le nom de votre contact ne peut pas eêtre vide")
    @Size(max = 50, message = "Le nom de votre contact peut contenir au maximum de 50 caractères")
    private String nomContact;

    //@NotBlank(message = "Le prénom de votre contact ne peut pas être vide")
    @Size(max = 50, message = "Le prénom de votre contact peut contenir au maximum de 50 caractères")
    private String prenomContact;

    //@NotBlank(message = "Le numero de telephone du contact ne peut pas être vide")
    @Pattern(regexp = "^(?:\\+33|0)?([1-9]\\d{8})$",

            message = "Le numéro de téléphone du client doit etre au format francais. Exemple : +33 6 12 34 56 78"
    )
    private String telephone;

    private Long ID;

    // TODO verifier existence de l'adresse
    @NotBlank(message = "L'adresse postale du contact ne peut pas être vide")
    @Size(max = 300, message = "L'adresse postale du contact peut  contenir un maximum de 300 caractères")
    private String adresse;

    @Size(max = 500, message = "La description du contact peut contenir au maximum de 500 caractères")
    private String description;

    @Max(message = "La longitude ne peut pas dépasser 180°", value = 180)
    @Min(message = "La longitude ne peut pas être en dessous de -180°", value = -180)
    private double longitude;

    @Max(message = "La latitude ne peut pas dépasser 90°", value = 90)
    @Min(message = "La latitude ne peut pas être en dessous de -90°", value = -90)
    private double latitude;

    private boolean prospect;

    private long id;
    
}