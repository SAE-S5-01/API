package fr.iutrodez.sae501.apicliandcollect.itineraire;


import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
    public class ItineraireDTO {

    @NotBlank(message = "Le nom d'itinéraire ne peut pas être vide")
    @Size(max = 50, message = "Le nom d'itinéraire peut contenir au maximum 50 caractères")
    private String nomItineraire;


    @Max(message = "La longitude ne peut pas dépasser 180°", value = 180)
    @Min(message = "La longitude ne peut pas être en dessous de -180°", value = -180)
    private double longitude;

    @Max(message = "La latitude ne peut pas dépasser 90°", value = 90)
    @Min(message = "La latitude ne peut pas être en dessous de -90°", value = -90)
    private double latitude;
    }
