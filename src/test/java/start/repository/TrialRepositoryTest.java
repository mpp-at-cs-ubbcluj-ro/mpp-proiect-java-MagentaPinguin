package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Trial;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class TrialRepositoryTest {
    @Test
    void testTrialRepo(){
        try {
            var dbProp=new Properties();
            dbProp.load(new FileReader("db.config"));
            var trialRepo=new TrialRepository(dbProp);
            var trial1=new Trial("Trial1",0,6);
            var trial2=new Trial("Trial2",0,6);
            var trial3=new Trial("Trial1",7,12);
            var trial4=new Trial("Trial2",7,12);
            trialRepo.add(trial1);
            trialRepo.add(trial2);
            trialRepo.add(trial3);
            trialRepo.add(trial4);
            assertEquals(4,trialRepo.getAll().size());
            assertEquals(2,trialRepo.getTrialsForAge(8).size());
            var specific=trialRepo.getSpecificTrial("Trial1",7,12).get();
            assertEquals(specific.getMinAge(),trial3.getMinAge());
            var updated=new Trial("UpdatedTrail",3,12);
            updated.setId(specific.getId());
            trialRepo.update(updated);
            assertEquals(Optional.empty(),trialRepo.getSpecificTrial("Trial1",7,12));;

            trialRepo.getAll().forEach(trial ->
                    assertDoesNotThrow(
                            ()->trialRepo.delete(trial.getId()),"Deleted"+trial.getName()));

        } catch (IOException | RepositoryException e) {
            assert false;
        }


    }

}