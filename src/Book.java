import java.sql.Date;

public class Book {
    private int bookId;
    private String title;
    private String author;
    private String isbn;
    private Date publishedDate;

    public Book(int bookId, String title, String author, String isbn, Date publishedDate) {
        this.bookId = bookId;
        this.title = title;
        this.author = author;
        this.isbn = isbn;
        this.publishedDate = publishedDate;
    }

    // Constructor for use when the book ID is not provided (e.g., for new books)
    public Book(String title, String author, String isbn, Date publishedDate) {
        this(0, title, author, isbn, publishedDate);  // bookId is set to 0 for new books
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public Date getPublishedDate() {
        return publishedDate;
    }

    public void setPublishedDate(Date publishedDate) {
        this.publishedDate = publishedDate;
    }
}


//import java.sql.Date;
//
//public class Book {
//    private int bookId;
//    private String title;
//    private String author;
//    private String isbn;
//    private String genre;
//    private int yearPublished;
//
//    public Book(int bookId, String title, String author, String isbn, String genre, int yearPublished) {
//        this.bookId = bookId;
//        this.title = title;
//        this.author = author;
//        this.isbn = isbn;
//        this.genre = genre;
//        this.yearPublished = yearPublished;
//    }
//
//    public Book(int bookId, String title, String author, String isbn, Date publishedDate) {
//        this.bookId = bookId;
//        this.title = title;
//        this.author = author;
//        this.isbn = isbn;
//        // Convert Date to year for yearPublished
//        this.yearPublished = publishedDate.toLocalDate().getYear();
//    }
//
//    // Getters and setters for all fields
//    public int getBookId() {
//        return bookId;
//    }
//
//    public void setBookId(int bookId) {
//        this.bookId = bookId;
//    }
//
//    public String getTitle() {
//        return title;
//    }
//
//    public void setTitle(String title) {
//        this.title = title;
//    }
//
//    public String getAuthor() {
//        return author;
//    }
//
//    public void setAuthor(String author) {
//        this.author = author;
//    }
//
//    public String getIsbn() {
//        return isbn;
//    }
//
//    public void setIsbn(String isbn) {
//        this.isbn = isbn;
//    }
//
//    public String getGenre() {
//        return genre;
//    }
//
//    public void setGenre(String genre) {
//        this.genre = genre;
//    }
//
//    public int getYearPublished() {
//        return yearPublished;
//    }
//
//    public void setYearPublished(int yearPublished) {
//        this.yearPublished = yearPublished;
//    }
//}
