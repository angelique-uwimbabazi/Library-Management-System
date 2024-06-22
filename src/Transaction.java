import java.sql.Date;

public class Transaction {
    private int transactionId;
    private int bookId;
    private int patronId;
    private String transactionType;
    private Date transactionDate;

    public Transaction(int transactionId, int bookId, int patronId, String transactionType, Date transactionDate) {
        this.transactionId = transactionId;
        this.bookId = bookId;
        this.patronId = patronId;
        this.transactionType = String.valueOf(transactionType);
        this.transactionDate = transactionDate;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getPatronId() {
        return patronId;
    }

    public void setPatronId(int patronId) {
        this.patronId = patronId;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }
}
