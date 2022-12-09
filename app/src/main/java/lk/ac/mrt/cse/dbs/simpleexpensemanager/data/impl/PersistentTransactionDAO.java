package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.PersistentExpenseManager;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentTransactionDAO implements TransactionDAO {
    private DbHandler db;
    private static final String TRANSACTION_COL = "transaction_id";
    private static final String DATE_COL = "date";
    private static final String TYPE_COL = "type";
    private static final String AMOUNT_COL = "amount";
    private static final String ACCOUNT_COL = "account_no";

    public PersistentTransactionDAO(DbHandler db){
        this.db = db;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        if(expenseType == ExpenseType.EXPENSE){
            PersistentAccountDAO pa = new PersistentAccountDAO(this.db);
            try {
                Account user = pa.getAccount(accountNo);
                if(user.getBalance() < amount){
                    return;
                }
            }catch (Exception e){
                System.out.println("Invalid Account");
            }
        }
        String sDate = date.toString();
        this.db.addNewTransaction(sDate,accountNo, amount, getStringExpense(expenseType));
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions = this.db.readTransactions(0);
        return transactions;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions = this.db.readTransactions(limit);
        return transactions;
    }

    public ExpenseType getExpense(String expense){
        if(expense.equals("Expense")){
            return ExpenseType.EXPENSE;
        }else{
            return ExpenseType.INCOME;
        }
    }

    public String getStringExpense(ExpenseType expense){
        if(expense == ExpenseType.EXPENSE){
            return "Expense";
        }else{
            return "Income";
        }
    }

    public Date stringToDate(String strDate){
        SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        Date date = new Date();
        try{
            date = dateFormat.parse(strDate);
        }catch(Exception e){
            System.out.println(e);
        }
        return date;
    }

    public void removeTransaction(String transactionNo) throws InvalidAccountException {
        int result = this.db.deleteData("transaction","transaction_no",transactionNo);
        if(result == 0){
            throw new InvalidAccountException("Transactoin number is invalid");
        }
    }

}

