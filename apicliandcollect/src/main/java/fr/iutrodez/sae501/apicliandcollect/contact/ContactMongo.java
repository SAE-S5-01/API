package fr.iutrodez.sae501.apicliandcollect.contact;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "contact")
@TypeAlias("ContactMongo")
public class ContactMongo {

    private long _id;
    private GeoJsonPoint location;

}
