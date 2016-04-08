package model;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;

/**
 * Created by JeanV on 21/03/2016.
 */
public class SleepProfileSerializer extends JsonSerializer<SleepProfile> {
    @Override
    public void serialize(SleepProfile sP, JsonGenerator jGen, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
        jGen.writeStartObject();

        jGen.writeStringField("name", sP.getName());

        jGen.writeArrayFieldStart("sleepItems");
        for (SleepItem sI :
                sP.getSleepItems()) {
            jGen.writeStartObject();
            jGen.writeStringField("begin", sI.getBegin().toString());
            jGen.writeStringField("end", sI.getEnd().toString());

            if (sI.getAmount() == null)
                jGen.writeStringField("amount", "");
            else
                jGen.writeStringField("amount", sI.getAmount().toString());

            jGen.writeBooleanField("alone", sI.getAlone());
            jGen.writeStringField("where", sI.getWhere());
            jGen.writeEndObject();
        }

        jGen.writeEndArray();
        jGen.writeEndObject();
    }
}