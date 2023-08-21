package dev.gabriel.bugtracker.Service;

import dev.gabriel.bugtracker.Model.Bug;
import dev.gabriel.bugtracker.Repository.BugRepository;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BugService {
    @Autowired
    private BugRepository bugRepository;

    public List<Bug> allBugs(){
        return bugRepository.findAll();
    }

    public Optional<Bug> singleBug(String bugId) {
        ObjectId id = new ObjectId(bugId);
        return bugRepository.findById(id);
    }

    public Bug addBug(Bug bug, String createdByUserId) {
        return bugRepository.insert(bug);
    }

    public Optional<Bug> updateBug(String bugId, Bug bugDetails) {
        ObjectId id = new ObjectId(bugId);
        Optional<Bug> bug = bugRepository.findById(id);
        if (bug.isPresent()) {
            Bug updatedBug = bug.get();
            updatedBug.setSummary(bugDetails.getSummary());
            updatedBug.setDescription(bugDetails.getDescription());
            updatedBug.setAssignedTo(bugDetails.getAssignedTo());
            updatedBug.setReportedBy(bugDetails.getReportedBy());
            updatedBug.setDateReported(bugDetails.getDateReported());
            updatedBug.setType(bugDetails.getType());
            updatedBug.setPriority(bugDetails.getPriority());
            updatedBug.setStatus(bugDetails.getStatus());
            bugRepository.save(updatedBug);
        }

        return bug;
    }

    public Optional<Bug> updateBugStatus(String bugId, Bug bugDetails) {
        ObjectId id = new ObjectId(bugId);
        Optional<Bug> bug = bugRepository.findById(id);
        if (bug.isPresent()) {
            Bug updatedBug = bug.get();
            updatedBug.setStatus(bugDetails.getStatus());
            bugRepository.save(updatedBug);
        }

        return bug;
    }

    public void deleteBug(String bugId) {
        ObjectId id = new ObjectId(bugId);
        Optional<Bug> bug = bugRepository.findById(id);
        if (bug.isPresent()) {
            Bug bugToDelete = bug.get();
            bugRepository.delete(bugToDelete);
        }
    }

    public List<Bug> searchBugsByTitle(String searchQuery) {
        List<Bug> bugs = bugRepository.findAll();
        List<Bug> matchingBugs = new ArrayList<>();

        for (Bug bug : bugs) {
            if (bug.getSummary().toLowerCase().contains(searchQuery.toLowerCase())) {
                matchingBugs.add(bug);
            }
        }

        return matchingBugs;
    }

    public List<Bug> getBugsByUserId(String userId) {
        ObjectId id = new ObjectId(userId);
        return bugRepository.findByCreatedByUserId(id);
    }
}

