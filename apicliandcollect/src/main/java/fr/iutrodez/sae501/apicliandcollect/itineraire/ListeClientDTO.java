package fr.iutrodez.sae501.apicliandcollect.itineraire;


import fr.iutrodez.sae501.apicliandcollect.contact.InterractionMongoContact;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.LinkedHashMap;
import java.util.Map;

@Setter
@Getter
public class ListeClientDTO {

    @ValidateMapKeysExistInTable
    private LinkedHashMap<Long , GeoJsonPoint> listePoint;

}


