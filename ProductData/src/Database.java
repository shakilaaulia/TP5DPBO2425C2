import javax.swing.JOptionPane;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection connection;
    private Statement statement;


    public Database() {
        try {
            connection = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/db_products",
                    "root",
                    "");
            statement = connection.createStatement();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, "Connection to database failed: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
            System.exit(0);
        }
    }

    public ResultSet selectQuery(String sql) {
        try {
            statement.executeQuery(sql);
            return statement.getResultSet();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public int insertUpdateDeleteQuery(String sql) {
        try {
            return statement.executeUpdate(sql);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Statement getStatement() {
        return statement;
    }
}