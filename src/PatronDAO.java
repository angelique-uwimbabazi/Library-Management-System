// PatronDAO.java
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import  java.sql.SQLException;

public class PatronDAO {
    private Connection connection;

    public PatronDAO(Connection connection) {
        this.connection = connection;
    }

    public void addPatron(Patron patron) throws SQLException {
        String sql = "INSERT INTO patrons (name, email) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.executeUpdate();
        }
    }

    public void updatePatron(Patron patron) throws SQLException {
        String sql = "UPDATE patrons SET name = ?, email = ? WHERE patron_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, patron.getName());
            statement.setString(2, patron.getEmail());
            statement.setInt(3, patron.getPatronId());
            statement.executeUpdate();
        }
    }

    public void deletePatron(int patronId) throws SQLException {
        String sql = "DELETE FROM patrons WHERE patronId = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, patronId);
            statement.executeUpdate();
        }
    }

    public List<Patron> getAllPatrons() throws SQLException {
        String sql = "SELECT * FROM patrons";
        List<Patron> patrons = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int patronId = resultSet.getInt("patronId");
                String name = resultSet.getString("name");
                String email = resultSet.getString("email");
                patrons.add(new Patron(patronId, name, email));
            }
        }
        return patrons;
    }
}
