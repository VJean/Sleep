package model;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.TextNode;

import java.io.IOException;
import java.util.*;

/**
 * Created by JeanV on 21/03/2016.
 */
public class SleepProfileDeserializer extends JsonDeserializer<SleepProfile> {
    @Override
    public SleepProfile deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JsonProcessingException {
        JsonNode node = jsonParser.getCodec().readTree(jsonParser);
        // get name
        String name = node.get("name").textValue();

        SortedSet<String> places = new TreeSet<>();

        // get items
        ArrayList<SleepItem> sleepItems = new ArrayList<>();
        ArrayNode sleepItemsNode = (ArrayNode) node.get("sleepItems");
        for (JsonNode sn : sleepItemsNode){
            String begin = sn.get("begin").textValue();
            String end = sn.get("end").textValue();
            String amount = sn.get("amount").textValue();

            String where = sn.get("where").textValue();
            places.add(where);
            boolean alone = sn.get("alone").booleanValue();

            sleepItems.add(new SleepItem(begin,end,amount,alone,where));
        }

        return new SleepProfile(name,places,sleepItems);
    }
}
