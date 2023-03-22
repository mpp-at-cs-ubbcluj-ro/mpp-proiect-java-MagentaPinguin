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
    void testOfficeRepo() throws RepositoryException {
        var dbProp=new Properties();
        //Repo initialization
        try {
            dbProp.load(new FileReader("db.config"));
        } catch (IOException e) {
            fail("File not found");
        }
        var officeRepo=new OfficeRepository(dbProp);
        var officeDummy=new Office("Dummy","DummyPassw");
        var officeDummy2=new Office("Dummy2","DummyPassw");
        //Add new office
        assertDoesNotThrow(()->officeRepo.add(officeDummy),"Add pass");
        assertDoesNotThrow(()->officeRepo.add(officeDummy2),"Add pass");
        //Find by username test
        assertDoesNotThrow(
                ()-> assertNotEquals(
                        Optional.empty(),
                        officeRepo.findByUsername("Dummy")),
                "Find by username pass");
        var found=officeRepo.findByUsername("Dummy").get();

        //Find all test
        assertNotEquals(0,officeRepo.getAll().size());

        //Find by id test
        assertDoesNotThrow(
                ()-> assertEquals(
                        found.getUsername(),
                        officeRepo.find(found.getId()).get().getUsername()),
                "Find by id pass");

        //Update the element
        var updatedDummy=new Office("Dummy2.0","StrongDummy");
        updatedDummy.setId(found.getId());

        //Find if the element was updated
        assertDoesNotThrow( ()->officeRepo.update(updatedDummy),"Update1 pass");

        //Check if the old element was updated
        assertDoesNotThrow(
                ()-> assertEquals(
                        Optional.empty(),
                        officeRepo.findByUsername("Dummy")),
                "Update1/2 pass");
        assertDoesNotThrow(
                ()-> assertEquals(
                        updatedDummy.getPassword(),
                        officeRepo.findByUsername("Dummy2.0").get().getPassword()),
                "Update2/2 pass");

        //Delete the element.
        assertDoesNotThrow( ()->officeRepo.delete(updatedDummy.getId()),"Deleted pass");
        assertDoesNotThrow( ()->officeRepo.delete(officeRepo.findByUsername("Dummy2").get().getId()),"Deleted pass");

    }

}