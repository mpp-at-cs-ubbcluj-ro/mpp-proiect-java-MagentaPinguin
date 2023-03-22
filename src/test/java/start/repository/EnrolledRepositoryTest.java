package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Enrolled;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class EnrolledRepositoryTest {
    @Test
    void testEnrollmentRepo() throws IOException, RepositoryException {

        Properties properties=new Properties();
        properties.load(new FileReader("db.config"));

        var pRepo= new ParticipantRepository(properties);
        var tRepo=new TrialRepository(properties);
        var enrollRepo=new EnrolledRepository(pRepo,tRepo,properties);


        var p=pRepo.findByCnp("0000").get();
        var t=tRepo.findByName("Test").get();

        var original=new Enrolled(p,t);

       // assertDoesNotThrow(()->enrollRepo.add(original),"Add success");
        assertNotEquals(0,enrollRepo.getTrialsFor(p).size());
        System.out.println(enrollRepo.getTrialsFor(p).get(0).getName());

        // System.out.println(enrollRepo.getTrialsFor(p).size());
        /*System.out.println(enrollRepo.getAll().get(0).toString());
        //assertNotEquals(0,enrollRepo.getAll().size());

    /*    var foundP=enrollRepo.getEnrolledAt(tRepo.find(0L).get());
        System.out.println(foundP.get(0).getName());



        var foundT=enrollRepo.getTrialsFor(pRepo.find(1L).get());
        System.out.println(foundT.get(0).getName());


        assertNotEquals(0,foundT.size());
        assertNotEquals(0,foundP.size());

        assertEquals("Test",
                enrollRepo.findByTrialParticipant(
                        foundP.get(0).getId(),
                        foundT.get(0).getId()).get().getTrial().getName());
        var found=enrollRepo.findByTrialParticipant(
                foundP.get(0).getId(),
                foundT.get(0).getId()).get();
        assertDoesNotThrow(()->enrollRepo.delete(found.getId()),"Deleted success");
    */
    }

}