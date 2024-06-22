// LibraryApp.java
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

public class LibraryApp extends Application {

    private TableView<Book> tableView;
    private TableView<Patron> patronTableView;
    private TextField titleInput, authorInput, isbnInput, publishedDateInput, idInput, patronNameInput, patronIdInput;
    private BookDAO bookDAO;
    private PatronDAO patronDAO;
    private Connection connection; // JDBC Connection object
    private TextField patronEmailInput;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Library Management System");
        // Establish database connection
        establishConnection();

        // Create DAOs with the established connection
        bookDAO = new BookDAO(connection);
        patronDAO = new PatronDAO(connection);

        // Create TableView for Books
        tableView = new TableView<>();
        TableColumn<Book, Integer> idColumn = new TableColumn<>("Book ID");
        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));

        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));

        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));

        TableColumn<Book, String> dateColumn = new TableColumn<>("Published Date");
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));

        tableView.getColumns().addAll(idColumn, titleColumn, authorColumn, isbnColumn, dateColumn);

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

        Button clearButton = new Button("Clear Fields");
        clearButton.setOnAction(event -> clearInputs());

        // Layout for Books section
        HBox bookButtons = new HBox();
        bookButtons.getChildren().addAll(addButton, updateButton, deleteButton, clearButton);
        bookButtons.setSpacing(10);

        VBox bookBox = new VBox();
        bookBox.getChildren().addAll(new Label("Books"), tableView, new Label("Book Details"), idInput, titleInput, authorInput, isbnInput, publishedDateInput, bookButtons);
        bookBox.setSpacing(10);
        bookBox.setPadding(new Insets(10));

        // Create TableView for Patrons
        patronTableView = new TableView<>();
        TableColumn<Patron, Integer> patronIdColumn = new TableColumn<>("Patron ID");
        patronIdColumn.setCellValueFactory(new PropertyValueFactory<>("patronId"));

        TableColumn<Patron, String> patronNameColumn = new TableColumn<>("Name");
        patronNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));

        patronTableView.getColumns().addAll(patronIdColumn, patronNameColumn);

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
        patronIdInput.setPromptText("Patron ID");

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
        patronBox.getChildren().addAll(new Label("Patrons"), patronTableView, new Label("Patron Details"), patronIdInput, patronNameInput, patronEmailInput ,patronButtons);
        patronBox.setSpacing(10);
        patronBox.setPadding(new Insets(10));

        // Main layout combining Books and Patrons sections
        HBox mainBox = new HBox();
        mainBox.getChildren().addAll(bookBox, patronBox);
        mainBox.setSpacing(20);
        mainBox.setPadding(new Insets(10));

        // Create scene and set it on the stage
        Scene scene = new Scene(mainBox, 1000, 600);
        primaryStage.setScene(scene);
        primaryStage.show();

        // Load initial data
        loadBooks();
        loadPatrons();
    }

    // Add methods for managing books
    private void addBook() {
        try {
            Book newBook = new Book(
                    titleInput.getText(),
                    authorInput.getText(),
                    isbnInput.getText(),
                    validateDate(publishedDateInput.getText())
            );
            bookDAO.addBook(newBook);
            loadBooks();  // Refresh the table view
            clearInputs();
        } catch (SQLException e) {
            showErrorDialog("Error adding book", e.getMessage());
        }
    }

    private void updateBook() {
        try {
            Book updatedBook = new Book(
                    Integer.parseInt(idInput.getText()),
                    titleInput.getText(),
                    authorInput.getText(),
                    isbnInput.getText(),
                    validateDate(publishedDateInput.getText())
            );
            bookDAO.updateBook(updatedBook);
            loadBooks();  // Refresh the table view
            clearInputs();
        } catch (SQLException | NumberFormatException e) {
            showErrorDialog("Error updating book", e.getMessage());
        }
    }

    private void deleteBook() {
        try {
            int bookId;
            if (!idInput.getText().isEmpty()) {
                bookId = Integer.parseInt(idInput.getText());
            } else {
                Book selectedBook = tableView.getSelectionModel().getSelectedItem();
                if (selectedBook != null) {
                    bookId = selectedBook.getBookId();
                } else {
                    showErrorDialog("No Book Selected", "Please select a book to delete or enter a book ID.");
                    return;
                }
            }
            bookDAO.deleteBook(bookId);
            loadBooks();  // Refresh the table view
            clearInputs();
        } catch (SQLException | NumberFormatException e) {
            showErrorDialog("Error deleting book", e.getMessage());
        }
    }

    // Add methods for managing patrons
    private void addPatron() {
        try {
            Patron newPatron = new Patron(patronNameInput.getText(), patronEmailInput.getText());
            patronDAO.addPatron(newPatron);
            loadPatrons();  // Refresh the patrons table
            clearPatronInputs();
        } catch (SQLException e) {
            showErrorDialog("Error adding patron", e.getMessage());
        }
    }


    private void updatePatron() {
        try {
            Patron updatedPatron = new Patron(
                    Integer.parseInt(patronIdInput.getText()),
                    patronNameInput.getText()
            );
            patronDAO.updatePatron(updatedPatron);
            loadPatrons();  // Refresh the patrons table
            clearPatronInputs();
        } catch (SQLException | NumberFormatException e) {
            showErrorDialog("Error updating patron", e.getMessage());
        }
    }

    private void deletePatron() {
        try {
            int patronId;
            if (!patronIdInput.getText().isEmpty()) {
                patronId = Integer.parseInt(patronIdInput.getText());
            } else {
                Patron selectedPatron = patronTableView.getSelectionModel().getSelectedItem();
                if (selectedPatron != null) {
                    patronId = selectedPatron.getPatronId();
                } else {
                    showErrorDialog("No Patron Selected", "Please select a patron to delete or enter a patron ID.");
                    return;
                }
            }
            patronDAO.deletePatron(patronId);
            loadPatrons();  // Refresh the patrons table
            clearPatronInputs();
        } catch (SQLException | NumberFormatException e) {
            showErrorDialog("Error deleting patron", e.getMessage());
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

    // Method to populate input fields with Book data
    private void populateInputs(Book book) {
        idInput.setText(String.valueOf(book.getBookId()));
        titleInput.setText(book.getTitle());
        authorInput.setText(book.getAuthor());
        isbnInput.setText(book.getIsbn());
        publishedDateInput.setText(book.getPublishedDate().toString());
    }

    // Method to populate input fields with Patron data
    private void populatePatronInputs(Patron patron) {
        patronIdInput.setText(String.valueOf(patron.getPatronId()));
        patronNameInput.setText(patron.getName());
        patronEmailInput.setText(patron.getEmail());
    }

    // Helper method to establish database connection
    private void establishConnection() {
        String url = "jdbc:mysql://localhost:3306/library_db"; // Replace with your database URL
        String username = "root"; // Replace with your database username
        String password = ""; // Replace with your database password

        try {
            connection = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            e.printStackTrace();
            showErrorDialog("Database Connection Error", "Failed to connect to the database.");
        }
    }

    // Helper method to validate date input
    private java.sql.Date validateDate(String dateStr) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
            format.setLenient(false);
            return new java.sql.Date(format.parse(dateStr).getTime());
        } catch (ParseException e) {
            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
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

    // Helper method to display error dialogs
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







//import javafx.application.Application;
//import javafx.geometry.Insets;
//import javafx.scene.Scene;
//import javafx.scene.control.*;
//import javafx.scene.control.cell.PropertyValueFactory;
//import javafx.scene.layout.HBox;
//import javafx.scene.layout.VBox;
//import javafx.stage.Stage;
//
//import java.sql.DriverManager;
//import java.sql.SQLException;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.List;
//import java.sql.Connection;
//
//public class LibraryApp extends Application {
//    private TableView<Book> tableView;
//    private TextField titleInput, authorInput, isbnInput, publishedDateInput, idInput;
//    private BookDAO bookDAO;
//    private Connection connection; // JDBC Connection object
//
//  //  @Override
////    public void start(Stage primaryStage) {
////        primaryStage.setTitle("Library Management System");
////
////        // Establish database connection
////        establishConnection();
////
////        // Create BookDAO with the established connection
////        bookDAO = new BookDAO(connection);
////
////        // Create TableView and other UI elements (unchanged from previous example)...
////    }
//
//    private void establishConnection() {
//        String url = "jdbc:mysql://localhost:3306/library_db"; // Replace with your database URL
//        String username = "root"; // Replace with your database username
//        String password = ""; // Replace with your database password
//
//        try {
//            connection = DriverManager.getConnection(url, username, password);
//        } catch (SQLException e) {
//            e.printStackTrace();
//            showErrorDialog("Database Connection Error", "Failed to connect to the database.");
//        }
//    }
//
//
//    @Override
//    public void start(Stage primaryStage) {
//        primaryStage.setTitle("Library Management System");
//        // Establish database connection
//        establishConnection();
//
//        // Create BookDAO with the established connection
//        bookDAO = new BookDAO(connection);
//
//        // Create TableView
//        tableView = new TableView<>();
//        TableColumn<Book, Integer> idColumn = new TableColumn<>("Book ID");
//        idColumn.setCellValueFactory(new PropertyValueFactory<>("bookId"));
//
//        TableColumn<Book, String> titleColumn = new TableColumn<>("Title");
//        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
//
//        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
//        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
//
//        TableColumn<Book, String> isbnColumn = new TableColumn<>("ISBN");
//        isbnColumn.setCellValueFactory(new PropertyValueFactory<>("isbn"));
//
//        TableColumn<Book, String> dateColumn = new TableColumn<>("Published Date");
//        dateColumn.setCellValueFactory(new PropertyValueFactory<>("publishedDate"));
//
//        tableView.getColumns().add(idColumn);
//        tableView.getColumns().add(titleColumn);
//        tableView.getColumns().add(authorColumn);
//        tableView.getColumns().add(isbnColumn);
//        tableView.getColumns().add(dateColumn);
//
//        // Set up table row click event
//        tableView.setRowFactory(tv -> {
//            TableRow<Book> row = new TableRow<>();
//            row.setOnMouseClicked(event -> {
//                if (!row.isEmpty()) {
//                    Book selectedBook = row.getItem();
//                    populateInputs(selectedBook);
//                }
//            });
//            return row;
//        });
//
//        // Create Input Fields
//        idInput = new TextField();
//        idInput.setPromptText("ID");
//
//        titleInput = new TextField();
//        titleInput.setPromptText("Title");
//
//        authorInput = new TextField();
//        authorInput.setPromptText("Author");
//
//        isbnInput = new TextField();
//        isbnInput.setPromptText("ISBN");
//
//        publishedDateInput = new TextField();
//        publishedDateInput.setPromptText("Published Date (YYYY-MM-DD)");
//
//        // Create Buttons
//        Button addButton = new Button("Add Book");
//        addButton.setOnAction(event -> addBook());
//
//        Button updateButton = new Button("Update Book");
//        updateButton.setOnAction(event -> updateBook());
//
//        Button deleteButton = new Button("Delete Book");
//        deleteButton.setOnAction(event -> deleteBook());
//
//        Button loadButton = new Button("Load Books");
//        loadButton.setOnAction(event -> loadBooks());
//
//        HBox inputBox = new HBox();
//        inputBox.setPadding(new Insets(10, 10, 10, 10));
//        inputBox.setSpacing(10);
//        inputBox.getChildren().addAll(idInput, titleInput, authorInput, isbnInput, publishedDateInput, addButton, updateButton, deleteButton, loadButton);
//
//        VBox vbox = new VBox();
//        vbox.getChildren().addAll(tableView, inputBox);
//        Scene scene = new Scene(vbox, 800, 600);
//
//        primaryStage.setScene(scene);
//        primaryStage.show();
//
//        loadBooks();  // Load initial data
//    }
//
//    private void loadBooks() {
//        try {
//            List<Book> books = bookDAO.getAllBooks();
//            tableView.getItems().clear();
//            tableView.getItems().addAll(books);
//        } catch (SQLException e) {
//            showErrorDialog("Error loading books", e.getMessage());
//        }
//    }
//
//    private void addBook() {
//        try {
//            Book newBook = new Book(
//                    titleInput.getText(),
//                    authorInput.getText(),
//                    isbnInput.getText(),
//                    validateDate(publishedDateInput.getText())
//            );
//            bookDAO.addBook(newBook);
//            loadBooks();  // Refresh the table view
//            clearInputs();
//        } catch (SQLException | IllegalArgumentException e) {
//            showErrorDialog("Error adding book", e.getMessage());
//        }
//    }
//
//    private void updateBook() {
//        try {
//            Book updatedBook = new Book(
//                    Integer.parseInt(idInput.getText()),
//                    titleInput.getText(),
//                    authorInput.getText(),
//                    isbnInput.getText(),
//                    validateDate(publishedDateInput.getText())
//            );
//            bookDAO.updateBook(updatedBook);
//            loadBooks();  // Refresh the table view
//            clearInputs();
//        } catch (SQLException | IllegalArgumentException e) {
//            showErrorDialog("Error updating book", e.getMessage());
//        }
//    }
//
//    private void deleteBook() {
//        try {
//            int bookId;
//            if (!idInput.getText().isEmpty()) {
//                bookId = Integer.parseInt(idInput.getText());
//            } else {
//                Book selectedBook = tableView.getSelectionModel().getSelectedItem();
//                if (selectedBook != null) {
//                    bookId = selectedBook.getBookId();
//                } else {
//                    showErrorDialog("No Book Selected", "Please select a book to delete or enter a book ID.");
//                    return;
//                }
//            }
//            bookDAO.deleteBook(bookId);
//            loadBooks();  // Refresh the table view
//            clearInputs();
//        } catch (SQLException | NumberFormatException e) {
//            showErrorDialog("Error deleting book", e.getMessage());
//        }
//    }
//
//    private void clearInputs() {
//        idInput.clear();
//        titleInput.clear();
//        authorInput.clear();
//        isbnInput.clear();
//        publishedDateInput.clear();
//    }
//
//    private void populateInputs(Book book) {
//        idInput.setText(String.valueOf(book.getBookId()));
//        titleInput.setText(book.getTitle());
//        authorInput.setText(book.getAuthor());
//        isbnInput.setText(book.getIsbn());
//        publishedDateInput.setText(book.getPublishedDate().toString());
//    }
//
//    private java.sql.Date validateDate(String dateStr) {
//        try {
//            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
//            format.setLenient(false);
//            return new java.sql.Date(format.parse(dateStr).getTime());
//        } catch (ParseException e) {
//            throw new IllegalArgumentException("Invalid date format. Please use YYYY-MM-DD.");
//        }
//    }
//
//    private void showErrorDialog(String title, String message) {
//        Alert alert = new Alert(Alert.AlertType.ERROR);
//        alert.setTitle(title);
//        alert.setHeaderText(null);
//        alert.setContentText(message);
//        alert.showAndWait();
//    }
//
//    public static void main(String[] args) {
//        launch(args);
//    }
//}





