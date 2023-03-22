package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Participant;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class ParticipantRepositoryTest {
    @Test
    void testParticipationRepo() throws RepositoryException {

        var prop= new Properties();
        try {
            prop.load(new FileReader("db.config"));
        } catch (IOException e) {
            throw new RepositoryException(e);
        }
        var participantRepo=new ParticipantRepository(prop);

        var participant=new Participant("Dummy","0987654323456",12);
        assertDoesNotThrow(()->participantRepo.add(participant),"Add successfully");

        assertDoesNotThrow(()->assertEquals(
                participant.getName(),
                        participantRepo.findByCnp("0987654323456").get().getName()),
                "Find successfully");

        var found=participantRepo.findByCnp("0987654323456").get();
        var updated=new Participant("UpdatedDummy","111111111111",21);
        updated.setId(found.getId());
        assertDoesNotThrow(()->participantRepo.update(updated),"Update pass");

        assertDoesNotThrow(()->assertEquals(
                        "UpdatedDummy",
                        participantRepo.find(found.getId()).get().getName()),
                "Find successfully");

        assertNotEquals(0,participantRepo.getAll().size());
        assertDoesNotThrow(()->participantRepo.delete(updated.getId()),"Delete successfully");
    }
}