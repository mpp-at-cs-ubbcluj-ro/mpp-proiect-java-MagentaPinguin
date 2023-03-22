package start.repository;

import start.domain.Trial;
import start.repository.interfaces.ITrialRepository;
import start.utils.ConnectionFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TrialRepository implements ITrialRepository {

    private static final  Logger log= LogManager.getLogger();
    private final ConnectionFactory dbConnection;

    public TrialRepository(Properties properties) {
        this.dbConnection = new ConnectionFactory( properties);
    }

    @Override
    public void add(Trial item) throws RepositoryException {
        log.traceEntry("Params: {}",item);
        String sqlAdd="Insert into trials (name, min_age, max_age) values (?,?,?)";
        try(var connection = dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlAdd)){
            statement.setObject(1,item.getName());
            statement.setObject(2,item.getMinAge());
            statement.setObject(3,item.getMaxAge());
            statement.execute();
            log.traceEntry("Added successful");
        } catch (SQLException | IOException ex) {
            throw log.throwing(new RepositoryException(ex));
        }

    }

    @Override
    public void delete(Long itemID) throws RepositoryException {
        log.traceEntry("Params: {}",itemID);
        String sqlAdd="DELETE from trials where id=?";
        try(var connection = dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlAdd)){
            statement.setObject(1,itemID);
            statement.execute();
            log.traceEntry("Delete successful");
        } catch (SQLException | IOException ex) {
            throw log.throwing(new RepositoryException(ex));
        }
    }

    @Override
    public void update(Trial item) throws RepositoryException {
        log.traceEntry("Params: {}",item);
        String sqlAdd="Update trials set name=?,min_age=?,max_age=? where id=?";
        try(var connection = dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlAdd)){

            statement.setObject(1,item.getName());
            statement.setObject(2,item.getMinAge());
            statement.setObject(3,item.getMaxAge());
            statement.setObject(4,item.getId());
            statement.execute();
            log.traceEntry("Update successful");
        } catch (SQLException | IOException ex) {
            throw log.throwing(new RepositoryException(ex));
        }
    }
    @Override
    public Optional<Trial> find(Long itemID) throws RepositoryException {
        log.traceEntry("Params{}",itemID);
        String sqlFind="SELECT  * from trials where id=?";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlFind)){
            statement.setObject(1,itemID);
            var result=statement.executeQuery();
            if (!result.next()){
                log.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Trial(
                    result.getString("name"),
                    result.getInt("min_age"),
                    result.getInt("max_age"));
            item.setId(result.getLong("id"));
            log.traceExit("Find successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw log.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Trial> getAll() throws RepositoryException {
        log.traceEntry("Params{}");
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
                item.setId(result.getLong("id"));
                list.add(item);
            }
            log.traceExit("Find successful");
            return list;
        } catch (SQLException | IOException e) {
            throw log.throwing(new RepositoryException(e));
        }
    }

    @Override
    public Optional<Trial> findByName(String name) throws RepositoryException {
        log.traceEntry("Params{}",name);
        String sqlUpdate="SELECT  * from trials where name=?";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlUpdate)){
            statement.setObject(1,name);
            var result=statement.executeQuery();
            if (!result.next()){
                log.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Trial(
                    result.getString("name"),
                    result.getInt("min_age"),
                    result.getInt("max_age"));
            item.setId(result.getLong("id"));
            log.traceExit("Find successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw log.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Trial> getAllForAge(int age) throws RepositoryException {
        log.traceEntry("Params{}");
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
                item.setId(result.getLong("id"));
                list.add(item);
            }
            log.traceExit("Find successful");
            return list;
        } catch (SQLException | IOException e) {
            throw log.throwing(new RepositoryException(e));
        }
    }

}

