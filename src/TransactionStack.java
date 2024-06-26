import java.util.LinkedList;

public class TransactionStack {

    private LinkedList<Transaction> stack;

    public TransactionStack() {
        stack = new LinkedList<>();
    }

    public void push(Transaction transaction) {
        stack.push(transaction);
    }

    public Transaction pop() {
        return stack.pop();
    }

    public Transaction peek() {
        return stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }
}
