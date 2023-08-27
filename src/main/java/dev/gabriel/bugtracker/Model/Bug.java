package dev.gabriel.bugtracker.Model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import dev.gabriel.bugtracker.Serialization.BugSerialization;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Document(collection = "bugs")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonSerialize(using = BugSerialization.class)
public class Bug {

    public enum Type {
        Story,
        Task,
        Bug
    }

    public enum Priority {
        Low,
        Medium,
        High
    }

    public enum Status {
        Backlog,
        Selected_For_Development,
        In_Progress,
        Done
    }

    @Id
    private ObjectId id;

    private ObjectId createdByUserId;

    private String summary;

    private String description;

    private String assignedTo;

    private String reportedBy;

    private Date dateReported;

    private Type type;

    private Priority priority;

    private Status status;
}
