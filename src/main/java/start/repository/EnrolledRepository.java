package start.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Enrolled;
import start.domain.Participant;
import start.domain.Trial;
import start.repository.interfaces.IEnrolledRepository;
import start.utils.ConnectionFactory;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class EnrolledRepository implements IEnrolledRepository {
    private static final Logger log = LogManager.getLogger();
    private final ConnectionFactory dbConnection;

    public EnrolledRepository( Properties prop) {
        this.dbConnection = new ConnectionFactory(prop);
    }

    @Override
    public void add(Enrolled item) throws RepositoryException {
        log.traceEntry("Params {}", item);
        String sqlAdd = "INSERT INTO enrollments (id_trial, id_participant) VALUES (?,?)";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, item.getTrial().getId());
            statement.setObject(2, item.getParticipant().getId());
            statement.execute();
            log.traceExit("Added success.");
        } catch (SQLException | IOException e) {
            System.out.println("EROR");
            throw log.throwing(new RepositoryException(e));
        }
    }

    @Override
    public void delete(Long itemID) throws RepositoryException {
        log.traceEntry("Params {}", itemID);
        String sqlAdd = "DELETE from enrollments where id_enroll=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, itemID);
            statement.execute();
            log.traceExit("Delete success.");
        } catch (SQLException | IOException e) {
            throw log.throwing(new RepositoryException(e));
        }
    }

    @Deprecated
    @Override
    public void update(Enrolled item) throws RepositoryException {
    }
    @Deprecated
    @Override
    public Optional<Enrolled> find(Long itemID) throws RepositoryException {
        return Optional.empty();
    }

    @Override
    public List<Trial> getTrialsFor(Participant participant) throws RepositoryException {
        List<Trial> list=new ArrayList<>();
        String sqlFindTrialsFor ="SELECT * from trials inner join enrollments e on trials.id_trial = e.id_trial where id_participant=?";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlFindTrialsFor)) {
            statement.setObject(1,participant.getId());

            var result=statement.executeQuery();
            while(result.next()) {
                var item = new Trial(
                        result.getString("name"),
                        result.getInt("min_age"),
                        result.getInt("max_age"));
                item.setId(result.getLong("id_trial"));
                list.add(item);
            }
            return list;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Participant> getEnrolledAt(Trial trial) throws RepositoryException {
        List<Participant> list=new ArrayList<>();
        String sqlFindTrialsFor ="SELECT * from participants inner join enrollments e on participants.id_participant = e.id_participant where id_trial=?";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlFindTrialsFor)) {
            statement.setObject(1,trial.getId());

            var result=statement.executeQuery();
            while(result.next()) {
                var item = new Participant(
                        result.getString("fullname"),
                        result.getString("cnp"),
                        result.getInt("age"));

                item.setId(result.getLong("id_participant"));
                list.add(item);
            }
            return list;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Enrolled> getAll() throws RepositoryException {
        List<Enrolled> list=new ArrayList<>();
        String sqlFindTrialsFor ="SELECT * from participants p, trials t,enrollments e where (e.id_participant=p.id_participant and e.id_trial = t.id_trial)";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlFindTrialsFor)) {
            var result=statement.executeQuery();
            while(result.next()) {
                var t=new Trial( result.getString("name"),result.getInt("min_age"),result.getInt("max_age"));
                var p=new Participant(result.getString("fullname"),result.getString("cnp"),result.getInt("age"));
                t.setId(result.getLong("id_trial"));
                p.setId(result.getLong("id_participant"));
                var item=new Enrolled(p,t);
                item.setId(result.getLong("id_enroll"));
                list.add(item);
            }
            return list;
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
