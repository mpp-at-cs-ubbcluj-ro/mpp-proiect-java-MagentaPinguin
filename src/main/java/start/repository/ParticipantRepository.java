package start.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import start.domain.Participant;
import start.repository.interfaces.IParticipantRepository;
import start.utils.ConnectionFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class ParticipantRepository implements IParticipantRepository {
    private final ConnectionFactory dbConnection;
    private static final Logger logger= LogManager.getLogger();

    public ParticipantRepository(Properties dbProp) {
        this.dbConnection = new ConnectionFactory(dbProp);
    }

    @Override
    public void add(Participant item) throws RepositoryException {
        logger.traceEntry("Params{}",item);
        String sqlAdd="INSERT into participants (fullname, cnp, age) VALUES (?,?,?)";
        try(var connection=dbConnection.getConnection();
        var statement=connection.prepareStatement(sqlAdd)){
            statement.setObject(1,item.getName());
            statement.setObject(2,item.getCnp());
            statement.setObject(3,item.getAge());
            statement.execute();
            logger.traceExit("Added successful");
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public void delete(Long itemID) throws RepositoryException {
        logger.traceEntry("Params{}",itemID);
        String sqlDelete="Delete from participants where id=?";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlDelete)){
            statement.setObject(1,itemID);
            statement.execute();
            logger.traceExit("Deleted successful");
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public void update(Participant item) throws RepositoryException {
        logger.traceEntry("Params{}",item);
        String sqlUpdate="UPDATE participants set fullname=?, cnp=?, age=? where id=?";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlUpdate)){
            statement.setObject(1,item.getName());
            statement.setObject(2,item.getCnp());
            statement.setObject(3,item.getAge());
            statement.setObject(4,item.getId());
            statement.executeUpdate();
            logger.traceExit("Updated successful");
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public Optional<Participant> find(Long itemID) throws RepositoryException {
        logger.traceEntry("Params{}",itemID);
        String sqlFind="SELECT  * from participants where id=?";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlFind)){
            statement.setObject(1,itemID);
            var result=statement.executeQuery();
            if (!result.next()){
                logger.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Participant(
                    result.getString("fullname"),
                    result.getString("cnp"),
                    result.getInt("age"));

            item.setId(result.getLong("id"));
            logger.traceExit("Find successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Participant> getAll() throws RepositoryException {
        logger.traceEntry("Params{}");
        List<Participant> list=new ArrayList<>();
        String sqlGetAll="SELECT  * from participants";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlGetAll)){
            var result=statement.executeQuery();
            while(result.next()) {
                var item = new Participant(
                        result.getString("fullname"),
                        result.getString("cnp"),
                        result.getInt("age"));

                item.setId(result.getLong("id"));
                list.add(item);
            }
            logger.traceExit("Find successful");
            return list;
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public Optional<Participant> findByCnp(String cnp) throws RepositoryException {
        logger.traceEntry("Params{}",cnp);
        String sqlFind="SELECT  * from participants where cnp=?";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlFind)){
            statement.setObject(1,cnp);
            var result=statement.executeQuery();
            if (!result.next()){
                logger.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Participant(
                    result.getString("fullname"),
                    result.getString("cnp"),
                    result.getInt("age"));

            item.setId(result.getLong("id"));
            logger.traceExit("Find successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

}
