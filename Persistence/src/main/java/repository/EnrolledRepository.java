package repository;

import model.Enrolled;
import model.Participant;
import model.Trial;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class EnrolledRepository implements IEnrolledRepository {
    private static final Logger logger = LogManager.getLogger();
    private final ConnectionFactory dbConnection;

    public EnrolledRepository( Properties prop) {
        this.dbConnection = new ConnectionFactory(prop);
    }

    @Override
    public void add(Enrolled item) throws RepositoryException {
        logger.traceEntry("Params {}", item);

        String sqlAdd = "INSERT INTO enrollments (id_trial, id_participant) VALUES (?,?)";
        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }
        try (var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, item.getTrial().getId());
            statement.setObject(2, item.getParticipant().getId());
            statement.execute();
            logger.traceExit("Added success.");
        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

    }

    @Override
    public void delete(Long itemID) throws RepositoryException {
        logger.traceEntry("Params {}", itemID);
        String sqlAdd = "DELETE from enrollments where id_enroll=?";
        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try (var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, itemID);
            statement.execute();
            logger.traceExit("Delete success.");
        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
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
        logger.traceEntry("Params {}", participant);
        List<Trial> list=new ArrayList<>();
        String sqlFindTrialsFor ="SELECT * from trials t inner join enrollments e on t.id_trial = e.id_trial where id_participant=?";
        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try(
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
            logger.traceExit("GetTrialsFor exit.");
            return list;
        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Participant> getEnrolledAt(Trial trial) throws RepositoryException {
        logger.traceEntry("Params {}", trial);
        List<Participant> list=new ArrayList<>();
        String sqlFindTrialsFor ="SELECT * from participants inner join enrollments e on participants.id_participant = e.id_participant where id_trial=?";
        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));

        }

        try(
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
            logger.traceExit("GetEntrolledAt exit.");
            return list;
        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Enrolled> getAll() throws RepositoryException {
        logger.traceEntry("Params {}");
        List<Enrolled> list=new ArrayList<>();
        String sqlFindTrialsFor ="SELECT * from participants p, trials t,enrollments e where (e.id_participant=p.id_participant and e.id_trial = t.id_trial)";

        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));

        }

        try(
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
            logger.traceExit("GetAll exit.");
            return list;
        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));

        }
    }
}
