package dev.gabriel.bugtracker.Repository;

import dev.gabriel.bugtracker.Model.Bug;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BugRepository extends MongoRepository<Bug, String> {
    Optional<Bug> findById(ObjectId _id);

    @Override
    default <S extends Bug> S insert(S bug) {
        String bugId = UUID.randomUUID().toString();
        return save(bug);
    }

    List<Bug> findByCreatedByUserId(ObjectId id);
}
