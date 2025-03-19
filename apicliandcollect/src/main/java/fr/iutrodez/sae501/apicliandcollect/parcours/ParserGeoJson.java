package fr.iutrodez.sae501.apicliandcollect.parcours;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.io.IOException;

public class ParserGeoJson extends JsonDeserializer<GeoJsonPoint> {

    @Override
    public GeoJsonPoint deserialize(JsonParser jsonParser, DeserializationContext ctxt)
            throws IOException {
        if (jsonParser.getCurrentToken() == JsonToken.VALUE_NULL) {
            return null;
        }

        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        if (node == null || node.isNull()) {
            return null;
        }

        double latitude = node.get("latitude").asDouble();
        double longitude = node.get("longitude").asDouble();
        return new GeoJsonPoint(longitude, latitude);
    }
}
