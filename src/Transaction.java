// Transaction.java
import java.sql.Date;

public class Transaction {
    private int transactionId;
    private int patronId;
    private int bookId;
    private Date borrowDate;
    private Date returnDate;

    public Transaction(int transactionId, int patronId, int bookId, Date borrowDate, Date returnDate) {
        this.transactionId = transactionId;
        this.patronId = patronId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
        this.returnDate = returnDate;
    }

    public Transaction(int patronId, int bookId, Date borrowDate) {
        this.patronId = patronId;
        this.bookId = bookId;
        this.borrowDate = borrowDate;
    }

    // Getters and Setters
    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public Date getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(Date borrowDate) {
        this.borrowDate = borrowDate;
    }

    public Date getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(Date returnDate) {
        this.returnDate = returnDate;
    }
}
