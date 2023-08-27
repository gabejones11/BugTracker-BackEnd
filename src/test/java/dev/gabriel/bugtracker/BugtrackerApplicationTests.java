package dev.gabriel.bugtracker;

import dev.gabriel.bugtracker.Model.Bug;
import dev.gabriel.bugtracker.Repository.BugRepository;
import dev.gabriel.bugtracker.Service.BugService;
import org.bson.types.ObjectId;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BugtrackerApplicationTests {

	//create mock repository
	@Mock
	private BugRepository bugRepository;

	//create and inject mock service
	@InjectMocks
	private BugService bugService;

	//Test updating a bug status when it exists
	@Test
	public void testUpdateBugStatus_bugFound() {
		ObjectId bugId = new ObjectId();

		//Bug to update
		Bug existingBug = new Bug();
		existingBug.setStatus(Bug.Status.In_Progress);

		//Updated Bug
		Bug updatedBugDetails = new Bug();
		updatedBugDetails.setStatus(Bug.Status.Done);

		//Mock behavior of the findById() method to return the existing Bug
		Mockito.when(bugRepository.findById(bugId)).thenReturn(Optional.of(existingBug));

		//Call the updateBugStatus method we are testing
		Optional<Bug> result = bugService.updateBugStatus(bugId.toHexString(), updatedBugDetails);

		//Make sure we call the method one time and that the repository saves the updated Bug
		Mockito.verify(bugRepository, Mockito.times(1)).save(existingBug);

		//Assert that there is a result
		assertTrue(result.isPresent());

		//Check that our result has the status of "Done"
		assertEquals(Bug.Status.Done, result.get().getStatus());
	}

	//test updating a bug status when it doesn't exist
	@Test
	public void testUpdateBugStatus_bugNotFound() {
		ObjectId bugId = new ObjectId();

		//Updated Bug
		Bug updatedBugDetails = new Bug();
		updatedBugDetails.setStatus(Bug.Status.In_Progress);

		//Mock behavior of the findById() method to return an empty Optional
		Mockito.when(bugRepository.findById(bugId)).thenReturn(Optional.empty());

		//Call the updateBugStatus method we are testing
		Optional<Bug> result = bugService.updateBugStatus(bugId.toHexString(), updatedBugDetails);

		//Verify we are never calling the save method on anything of Type Bug
		Mockito.verify(bugRepository, Mockito.never()).save(Mockito.any(Bug.class));

		//Check that our result is null
		assertFalse(result.isPresent());
	}

}
