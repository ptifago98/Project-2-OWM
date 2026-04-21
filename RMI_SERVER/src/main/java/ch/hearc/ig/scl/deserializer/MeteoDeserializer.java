package ch.hearc.ig.scl.deserializer;

import ch.hearc.ig.scl.business.Meteo;
import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;
import java.util.Date;

public class MeteoDeserializer extends JsonDeserializer<Meteo> {
    @Override
    public Meteo deserialize(JsonParser jsonParser, DeserializationContext deserializationContext)
            throws IOException, JacksonException, NullPointerException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        String description = node.get("weather").get(0).get("description").asText();
        Double temperature = node.get("main").get("temp").asDouble();
        Double ressenti = node.get("main").get("feels_like").asDouble();
        int pression = node.get("main").get("pressure").asInt();
            Integer pressionInteger = new Integer(pression);
        Double humidity = node.get("main").get("humidity").asDouble();
        Double ventVitesse = node.get("wind").get("speed").asDouble();
        Double ventOrientation = node.get("wind").get("deg").asDouble();
        String icon = node.get("weather").get(0).get("icon").asText();
        Date date = new Date(node.get("dt").asLong()*1000);
        return new Meteo(description, date, temperature, ressenti, pressionInteger, humidity, ventVitesse, ventOrientation, icon);
    }
}
