package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Trial;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class TrialRepositoryTest {
    @Test
    void testTrialRepo() throws RepositoryException {

        Properties prop=new Properties();
        try {
            prop.load(new FileReader("db.config"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        var repo= new TrialRepository(prop);

        var original=new Trial("Crafting",10,15);
        assertDoesNotThrow(()->repo.add(original),"Add passed");

        assertDoesNotThrow(()-> assertEquals(
                                original.getMaxAge(),
                                repo.findByName("Crafting").get().getMaxAge()),
                "Find passed");

        assertNotEquals(0,repo.getAll().size());

        var found= repo.findByName("Crafting").get();
        var updated=new Trial("Advanced Crafting",10,55);
        updated.setId(found.getId());
        repo.update(updated);

        assertEquals(
                        Optional.empty(),
                        repo.findByName("Crafting"));

        var foundAge=repo.getAllForAge(40);
        assertNotEquals(0,foundAge.size());
        assertDoesNotThrow(()->repo.delete(updated.getId()),"Delete pass");

    }

}