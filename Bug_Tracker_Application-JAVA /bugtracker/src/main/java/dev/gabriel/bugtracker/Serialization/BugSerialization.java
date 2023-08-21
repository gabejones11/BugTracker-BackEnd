package dev.gabriel.bugtracker.Serialization;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import dev.gabriel.bugtracker.Model.Bug;

import java.io.IOException;

public class BugSerialization extends JsonSerializer<Bug> {
    @Override
    public void serialize(Bug bug, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeStringField("id", String.valueOf(bug.getId()));
        jsonGenerator.writeStringField("createdByUserId", String.valueOf(bug.getCreatedByUserId()));
        jsonGenerator.writeStringField("summary", bug.getSummary());
        jsonGenerator.writeStringField("description", bug.getDescription());
        jsonGenerator.writeStringField("assignedTo", bug.getAssignedTo());
        jsonGenerator.writeStringField("reportedBy", bug.getReportedBy());
        jsonGenerator.writeStringField("dateReported", bug.getDateReported().toString());
        jsonGenerator.writeStringField("type", bug.getType().toString());
        jsonGenerator.writeStringField("priority", bug.getPriority().toString());
        jsonGenerator.writeObjectField("status", bug.getStatus());
        jsonGenerator.writeEndObject();
    }
}
