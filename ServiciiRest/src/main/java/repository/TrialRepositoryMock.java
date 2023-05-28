package repository;

import model.Trial;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

@Component
public class TrialRepositoryMock{


    private final ConnectionFactory dbConnection;

    public TrialRepositoryMock() {
        Properties properties= new Properties();
        try {
            properties.load(TrialRepository.class.getResourceAsStream("serverProperties.properties"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        System.out.println(properties);
        this.dbConnection = new ConnectionFactory( properties);
    }


    public Optional<Trial> getSpecificTrial(String name, int minAge, int maxAge) throws RepositoryException {
        String sqlUpdate="SELECT  * from trials where name=? and  min_age=? and max_age=? ";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlUpdate)){
            statement.setObject(1,name);
            statement.setObject(2,minAge);
            statement.setObject(3,maxAge);
            var result=statement.executeQuery();
            if (!result.next()){

                return Optional.empty();
            }
            var item = new Trial(
                    result.getString("name"),
                    result.getInt("min_age"),
                    result.getInt("max_age"));
            item.setId(result.getLong("id_trial"));

            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw new RepositoryException(e);
        }
    }


    public List<Trial> findByName(String name) throws RepositoryException {
        return null;
    }


    public List<Trial> getTrialsForAge(int age) throws RepositoryException {
        return null;
    }


    public Trial add(Trial item) throws RepositoryException {
        String sqlAdd = "INSERT into trials (name, min_age, max_age) VALUES (?,?,?)";
        try (var connection = dbConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sqlAdd, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, item.getName());
            statement.setObject(2, item.getMinAge());
            statement.setObject(3, item.getMaxAge());
            int result=statement.executeUpdate();

            if (result>0){
                ResultSet rs = statement.getGeneratedKeys();
                if (rs.next()) {
                    int id=rs.getInt(1);
                    item.setId((long)id);
                    return item;
                }
            }

        } catch (SQLException | IOException e) {
            throw new RepositoryException(e);
        }
    return null;
    }


    public void delete(Long itemID) throws RepositoryException {
        String sqlAdd = "DELETE from trials where id_trial=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, itemID);
            statement.execute();

        } catch (SQLException | IOException e) {
            throw new RepositoryException(e);
        }

    }


    public void update(Trial item) throws RepositoryException {
        String sqlAdd = "UPDATE trials SET  min_age=?, max_age=?, name=? WHERE  id_trial=?";
        try (var connection = dbConnection.getConnection();
             var statement = connection.prepareStatement(sqlAdd)) {
            statement.setObject(1, item.getMinAge());
            statement.setObject(2, item.getMaxAge());
            statement.setObject(3, item.getName());
            statement.setObject(4, item.getId());
            statement.execute();

        } catch (SQLException | IOException e) {
            throw new RepositoryException(e);
        }
    }


    public Optional<Trial> find(Long itemID) throws RepositoryException {
        String sqlUpdate="SELECT  * from trials where id_trial=? ";
        try(var connection= dbConnection.getConnection();
            var statement=connection.prepareStatement(sqlUpdate)){
            statement.setObject(1,itemID);
            var result=statement.executeQuery();
            if (!result.next()){

                return Optional.empty();
            }
            var item = new Trial(
                    result.getString("name"),
                    result.getInt("min_age"),
                    result.getInt("max_age"));
            item.setId(result.getLong("id_trial"));

            return Optional.of(item);
        } catch (SQLException | IOException e) {
            throw new RepositoryException(e);
        }
    }


    public List<Trial> getAll() throws RepositoryException {

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
            return list;
        } catch (SQLException | IOException e) {
        }
        return list;
    }
}
