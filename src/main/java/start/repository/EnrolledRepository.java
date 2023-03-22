package start.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Enrolled;
import start.domain.Participant;
import start.domain.Trial;
import start.repository.interfaces.IEnrolledRepository;
import start.utils.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class EnrolledRepository implements IEnrolledRepository {
    private static final Logger log = LogManager.getLogger();
    private final ConnectionFactory dbConnection;
    private final ParticipantRepository participantRepository;
    private final TrialRepository trialRepository;

    public EnrolledRepository(ParticipantRepository participantRepository, TrialRepository trialRepository, Properties prop) {
        this.participantRepository = participantRepository;
        this.trialRepository = trialRepository;
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
            throw log.throwing(new RepositoryException(e));
        }
    }

    @Override
    public void delete(Long itemID) throws RepositoryException {
        log.traceEntry("Params {}", itemID);
        String sqlAdd = "DELETE from enrollments where id=?";
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
        String sqlfindFor="SELECT * from enrollments where id_participant=?";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlfindFor)) {
            statement.setObject(1,participant.getId());
            var result=statement.executeQuery();

              list.add(trialRepository.find(result.getLong("id_trial")).get());

             return list;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Participant> getEnrolledAt(Trial idTrial) throws RepositoryException {
        return null;
    }

    @Override
    public Optional<Enrolled> findByTrialParticipant(Long idP, Long idT) throws RepositoryException {
        return Optional.empty();
    }

    @Override
    public List<Enrolled> getAll() throws RepositoryException {
        return null;
    }
}
