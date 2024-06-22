// BookList.java
import java.util.ArrayList;
import java.util.List;

public class BookList {
    private Node<Book> head;

    public void add(Book book) {
        Node<Book> newNode = new Node<>(book);
        if (head == null) {
            head = newNode;
        } else {
            Node<Book> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void remove(Book book) {
        if (head == null) return;

        if (head.data.equals(book)) {
            head = head.next;
            return;
        }

        Node<Book> current = head;
        while (current.next != null && !current.next.data.equals(book)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
        }
    }

    public void display() {
        Node<Book> current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    public List<Book> toList() {
        List<Book> list = new ArrayList<>();
        Node<Book> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}

