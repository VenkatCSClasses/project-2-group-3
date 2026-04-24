package storage;

import java.util.ArrayList;
import java.util.List;

import trade.Transaction;

public class TransactionStorage {
    private List<Transaction> transactions;

    public TransactionStorage() {
        transactions = new ArrayList<>();
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }
}