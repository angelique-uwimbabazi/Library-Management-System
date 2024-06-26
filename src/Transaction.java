
import java.sql.Date;
public class Transaction {
    private int transactionId;
    private int bookId;
    private int patronId;
    private Date transactionDate;
    private Date borrowDate;
    private Date returnDate;

    public Transaction(int bookId, int patronId, Date transactionDate, Date borrowDate, Date returnDate) {
        this.bookId = bookId;
        this.patronId = patronId;
        this.transactionDate = transactionDate;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Transaction(int transactionId, int bookId, int patronId, Date transactionDate, Date borrowDate, Date returnDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.patronId = patronId;
        this.transactionDate = transactionDate;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    // Getters
    public int getTransactionId() {
        return transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public int getPatronId() {
        return patronId;
    }

    public java.sql.Date getTransactionDate() {
        return transactionDate;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    // Setters
    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
