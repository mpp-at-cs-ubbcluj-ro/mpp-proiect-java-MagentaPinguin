package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Participant;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantRepositoryTest {

    @Test
    void testParticipants() throws IOException {
        try {
        var dbProp=new Properties();
        dbProp.load(new FileReader("db.config"));
        var participantRepository =new ParticipantRepository(dbProp);

        var p1=new Participant("A","0000",12);
        var p2=new Participant("A","0001",5);
        var p3=new Participant("D","0002",6);
        assertDoesNotThrow(()->participantRepository.add(p1),"Add pass");
        assertDoesNotThrow(()->participantRepository.add(p2),"Add pass");
        assertDoesNotThrow(()->participantRepository.add(p3),"Add pass");
        assertThrows(RepositoryException.class,()->participantRepository.add(p3));
        //Add
        var found=participantRepository.findByCnp("0001").get();
        var foundId=found.getId();
        assertEquals("A",found.getName());
        //Finds
        var all=participantRepository.getAll();
        assertEquals(3,all.size(),"Get all pass");
        //Get all
        var updated=new Participant("Up","0000222",7);
        updated.setId(found.getId());
        assertDoesNotThrow(()->participantRepository.update(updated),"Updated pass");
        assertEquals("Up",participantRepository.find(foundId).get().getName());
        //Update
            all.forEach(e->assertDoesNotThrow(
                    ()->participantRepository.delete(e.getId()),
                    "Deleted"+e.getName()));

        //Delete

        } catch (RepositoryException e) {
            throw new RuntimeException(e);
        }


    }

}