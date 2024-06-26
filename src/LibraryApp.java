import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LibraryApp extends Application {

    private TableView<Book> tableView;
    private TextField idInput, titleInput, authorInput, isbnInput, publishedDateInput;
    private TableView<Patron> patronTableView;
    private TextField patronIdInput, patronNameInput, patronEmailInput;
    private TableView<Transaction> transactionTableView;
    private TextField transactionIdInput, transactionBookIdInput, transactionPatronIdInput, transactionDateInput, borrowDateInput, returnDateInput;
    private Connection connection;
    private BookDAO bookDAO;
    private PatronDAO patronDAO;
    private TransactionDAO transactionDAO;
    private TransactionStack transactionStack;
    private TransactionQueue transactionQueue;

    @Override
    public void start(Stage primaryStage) {
        establishConnection();
        bookDAO = new BookDAO(connection);
        patronDAO = new PatronDAO(connection);
        transactionDAO = new TransactionDAO(connection);
        transactionStack = new TransactionStack();
        transactionQueue = new TransactionQueue();
        // Create TableView for Books
        tableView = new TableView<>();
        TableColumn<Book, Integer> idColumn = new TableColumn<>("ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, Date> publishedDateColumn = new TableColumn<>("Published Date");
        publishedDateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, publishedDateColumn);

        // Set up table row click event for Books
        tableView.setRowFactory(tv -> {
            TableRow<Book> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Book selectedBook = row.getItem();
                    populateInputs(selectedBook);
                }
            });
            return row;
        });

        // Create Input Fields for Books
        idInput = new TextField();
        idInput.setPromptText("ID");

        titleInput = new TextField();
        titleInput.setPromptText("Title");

        authorInput = new TextField();
        authorInput.setPromptText("Author");

        isbnInput = new TextField();
        isbnInput.setPromptText("ISBN");

        publishedDateInput = new TextField();
        publishedDateInput.setPromptText("Published Date (YYYY-MM-DD)");

        // Create Buttons for Books
        Button addButton = new Button("Add Book");
        addButton.setOnAction(event -> addBook());

        Button updateButton = new Button("Update Book");
        updateButton.setOnAction(event -> updateBook());

        Button deleteButton = new Button("Delete Book");
        deleteButton.setOnAction(event -> deleteBook());

        Button clearFieldsButton = new Button("Clear Fields");
        clearFieldsButton.setOnAction(event -> clearInputs());

        // Layout for Books section
        HBox buttons = new HBox();
        buttons.getChildren().addAll(addButton, updateButton, deleteButton, clearFieldsButton);
        buttons.setSpacing(10);

        VBox bookBox = new VBox();
        bookBox.getChildren().addAll(new Label("Books"), tableView, new Label("Book Details"), idInput, titleInput, authorInput, isbnInput, publishedDateInput, buttons);
        bookBox.setSpacing(10);
        bookBox.setPadding(new Insets(10));

        // Create TableView for Patrons
        patronTableView = new TableView<>();
        TableColumn<Patron, Integer> patronIdColumn = new TableColumn<>("ID");
        patronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));

        TableColumn<Patron, String> patronNameColumn = new TableColumn<>("Name");
        patronNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<Patron, String> patronEmailColumn = new TableColumn<>("Email");
        patronEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));

        patronTableView.getColumns().addAll(patronIdColumn, patronNameColumn, patronEmailColumn);

        // Set up table row click event for Patrons
        patronTableView.setRowFactory(tv -> {
            TableRow<Patron> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Patron selectedPatron = row.getItem();
                    populatePatronInputs(selectedPatron);
                }
            });
            return row;
        });

        // Create Input Fields for Patrons
        patronIdInput = new TextField();
        patronIdInput.setPromptText("ID");

        patronNameInput = new TextField();
        patronNameInput.setPromptText("Name");

        patronEmailInput = new TextField();
        patronEmailInput.setPromptText("Email");

        // Create Buttons for Patrons
        Button addPatronButton = new Button("Add Patron");
        addPatronButton.setOnAction(event -> addPatron());

        Button updatePatronButton = new Button("Update Patron");
        updatePatronButton.setOnAction(event -> updatePatron());

        Button deletePatronButton = new Button("Delete Patron");
        deletePatronButton.setOnAction(event -> deletePatron());

        Button clearPatronFieldsButton = new Button("Clear Fields");
        clearPatronFieldsButton.setOnAction(event -> clearPatronInputs());

        // Layout for Patrons section
        HBox patronButtons = new HBox();
        patronButtons.getChildren().addAll(addPatronButton, updatePatronButton, deletePatronButton, clearPatronFieldsButton);
        patronButtons.setSpacing(10);

        VBox patronBox = new VBox();
        patronBox.getChildren().addAll(new Label("Patrons"), patronTableView, new Label("Patron Details"), patronIdInput, patronNameInput, patronEmailInput, patronButtons);
        patronBox.setSpacing(10);
        patronBox.setPadding(new Insets(10));

        // Create TableView for Transactions
        transactionTableView = new TableView<>();
        TableColumn<Transaction, Integer> transactionIdColumn = new TableColumn<>("ID");
        transactionIdColumn.setCellValueFactory(new PropertyValueFactory<>("transactionId"));

        TableColumn<Transaction, Integer> transactionBookIdColumn = new TableColumn<>("Book ID");
        transactionBookIdColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Transaction, Integer> transactionPatronIdColumn = new TableColumn<>("Patron ID");
        transactionPatronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));

        TableColumn<Transaction, Date> transactionDateColumn = new TableColumn<>("Transaction Date");
        transactionDateColumn.setCellValueFactory(new PropertyValueFactory<>("transactionDate"));

        TableColumn<Transaction, Date> borrowDateColumn = new TableColumn<>("Borrow Date");
        borrowDateColumn.setCellValueFactory(new PropertyValueFactory<>("borrowDate"));

        TableColumn<Transaction, Date> returnDateColumn = new TableColumn<>("Return Date");
        returnDateColumn.setCellValueFactory(new PropertyValueFactory<>("returnDate"));

        transactionTableView.getColumns().addAll(transactionIdColumn, transactionBookIdColumn, transactionPatronIdColumn, transactionDateColumn, borrowDateColumn, returnDateColumn);

        // Set up table row click event for Transactions
        transactionTableView.setRowFactory(tv -> {
            TableRow<Transaction> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty()) {
                    Transaction selectedTransaction = row.getItem();
                    populateTransactionInputs(selectedTransaction);
                }
            });
            return row;
        });

        // Create Input Fields for Transactions
        transactionIdInput = new TextField();
        transactionIdInput.setPromptText("ID");

        transactionBookIdInput = new TextField();
        transactionBookIdInput.setPromptText("Book ID");

        transactionPatronIdInput = new TextField();
        transactionPatronIdInput.setPromptText("Patron ID");

        transactionDateInput = new TextField();
        transactionDateInput.setPromptText("Transaction Date (YYYY-MM-DD)");

        borrowDateInput = new TextField();
        borrowDateInput.setPromptText("Borrow Date (YYYY-MM-DD)");

        returnDateInput = new TextField();
        returnDateInput.setPromptText("Return Date (YYYY-MM-DD)");

        // Create Buttons for Transactions
        Button addTransactionButton = new Button("Add Transaction");
        addTransactionButton.setOnAction(event -> addTransaction());

        Button updateTransactionButton = new Button("Update Transaction");
        updateTransactionButton.setOnAction(event -> updateTransaction());

        Button deleteTransactionButton = new Button("Delete Transaction");
        deleteTransactionButton.setOnAction(event -> deleteTransaction());

        Button clearTransactionFieldsButton = new Button("Clear Fields");
        clearTransactionFieldsButton.setOnAction(event -> clearTransactionInputs());

        // Layout for Transactions section
        HBox transactionButtons = new HBox();
        transactionButtons.getChildren().addAll(addTransactionButton, updateTransactionButton, deleteTransactionButton, clearTransactionFieldsButton);
        transactionButtons.setSpacing(10);

        VBox transactionBox = new VBox();
        transactionBox.getChildren().addAll(new Label("Transactions"), transactionTableView, new Label("Transaction Details"), transactionIdInput, transactionBookIdInput, transactionPatronIdInput, transactionDateInput, borrowDateInput, returnDateInput, transactionButtons);
        transactionBox.setSpacing(10);
        transactionBox.setPadding(new Insets(10));

        // Main Layout
        HBox mainLayout = new HBox();
        mainLayout.getChildren().addAll(bookBox, patronBox, transactionBox);
        mainLayout.setSpacing(10);

        Scene scene = new Scene(mainLayout, 1200, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Library Management System");
        primaryStage.show();

        // Load data into TableViews
        loadBooks();
        loadPatrons();
        loadTransactions();
    }

    private void addTransactionToStack(Transaction transaction) {
        transactionStack.push(transaction);
    }

    // Method to add transaction to queue
    private void addTransactionToQueue(Transaction transaction) {
        transactionQueue.enqueue(transaction);
    }
    private void addBook() {
        String title = titleInput.getText();
        String author = authorInput.getText();
        String isbn = isbnInput.getText();
        Date publishedDate = validateDate(publishedDateInput.getText());

        Book book = new Book(title, author, isbn, publishedDate);
        try {
            bookDAO.addBook(book);
            loadBooks();
            clearInputs();
        } catch (SQLException e) {
            showErrorDialog("Error adding book", e.getMessage());
        }
    }

    private void updateBook() {
        int id = Integer.parseInt(idInput.getText());
        String title = titleInput.getText();
        String author = authorInput.getText();
        String isbn = isbnInput.getText();
        Date publishedDate = validateDate(publishedDateInput.getText());

        Book book = new Book(id, title, author, isbn, publishedDate);
        try {
            bookDAO.updateBook(book);
            loadBooks();
            clearInputs();
        } catch (SQLException e) {
            showErrorDialog("Error updating book", e.getMessage());
        }
    }

    private void deleteBook() {
        int id = Integer.parseInt(idInput.getText());
        try {
            bookDAO.deleteBook(id);
            loadBooks();
            clearInputs();
        } catch (SQLException e) {
            showErrorDialog("Error deleting book", e.getMessage());
        }
    }

    private void addPatron() {
        String name = patronNameInput.getText();
        String email = patronEmailInput.getText();

        Patron patron = new Patron(name, email);
        try {
            patronDAO.addPatron(patron);
            loadPatrons();
            clearPatronInputs();
        } catch (SQLException e) {
            showErrorDialog("Error adding patron", e.getMessage());
        }
    }

    private void updatePatron() {
        int id = Integer.parseInt(patronIdInput.getText());
        String name = patronNameInput.getText();
        String email = patronEmailInput.getText();

        Patron patron = new Patron(id, name, email);
        try {
            patronDAO.updatePatron(patron);
            loadPatrons();
            clearPatronInputs();
        } catch (SQLException e) {
            showErrorDialog("Error updating patron", e.getMessage());
        }
    }

    private void deletePatron() {
        int id = Integer.parseInt(patronIdInput.getText());
        try {
            patronDAO.deletePatron(id);
            loadPatrons();
            clearPatronInputs();
        } catch (SQLException e) {
            showErrorDialog("Error deleting patron", e.getMessage());
        }
    }

    private void addTransaction() {
        int bookId = Integer.parseInt(transactionBookIdInput.getText());
        int patronId = Integer.parseInt(transactionPatronIdInput.getText());
        Date transactionDate = validateDate(transactionDateInput.getText());
        Date borrowDate = validateDate(borrowDateInput.getText());
        Date returnDate = validateDate(returnDateInput.getText());

        Transaction transaction = new Transaction(bookId, patronId, transactionDate, borrowDate, returnDate);
        try {
            transactionDAO.addTransaction(transaction);
            loadTransactions();
            clearTransactionInputs();
        } catch (SQLException e) {
            showErrorDialog("Error adding transaction", e.getMessage());
        }
    }

    private void updateTransaction() {
        int id = Integer.parseInt(transactionIdInput.getText());
        int bookId = Integer.parseInt(transactionBookIdInput.getText());
        int patronId = Integer.parseInt(transactionPatronIdInput.getText());
        Date transactionDate = validateDate(transactionDateInput.getText());
        Date borrowDate = validateDate(borrowDateInput.getText());
        Date returnDate = validateDate(returnDateInput.getText());

        Transaction transaction = new Transaction(id, bookId, patronId, transactionDate, borrowDate, returnDate);
        try {
            transactionDAO.updateTransaction(transaction);
            loadTransactions();
            clearTransactionInputs();
        } catch (SQLException e) {
            showErrorDialog("Error updating transaction", e.getMessage());
        }
    }

    private void deleteTransaction() {
        int id = Integer.parseInt(transactionIdInput.getText());
        try {
            transactionDAO.deleteTransaction(id);
            loadTransactions();
            clearTransactionInputs();
        } catch (SQLException e) {
            showErrorDialog("Error deleting transaction", e.getMessage());
        }
    }

    // Helper method to load all Books from the database
    private void loadBooks() {
        try {
            List<Book> books = bookDAO.getAllBooks();
            tableView.getItems().clear();
            tableView.getItems().addAll(books);
        } catch (SQLException e) {
            showErrorDialog("Error loading books", e.getMessage());
        }
    }

    // Helper method to load all Patrons from the database
    private void loadPatrons() {
        try {
            List<Patron> patrons = patronDAO.getAllPatrons();
            patronTableView.getItems().clear();
            patronTableView.getItems().addAll(patrons);
        } catch (SQLException e) {
            showErrorDialog("Error loading patrons", e.getMessage());
        }
    }

    // Helper method to load all Transactions from the database
    private void loadTransactions() {
        try {
            List<Transaction> transactions = transactionDAO.getAllTransactions();
            transactionTableView.getItems().clear();
            transactionTableView.getItems().addAll(transactions);
        } catch (SQLException e) {
            showErrorDialog("Error loading transactions", e.getMessage());
        }
    }

    // Helper method to validate and parse Date input
    private Date validateDate(String dateStr) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        try {
            java.util.Date parsedDate = format.parse(dateStr);
            return new Date(parsedDate.getTime());
        } catch (ParseException e) {
            showErrorDialog("Invalid Date", "Please enter a valid date in YYYY-MM-DD format.");
            return null;
        }
    }

    // Helper method to establish the database connection


    private void establishConnection() {
        String url = "jdbc:mysql://localhost:3306/library_db";
        String username = "root";
        String password = "";

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Connection Error", "Failed to connect to the database.");
        }
    }


    // Helper method to clear input fields for Books
    private void clearInputs() {
        idInput.clear();
        titleInput.clear();
        authorInput.clear();
        isbnInput.clear();
        publishedDateInput.clear();
    }

    // Helper method to clear input fields for Patrons
    private void clearPatronInputs() {
        patronIdInput.clear();
        patronNameInput.clear();
        patronEmailInput.clear();
    }

    // Helper method to clear input fields for Transactions
    private void clearTransactionInputs() {
        transactionIdInput.clear();
        transactionBookIdInput.clear();
        transactionPatronIdInput.clear();
        transactionDateInput.clear();
        borrowDateInput.clear();
        returnDateInput.clear();
    }

    // Helper method to populate input fields with selected Book data
    private void populateInputs(Book book) {
        idInput.setText(String.valueOf(book.getBookId()));
        titleInput.setText(book.getTitle());
        authorInput.setText(book.getAuthor());
        isbnInput.setText(book.getIsbn());
        publishedDateInput.setText(book.getPublishedDate().toString());
    }

    // Helper method to populate input fields with selected Patron data
    private void populatePatronInputs(Patron patron) {
        patronIdInput.setText(String.valueOf(patron.getPatronId()));
        patronNameInput.setText(patron.getName());
        patronEmailInput.setText(patron.getEmail());
    }

    // Helper method to populate input fields with selected Transaction data
    private void populateTransactionInputs(Transaction transaction) {
        transactionIdInput.setText(String.valueOf(transaction.getTransactionId()));
        transactionBookIdInput.setText(String.valueOf(transaction.getBookId()));
        transactionPatronIdInput.setText(String.valueOf(transaction.getPatronId()));
        transactionDateInput.setText(transaction.getTransactionDate().toString());
        borrowDateInput.setText(transaction.getBorrowDate().toString());
        returnDateInput.setText(transaction.getReturnDate().toString());
    }

    // Helper method to show error dialog
    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }



    public static void main(String[] args) {
        launch(args);
    }
}
