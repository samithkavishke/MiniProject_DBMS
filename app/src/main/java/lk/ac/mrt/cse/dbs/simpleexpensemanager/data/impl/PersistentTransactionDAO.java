package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private DBHandler dbHandler;
    public PersistentTransactionDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String string_date = date.toString();
        String string_expense_type = getStringExpense(expenseType);

//        Transaction transaction = new Transaction(date,accountNo,expenseType,amount);
        dbHandler.addNewTransaction(string_date,accountNo,string_expense_type,amount);
    }

    private String getStringExpense(ExpenseType expenseType) {
        if (expenseType == ExpenseType.EXPENSE){
            return "Expense";
        }else{
            return "Income";
        }
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactionList = dbHandler.getTransactionLogs(0);
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactionList = dbHandler.getTransactionLogs(limit);
        return transactionList;
    }
}
