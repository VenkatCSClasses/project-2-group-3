package trade;
import java.util.ArrayList;

public class TransactionLog {
    private ArrayList<Transaction> transactionLog;

    public TransactionLog() {
        this.transactionLog = new ArrayList<>();
    }

    public ArrayList<Transaction> getTransactionLog() {
        return transactionLog;
    }

    public void addTransaction(Transaction transaction) {
        transactionLog.add(transaction);
    }

    public int size() {
        return transactionLog.size();
    }

    public Transaction get(int index) {
        return transactionLog.get(index);
    }
}