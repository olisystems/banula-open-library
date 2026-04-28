package com.banula.openlib.ocpi.util;

import com.banula.openlib.ocpi.model.vo.GeoLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class GeoLocationDeserializer extends JsonDeserializer<GeoLocation> {

    @Override
    public GeoLocation deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        JsonNode node = p.getCodec().readTree(p);

        if (node == null || node.isNull()) {
            return null;
        }

        // GeoLocationDTO wire format: {"latitude": "49.4788", "longitude": "8.48135"}
        if (node.has("latitude") && node.has("longitude")) {
            String lat = node.get("latitude").asText();
            String lon = node.get("longitude").asText();
            if (lat != null && !lat.isEmpty() && lon != null && !lon.isEmpty()) {
                return new GeoLocation(Double.parseDouble(lat), Double.parseDouble(lon));
            }
            return null;
        }

        // Native GeoJSON format: {"type": "Point", "coordinates": [lat, lon]}
        if (node.has("coordinates") && node.get("coordinates").isArray()) {
            JsonNode coordsNode = node.get("coordinates");
            if (coordsNode.size() >= 2) {
                double lat = coordsNode.get(0).asDouble();
                double lon = coordsNode.get(1).asDouble();
                return new GeoLocation(lat, lon);
            }
        }

        return null;
    }
}
