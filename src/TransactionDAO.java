// TransactionDAO.java
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAO {
    private Connection connection;

    public TransactionDAO(Connection connection) {
        this.connection = connection;
    }

    public void addTransaction(Transaction transaction) throws SQLException {
        String sql = "INSERT INTO transactions (patron_id, book_id, borrow_date) VALUES (?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, transaction.getPatronId());
            statement.setInt(2, transaction.getBookId());
            statement.setDate(3, transaction.getBorrowDate());
            statement.executeUpdate();
        }
    }

    public void returnBook(int transactionId, Date returnDate) throws SQLException {
        String sql = "UPDATE transactions SET return_date = ? WHERE transaction_id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDate(1, returnDate);
            statement.setInt(2, transactionId);
            statement.executeUpdate();
        }
    }

    public List<Transaction> getAllTransactions() throws SQLException {
        String sql = "SELECT * FROM transactions";
        List<Transaction> transactions = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                int transactionId = resultSet.getInt("transaction_id");
                int patronId = resultSet.getInt("patron_id");
                int bookId = resultSet.getInt("book_id");
                Date borrowDate = resultSet.getDate("borrow_date");
                Date returnDate = resultSet.getDate("return_date");
                transactions.add(new Transaction(transactionId, patronId, bookId, borrowDate, returnDate));
            }
        }
        return transactions;
    }
}
