package start.repository;

import start.domain.Office;
import start.repository.interfaces.IOfficeRepository;
import start.utils.ConnectionFactory;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class OfficeRepository implements IOfficeRepository {

    private final ConnectionFactory dbConnection;
    private static final Logger logger = LogManager.getLogger();

    public OfficeRepository(Properties prop) {
        dbConnection = new ConnectionFactory(prop);
    }

    @Override
    public void add(Office item) throws RepositoryException {
        logger.traceEntry("Param {}", item);
        String sqlAdd = "Insert into offices (username, password) VALUES (?,?)";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, item.getUsername());
            statement.setObject(2, item.getPassword());
            statement.execute();
            logger.traceExit("Added successful!");
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public void update(Office item) throws RepositoryException {
        logger.traceEntry("Param {}", item);
        String sqlUpdate = "Update offices  set username=?, password=? where id_office=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlUpdate)) {
            statement.setObject(1, item.getUsername());
            statement.setObject(2, item.getPassword());
            statement.setObject(3, item.getId());
            statement.executeUpdate();
            logger.traceExit("Update successful");
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public Optional<Office> find(Long itemID) {
        logger.traceEntry("Param {}", itemID);
        String sqlFind = "SELECT * from offices where id_office=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlFind)) {
            statement.setObject(1, itemID);
            var result = statement.executeQuery();
            if (!result.next()){
                logger.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Office(
                    result.getString("username"),
                    result.getString("password"));
            item.setId(result.getLong("id_office"));
            logger.traceExit("Find by id successful");
            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Optional<Office> findByUsername(String username) throws RepositoryException {
        logger.traceEntry("Param {}", username);
        String sqlFind = "SELECT * from offices where username=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlFind)) {
            statement.setObject(1, username);
            var result = statement.executeQuery();
            if (!result.next()){
                logger.traceExit("None was found by username successful");
                return Optional.empty();}
            var item = new Office(
                    result.getString("username"),
                    result.getString("password"));
            item.setId(result.getLong("id_office"));
            logger.traceExit("Find by username successful");
            return Optional.of(item);

        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Office> getAll() throws RepositoryException {
        logger.traceEntry("Param none");
        List<Office> list=new ArrayList<>();
        String sqlGetAll = "SELECT * from offices";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlGetAll)) {
            var result = statement.executeQuery();
            while(result.next()) {
                var item = new Office(
                        result.getString("username"),
                        result.getString("password"));
                item.setId(result.getLong("id_office"));
                list.add(item);
            }
            logger.traceExit("Get all successful");
            return list;

        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }
    @Override
    public void delete(Long itemID) throws RepositoryException {
        logger.traceEntry("Param {}", itemID);
        String sqlDelete = "DELETE from offices where id_office=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlDelete)) {
            statement.setObject(1, itemID);
            var afect=statement.executeUpdate();
            if (afect==0){
                logger.traceExit("Nothing deleted");
                return;
            }
            logger.traceExit("Deleted successful");
        } catch (SQLException | IOException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }


}
