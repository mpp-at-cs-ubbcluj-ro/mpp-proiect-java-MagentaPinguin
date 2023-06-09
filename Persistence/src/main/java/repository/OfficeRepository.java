package repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import model.*;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

public class OfficeRepository implements IOfficeRepository {

    private final ConnectionFactory dbConnection;
    private static final Logger logger = LogManager.getLogger();

    public OfficeRepository(Properties prop) {
        dbConnection = new ConnectionFactory(prop);
    }

    @Override
    public void add(Office item) throws RepositoryException {
        //Nu adaugam office
    }

    @Override
    public void update(Office item) throws RepositoryException {
        //Nu fac update la office
    }

    @Override
    public Optional<Office> find(Long itemID) throws RepositoryException {
        logger.traceEntry("Param {}", itemID);
        String sqlFind = "SELECT * from offices where id_office=?";

        Connection connection;
        try {
            connection = dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try (
                var statement = connection.prepareStatement(sqlFind)) {
            statement.setObject(1, itemID);
            var result = statement.executeQuery();
            if (!result.next()) {
                logger.traceExit("None was found by id");
                return Optional.empty();
            }
            var item = new Office(
                    result.getString("username"),
                    result.getString("password"));
            item.setId(result.getLong("id_office"));
            logger.traceExit("Find by id successful");
            return Optional.of(item);
        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public Optional<Office> findByUsername(String username) throws RepositoryException {
        logger.traceEntry("Param {}", username);
        String sqlFind = "SELECT * from offices where username=?";

        Connection connection;
        try {
            connection = dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try (
                var statement = connection.prepareStatement(sqlFind)) {
            statement.setObject(1, username);
            var result = statement.executeQuery();
            if (!result.next()) {
                logger.traceExit("None was found by username successful");
                return Optional.empty();
            }
            var item = new Office(
                    result.getString("username"),
                    result.getString("password"));
            item.setId(result.getLong("id_office"));
            logger.traceExit("Find by username successful");
            return Optional.of(item);

        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public List<Office> getAll() throws RepositoryException {
        logger.traceEntry("Param none");
        List<Office> list = new ArrayList<>();
        String sqlGetAll = "SELECT * from offices";

        Connection connection;
        try {
            connection = dbConnection.getConnection();
        } catch (IOException | SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }

        try (
                var statement = connection.prepareStatement(sqlGetAll)) {
            var result = statement.executeQuery();
            while (result.next()) {
                var item = new Office(
                        result.getString("username"),
                        result.getString("password"));
                item.setId(result.getLong("id_office"));
                list.add(item);
            }
            logger.traceExit("Get all successful");
            return list;

        } catch (SQLException e) {
            throw logger.throwing(new RepositoryException(e));
        }
    }

    @Override
    public void delete(Long itemID) throws RepositoryException {

    }
}
