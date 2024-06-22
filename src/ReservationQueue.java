// ReservationQueue.java
import java.util.LinkedList;

public class ReservationQueue {
    private LinkedList<Transaction> queue;

    public ReservationQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(Transaction transaction) {
        queue.addLast(transaction);
    }

    public Transaction dequeue() {
        return queue.removeFirst();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }
}
