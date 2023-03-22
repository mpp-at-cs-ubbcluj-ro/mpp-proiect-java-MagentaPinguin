package start.utils;

import java.io.IOException;
import java.util.Properties;
import java.sql.*;
public class ConnectionFactory {
    private final Properties jdbcProps;
    private static Connection instance=null;

    public ConnectionFactory(Properties jdbcProps) {
        this.jdbcProps = jdbcProps;
    }

    private void Connection() throws  SQLException {
        var url=jdbcProps.get("dbUrl");
        var user=jdbcProps.get("dbUser");
        var passwd=jdbcProps.get("dbPass");
        if(user==null && passwd==null){
            instance=DriverManager.getConnection(url.toString());
            return;
        }
        instance= DriverManager.getConnection(url.toString(),user.toString(),passwd.toString());
    }
    public  Connection getConnection() throws IOException, SQLException {

        if (instance==null || instance.isClosed())
            Connection();
        return instance;
    }
}
