package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;
import android.content.ContentValues;

import java.util.ArrayList;
import java.util.*;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {
    private final DbHandler db;

    private static final String TABLE_ACCOUNT = "account";

    private static final String ACCOUNT_COL = "account_no";
    private static final String BANK_COL = "bank_name";
    private static final String HOLDER_COL = "holder_name";
    private static final String BALANCE_COL = "init_balance";



    public PersistentAccountDAO(DbHandler db){
        this.db = db;
    }


    @Override
    public List<String> getAccountNumbersList() {
        List<Account> accounts = this.db.readAccounts();
        List<String> accountNumbers = new ArrayList<String>();
        for(int i = 0; i < accounts.size(); i++){
            accountNumbers.add(accounts.get(i).getAccountNo());
        }
        return accountNumbers;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accounts = this.db.readAccounts();
        //List<Account> accounts = new ArrayList<Account>();
        return accounts;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        List<Account> accounts = this.db.readAccounts();
        for(int i = 0; i < accounts.size(); i++){
            if (accounts.get(i).getAccountNo().equals(accountNo)){
                return accounts.get(i);
            }
        }
        throw new InvalidAccountException("Invalid Account Number");
    }

    @Override
    public void addAccount(Account account) {
        this.db.addNewAccount(account.getAccountNo(),account.getBankName(),account.getAccountHolderName(),account.getBalance());

    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        int result = this.db.deleteData("account","accountno",accountNo);
        if(result == 0){
            throw new InvalidAccountException("Account number is invalid");
        }


    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        double balance = 0;
        double total = 0;
        try{
            Account acc = getAccount(accountNo);
            balance = acc.getBalance();
        }catch(Exception e){
            throw new InvalidAccountException("Invalid Account Number");
        }

        if (expenseType == ExpenseType.EXPENSE){
            if(balance < amount){
                throw new InvalidAccountException("Insufficient Account Balance");
            }
            total = balance-amount;
        }else{
            total = amount +balance;
        }
        boolean result = this.db.updateData(accountNo,total);
        if(!result){
            throw new InvalidAccountException("Account number is invalid");
        }
    }


}
