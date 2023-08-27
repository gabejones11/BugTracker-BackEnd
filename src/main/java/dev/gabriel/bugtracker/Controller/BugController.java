package dev.gabriel.bugtracker.Controller;

import dev.gabriel.bugtracker.Model.Bug;
import dev.gabriel.bugtracker.Service.BugService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/bugs")
public class BugController {

    @Autowired
    private BugService bugService;

    @GetMapping()
    public ResponseEntity<List<Bug>> getAllBugs() {
       return new ResponseEntity<List<Bug>>(bugService.allBugs(), HttpStatus.OK);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Bug>> getBugsByUserId(@PathVariable String userId) {
        List<Bug> bugs = bugService.getBugsByUserId(userId);
        return new ResponseEntity<>(bugs, HttpStatus.OK);
    }

    @GetMapping("/{bugId}")
    public ResponseEntity<Optional<Bug>> getSingleBug(@PathVariable String bugId) {
        return new ResponseEntity<Optional<Bug>>(bugService.singleBug(bugId), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<Bug> addBug(@RequestBody Bug bug, @RequestParam("createdByUserId") String createdByUserId) {
        Bug newBug = bugService.addBug(bug, createdByUserId);
        return new ResponseEntity<Bug>(newBug, HttpStatus.CREATED);
    }

    @PutMapping("/{bugId}")
    public ResponseEntity<Optional<Bug>> updateBug(@PathVariable String bugId, @RequestBody Bug bugDetails) {
        Optional<Bug> updatedBug = bugService.updateBug(bugId, bugDetails);
        return new ResponseEntity<Optional<Bug>>(updatedBug, HttpStatus.OK);
    }

    @PutMapping("/update/status/{bugId}")
    public ResponseEntity<Optional<Bug>> updateBugStatus(@PathVariable String bugId, @RequestBody Bug bugDetails) {
        Optional<Bug> updatedBug = bugService.updateBugStatus(bugId, bugDetails);
        return new ResponseEntity<Optional<Bug>>(updatedBug, HttpStatus.OK);
    }

    @DeleteMapping("/{bugId}")
    public ResponseEntity<Void> deleteBug(@PathVariable String bugId){
        bugService.deleteBug(bugId);
        return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Bug>> searchBugsByTitle(@RequestParam String query) {
        List<Bug> matchingBugs = bugService.searchBugsByTitle(query);
        return new ResponseEntity<List<Bug>>(matchingBugs, HttpStatus.OK);
    }
}
