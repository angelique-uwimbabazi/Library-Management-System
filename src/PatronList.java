// PatronList.java
import java.util.ArrayList;
import java.util.List;

public class PatronList {
    private Node<Patron> head;

    public void add(Patron patron) {
        Node<Patron> newNode = new Node<>(patron);
        if (head == null) {
            head = newNode;
        } else {
            Node<Patron> current = head;
            while (current.next != null) {
                current = current.next;
            }
            current.next = newNode;
        }
    }

    public void remove(Patron patron) {
        if (head == null) return;

        if (head.data.equals(patron)) {
            head = head.next;
            return;
        }

        Node<Patron> current = head;
        while (current.next != null && !current.next.data.equals(patron)) {
            current = current.next;
        }

        if (current.next != null) {
            current.next = current.next.next;
        }
    }

    public void display() {
        Node<Patron> current = head;
        while (current != null) {
            System.out.println(current.data);
            current = current.next;
        }
    }

    public List<Patron> toList() {
        List<Patron> list = new ArrayList<>();
        Node<Patron> current = head;
        while (current != null) {
            list.add(current.data);
            current = current.next;
        }
        return list;
    }
}
