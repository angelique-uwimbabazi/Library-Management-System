import java.util.LinkedList;

public class TransactionQueue {

    private LinkedList<Transaction> queue;

    public TransactionQueue() {
        queue = new LinkedList<>();
    }

    public void enqueue(Transaction transaction) {
        queue.addLast(transaction);
    }

    public Transaction dequeue() {
        return queue.poll();
    }

    public Transaction peek() {
        return queue.peek();
    }

    public boolean isEmpty() {
        return queue.isEmpty();
    }

    public int size() {
        return queue.size();
    }
}
