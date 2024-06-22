import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    // Add a new transaction
    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (bookId, patronId, transactionType, transactionDate) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, transaction.getBookId());
            stmt.setInt(2, transaction.getPatronId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setDate(4, transaction.getTransactionDate());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if (generatedKeys.next()) {
                transaction.setTransactionId(generatedKeys.getInt(1));
            }
        }
    }

    // Update an existing transaction
    public void updateTransaction(Transaction transaction) throws SQLException {
        String sql = "UPDATE transactions SET bookId = ?, patronId = ?, transactionType = ?, transactionDate = ? WHERE transactionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transaction.getBookId());
            stmt.setInt(2, transaction.getPatronId());
            stmt.setString(3, transaction.getTransactionType());
            stmt.setDate(4, transaction.getTransactionDate());
            stmt.setInt(5, transaction.getTransactionId());
            stmt.executeUpdate();
        }
    }

    // Delete a transaction by ID
    public void deleteTransaction(int transactionId) throws SQLException {
        String sql = "DELETE FROM transactions WHERE transactionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            stmt.executeUpdate();
        }
    }

    // Get a transaction by ID
    public Transaction getTransactionById(int transactionId) throws SQLException {
        String sql = "SELECT * FROM transactions WHERE transactionId = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transactionId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Transaction(
                        rs.getInt("transactionId"),
                        rs.getInt("bookId"),
                        rs.getInt("patronId"),
                        rs.getString("transactionType"),
                        rs.getDate("transactionDate")
                );
            }
        }
        return null;
    }

    // Get all transactions
    public List<Transaction> getAllTransactions() throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String sql = "SELECT * FROM transactions";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Transaction transaction = new Transaction(
                        rs.getInt("transactionId"),
                        rs.getInt("bookId"),
                        rs.getInt("patronId"),
                        rs.getString("transactionType"),
                        rs.getDate("transactionDate")
                );
                transactions.add(transaction);
            }
        }
        return transactions;
    }
}
