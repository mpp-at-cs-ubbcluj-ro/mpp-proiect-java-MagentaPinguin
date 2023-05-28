package services;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import model.Trial;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import repository.RepositoryException;
import repository.TrialRepositoryMock;

import java.util.List;



@RestController
@RequestMapping("/trials")
@CrossOrigin(origins = "http://localhost:3000")
public class TrialController {

    @Autowired
    private TrialRepositoryMock repository;

    @RequestMapping( method= RequestMethod.GET)
    public ResponseEntity<List<Trial>> getAll() throws RepositoryException {
        System.out.println("Get all users ...");
        return ResponseEntity.ok( repository.getAll());
    }
    @RequestMapping(value = "/{idTrial}", method = RequestMethod.GET)
    public ResponseEntity<Trial> getByID(@PathVariable int idTrial) throws RepositoryException {
        System.out.println("Get by id ...");
        var found=repository.find((long)idTrial);
        if(found.isPresent())
            return ResponseEntity.ok(found.get());
        return ResponseEntity.badRequest().build();

    }
    @RequestMapping(value = "/specific", method = RequestMethod.POST)
    public  ResponseEntity<Trial> getSpecific(@RequestBody Trial item) throws RepositoryException {
        System.out.println("Get specific trial ...");
        var found=repository.getSpecificTrial(item.getName(),item.getMinAge(),item.getMaxAge());

        return found.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.badRequest().build());

    }
    @RequestMapping(method= RequestMethod.POST)
    public ResponseEntity<Trial> addTrial(@RequestBody Trial item) throws RepositoryException {
        System.out.println(item);
        var added=repository.add(item);
        System.out.println(added);
        return ResponseEntity.ok(added);

    }

    @RequestMapping(value = "/{idTrial}", method = RequestMethod.PUT)
    public ResponseEntity<?> update(@PathVariable int idTrial, @RequestBody Trial item){
        if(idTrial != item.getId()) {
            return ResponseEntity.badRequest().build();
        }
        try {
            repository.update(item);

        } catch (RepositoryException e) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok(idTrial);

    }

    @RequestMapping(value="/{idTrial}", method= RequestMethod.DELETE)
    public ResponseEntity<?> delete(@PathVariable int idTrial){
        System.out.println("Deleting trial with the id... "+idTrial);
        try {
            repository.delete((long)idTrial);
            return ResponseEntity.ok("Sters");
        }catch (RepositoryException ex){
            System.out.println("Delete user exception");
            return ResponseEntity.badRequest().body("ERROR");
        }
    }

}
