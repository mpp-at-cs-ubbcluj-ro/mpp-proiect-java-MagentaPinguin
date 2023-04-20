package start.repository;

import start.domain.Trial;
import start.repository.interfaces.ITrialRepository;
import start.utils.ConnectionFactory;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrialRepository implements ITrialRepository {

    private static final  Logger logger = LogManager.getLogger();
    private final ConnectionFactory dbConnection;

    public TrialRepository(Properties properties) {
        this.dbConnection = new ConnectionFactory( properties);
    }

    @Override
    public void add(Trial item) throws RepositoryException {
        logger.traceEntry("Params: {}",item);

        String sqlAdd="Insert into trials (name, min_age, max_age) values (?,?,?)";

        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try(
            var statement=connection.prepareStatement(sqlAdd)){
            statement.setObject(1,item.getName());
            statement.setObject(2,item.getMinAge());
            statement.setObject(3,item.getMaxAge());
            statement.execute();
            logger.traceEntry("Added successful");
        } catch (SQLException  ex) {
            throw logger.throwing(new RepositoryException(ex));
        }

    }

    @Override
    public void delete(Long itemID) throws RepositoryException {
        logger.traceEntry("Params: {}",itemID);
        String sqlAdd="DELETE from trials where id_trial=?";

        Connection connection;
        try {
            connection=dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try(
            var statement=connection.prepareStatement(sqlAdd)){
            statement.setObject(1,itemID);
            statement.execute();
            logger.traceEntry("Delete successful");
        } catch (SQLException ex) {
            throw logger.throwing(new RepositoryException(ex));
        }
    }

    @Override
    public void update(Trial item) throws RepositoryException {
        logger.traceEntry("Params: {}",item);
        String sqlAdd="Update trials set name=?,min_age=?,max_age=? where id_trial=?";
        try(var connection = dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlAdd)){

            statement.setObject(1,item.getName());
            statement.setObject(2,item.getMinAge());
            statement.setObject(3,item.getMaxAge());
            statement.setObject(4,item.getId());
            statement.execute();
            logger.traceEntry("Update successful");
        } catch (SQLException | IOException ex) {
            throw logger.throwing(new RepositoryException(ex));
        }
    }
    @Override
    public Optional<Trial> find(Long itemID) throws RepositoryException {
        logger.traceEntry("Params{}",itemID);
        String sqlUpdate="SELECT  * from trials where id_trial=? ";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlUpdate)){
            statement.setObject(1,itemID);
            var result=statement.executeQuery();
            if (!result.next()){
                logger.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Trial(
                    result.getString("name"),
                    result.getInt("min_age"),
                    result.getInt("max_age"));
            item.setId(result.getLong("id_trial"));
            logger.traceExit("Find successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Trial> getAll() throws RepositoryException {
        logger.traceEntry("Params{}");
        List<Trial> list=new ArrayList<>();
        String sqlGetAll="SELECT  * from trials";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlGetAll)){
            var result=statement.executeQuery();
            while(result.next()) {
                var item = new Trial(
                        result.getString("name"),
                        result.getInt("min_age"),
                        result.getInt("max_age"));
                item.setId(result.getLong("id_trial"));
                list.add(item);
            }
            logger.traceExit("Find successful");
            return list;
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public Optional<Trial> getSpecificTrial(String name, int minAge, int maxAge) throws RepositoryException {
        logger.traceEntry("Params{}",name,minAge,maxAge);
        String sqlUpdate="SELECT  * from trials where name=? and min_age=? and max_age=?";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlUpdate)){
            statement.setObject(1,name);
            statement.setObject(2,minAge);
            statement.setObject(3,maxAge);
            var result=statement.executeQuery();
            if (!result.next()){
                logger.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Trial(
                    result.getString("name"),
                    result.getInt("min_age"),
                    result.getInt("max_age"));
            item.setId(result.getLong("id_trial"));
            logger.traceExit("Find successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Trial> getTrialsForAge(int age) throws RepositoryException {
        logger.traceEntry("Params{}");
        List<Trial> list=new ArrayList<>();
        String sqlGetAll="SELECT  * from trials where min_age<= ?  and ? <=max_age";
        try(var connection=dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlGetAll)){
            statement.setObject(1,age);
            statement.setObject(2,age);
            var result=statement.executeQuery();
            while(result.next()) {
                var item = new Trial(
                        result.getString("name"),
                        result.getInt("min_age"),
                        result.getInt("max_age"));
                item.setId(result.getLong("id_trial"));
                list.add(item);
            }
            logger.traceExit("Find successful");
            return list;
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

}

