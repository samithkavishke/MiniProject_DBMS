package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import android.content.Context;
import android.support.annotation.Nullable;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database.DbHandler;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class PersistentExpenseManager extends ExpenseManager {
    private DbHandler db;
    public PersistentExpenseManager(@Nullable Context context){
        this.db = new DbHandler(context);
        try{
            setup();
        }catch(Exception e){
            System.out.println("Setup error");
        }

    }
    @Override
    public void setup() throws ExpenseManagerException {
        TransactionDAO persistentTransactionDAO = new PersistentTransactionDAO(this.db);
        setTransactionsDAO(persistentTransactionDAO);

        AccountDAO persistentAccountDAO = new PersistentAccountDAO(this.db);
        setAccountsDAO(persistentAccountDAO);


        // dummy data
        Account dummyAcct1 = new Account("12345A", "Yoda Bank", "Anakin Skywalker", 10000.0);
        Account dummyAcct2 = new Account("78945Z", "Clone BC", "Obi-Wan Kenobi", 80000.0);
        getAccountsDAO().addAccount(dummyAcct1);
        getAccountsDAO().addAccount(dummyAcct2);
        getAccountsDAO().getAccountNumbersList();
    }
}
