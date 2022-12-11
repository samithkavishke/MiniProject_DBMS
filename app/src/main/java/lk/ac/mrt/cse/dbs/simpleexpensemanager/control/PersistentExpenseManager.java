package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{
    private TransactionDAO persistentTransactionDAO;
    private AccountDAO persistentAccountDAO;
    private lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHandler dbHandler;
    public PersistentExpenseManager(Context context) throws ExpenseManagerException {
        this.dbHandler = new lk.ac.mrt.cse.dbs.simpleexpensemanager.control.DBHandler(context);
        setup();
    }

    @Override
    public void setup() throws ExpenseManagerException {
        persistentAccountDAO = new PersistentAccountDAO(this.dbHandler);
        this.setAccountsDAO(persistentAccountDAO);

        persistentTransactionDAO = new PersistentTransactionDAO(this.dbHandler);
        this.setTransactionsDAO(persistentTransactionDAO);
    }
}
