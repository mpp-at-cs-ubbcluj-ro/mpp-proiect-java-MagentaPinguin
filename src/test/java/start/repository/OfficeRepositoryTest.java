package start.repository;

import org.junit.jupiter.api.Test;
import start.domain.Office;

import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.util.Properties;

import static org.junit.jupiter.api.Assertions.*;

class OfficeRepositoryTest {

    @Test
    void testOfficeRepo() {
        try {
            var dbProp=new Properties();
            dbProp.load(new FileReader("db.config"));
            var officeRepo=new OfficeRepository(dbProp);
            //Init
            var t1=new Office("T1","1");
            var t2=new Office("T2","2");
            var t3=new Office("T3","3");
            assertDoesNotThrow(()-> officeRepo.add(t1),"T1 added");
            assertDoesNotThrow(()-> officeRepo.add(t2),"T2 added");
            assertDoesNotThrow(()-> officeRepo.add(t3),"T3 added");
            assertThrows(RepositoryException.class,()-> officeRepo.add(t3),"A duplicated key -> not added");
            //Add

            var found=officeRepo.findByUsername("T1");
            assertTrue(found.isPresent(),"Find by id pass");
            var foundID=officeRepo.find(found.get().getId());
            assertEquals(foundID,found,"Find by it pass");
            //Findings
            assertEquals(3,officeRepo.getAll().size(),"Get all passed");
            //Get all
            var updated=new Office("T11","T11");
            updated.setId(foundID.get().getId());
            officeRepo.update(updated);
            assertEquals("T11",officeRepo.find(foundID.get().getId()).get().getUsername());
            //Update

            officeRepo.delete(officeRepo.findByUsername("T11").get().getId());
            officeRepo.delete(officeRepo.findByUsername("T2").get().getId());
            officeRepo.delete(officeRepo.findByUsername("T3").get().getId());
            //Delete
            assertEquals(0,officeRepo.getAll().size(),"Delete complete");
            //Get all

        } catch (IOException | RepositoryException e) {
            assert false;
        }
    }
}