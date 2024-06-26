
import java.sql.Connection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import  java.sql.SQLException;
public class TransactionDAO {
    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        String query = "INSERT INTO transactions (bookId, patronId, transactionDate, borrowDate, returnDate) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transaction.getBookId());
            stmt.setInt(2, transaction.getPatronId());
            stmt.setDate(3, transaction.getTransactionDate());
            stmt.setDate(4, transaction.getBorrowDate());
            stmt.setDate(5, transaction.getReturnDate());
            stmt.executeUpdate();
        }
    }

    public void updateTransaction(Transaction transaction) throws SQLException {
        String query = "UPDATE transactions SET bookId = ?, patronId = ?, transactionDate = ?, borrowDate = ?, returnDate = ? WHERE transactionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transaction.getBookId());
            stmt.setInt(2, transaction.getPatronId());
            stmt.setDate(3, transaction.getTransactionDate());
            stmt.setDate(4, transaction.getBorrowDate());
            stmt.setDate(5, transaction.getReturnDate());
            stmt.setInt(6, transaction.getTransactionId());
            stmt.executeUpdate();
        }
    }

    public void deleteTransaction(int transactionId) throws SQLException {
        String query = "DELETE FROM transactions WHERE transactionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transactionId"),
                        rs.getInt("bookId"),
                        rs.getInt("patronId"),
                        rs.getDate("transactionDate"),
                        rs.getDate("borrowDate"),
                        rs.getDate("returnDate")
                );
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
