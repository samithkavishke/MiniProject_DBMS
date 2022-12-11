package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private static final String TABLE_NAME = "Accounts";

    private static final String ACC_NO_COL = "Account No.";

    private static final String TYPE_COL = "Type";

    private static final String AMOUNT_COL = "Amount";

    private static final String DATE_COL = "Date";

    private final DBHandler dbHandler;

    public PersistentAccountDAO(DBHandler dbHandler) {
        this.dbHandler = dbHandler;
    }

    @Override
    public List<String> getAccountNumbersList() {
        return this.dbHandler.getAccountNoList();
    }

    @Override
    public List<Account> getAccountsList() {
        return this.dbHandler.getAccounts();
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        return this.dbHandler.getAccount(accountNo);
    }

    @Override
    public void addAccount(Account account) {
        this.dbHandler.addNewAccount(account.getAccountNo(), account.getBankName(), account.getAccountHolderName(), account.getBalance());
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        this.dbHandler.removeAccount(accountNo);
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = getAccount(accountNo);
        double accBalance = account.getBalance();;
        if(expenseType == ExpenseType.EXPENSE){
            accBalance -= amount;
        }else{
            accBalance += amount;
        }
        if (accBalance >=0){
            this.dbHandler.updateBalance(account,accBalance);
        }else{

        }
    }
}
