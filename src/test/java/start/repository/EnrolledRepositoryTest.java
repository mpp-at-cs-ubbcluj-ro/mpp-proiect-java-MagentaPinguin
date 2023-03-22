package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Enrolled;
import start.domain.Participant;
import start.domain.Trial;

import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EnrolledRepositoryTest {

    @Test
    void testEnrolled(){
        try{

            Properties dbProp=new Properties();
            dbProp.load(new FileReader("db.config"));

            var pRepo= new ParticipantRepository(dbProp);

            var tRepo= new TrialRepository(dbProp);

            var eRepo= new EnrolledRepository(dbProp);

            var participant= new Participant("Participant","0001",14);
            pRepo.add(participant);
            var foundPID=pRepo.findByCnp("0001").get().getId();
            participant.setId(foundPID);

            var trial1= new Trial("Citire A",6,16);
            var trial2= new Trial("Citire B",1,15);
            tRepo.add(trial1);
            var foundTrialId1=tRepo.getSpecificTrial("Citire A",6,16).get().getId();
            trial1.setId(foundTrialId1);

            tRepo.add(trial2);
            var foundTrialId2=tRepo.getSpecificTrial("Citire B",1,15).get().getId();
            trial2.setId(foundTrialId2);

            eRepo.add(new Enrolled(participant,trial1));
            eRepo.add(new Enrolled(participant,trial2));

            assertEquals("Citire A",eRepo.getTrialsFor(pRepo.findByCnp("0001").get()).get(0).getName());
            assertEquals("Participant",eRepo.getEnrolledAt(tRepo.getSpecificTrial("Citire A",6,16).get()).get(0).getName());

            assertEquals(2, eRepo.getAll().size());
            pRepo.delete(foundPID);
            tRepo.delete(foundTrialId1);
            tRepo.delete(foundTrialId2);

        } catch (RepositoryException | IOException e) {
            throw new RuntimeException(e);
        }


    }

}