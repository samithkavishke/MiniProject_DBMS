package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;

public class DbHandler extends SQLiteOpenHelper {

    private static final String DB_NAME = "200742E";
    private static final int DB_VERSION = 1;
    private static final String TABLE_ACCOUNT = "account";
    private static final String TABLE_TRANSACTIONS = "transactions";
    private static final String ACCOUNT_COL = "account_no";
    private static final String BANK_COL = "bank_name";
    private static final String HOLDER_COL = "holder_name";
    private static final String BALANCE_COL = "init_balance";
    private static final String TRANSACTION_COL = "transaction_id";
    private static final String DATE_COL = "date";
    private static final String TYPE_COL = "type";
    private static final String AMOUNT_COL = "amount";

    public DbHandler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String query1 = "CREATE TABLE " + TABLE_ACCOUNT + " ("
                + ACCOUNT_COL + " TEXT PRIMARY KEY, "
                + BANK_COL + " TEXT,"
                + HOLDER_COL + " TEXT,"
                + BALANCE_COL + " REAL);";
        String query2 = "CREATE TABLE " + TABLE_TRANSACTIONS + " ("
                + TRANSACTION_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DATE_COL + " TEXT,"
                + ACCOUNT_COL + " TEXT,"
                + TYPE_COL + " TEXT,"
                + AMOUNT_COL + " REAL)";

        sqLiteDatabase.execSQL(query1);
        sqLiteDatabase.execSQL(query2);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_ACCOUNT);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+TABLE_TRANSACTIONS);
        onCreate(sqLiteDatabase);
    }

    public void addNewAccount(String account, String bank, String holder_name, double balance) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(ACCOUNT_COL, account);
        values.put(BANK_COL, bank);
        values.put(HOLDER_COL, holder_name);
        values.put(BALANCE_COL, balance);

        db.insert(TABLE_ACCOUNT, null, values);

        db.close();
    }

    public void addNewTransaction(String date,String account, double amount, String type) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        //values.put(TRANSACTION_COL, transaction_id);
        values.put(DATE_COL, date);
        values.put(ACCOUNT_COL, account);
        values.put(TYPE_COL,type);
        values.put(AMOUNT_COL, amount);

        db.insert(TABLE_TRANSACTIONS, null, values);

        db.close();
    }

    public ArrayList<Account> readAccounts() {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();

        // on below line we are creating a cursor with query to read data from database.
        Cursor cursorAccount = db.rawQuery("SELECT * FROM " + TABLE_ACCOUNT, null);

        // on below line we are creating a new array list.
        ArrayList<Account> accountArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorAccount.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                accountArrayList.add(new Account(cursorAccount.getString(0),
                        cursorAccount.getString(1),
                        cursorAccount.getString(2),
                        cursorAccount.getDouble(3)));
            } while (cursorAccount.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorAccount.close();
        return accountArrayList;
    }
    public ArrayList<Transaction> readTransactions(int limit) {
        // on below line we are creating a
        // database for reading our database.
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursorTransactions;
        if (limit == 0){
            cursorTransactions = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS, null);
        }
        else {
            // on below line we are creating a cursor with query to read data from database.
            cursorTransactions = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " LIMIT " + String.valueOf(limit), null);
        }
        //cursorTransactions = db.rawQuery("SELECT * FROM " + TABLE_TRANSACTIONS + " LIMIT 10", null);
        // on below line we are creating a new array list.
        ArrayList<Transaction> transactionsArrayList = new ArrayList<>();

        // moving our cursor to first position.
        if (cursorTransactions.moveToFirst()) {
            do {
                // on below line we are adding the data from cursor to our array list.
                ExpenseType type;
                if(cursorTransactions.getString(3).equals("Expense")){
                    type = ExpenseType.EXPENSE;
                }
                else{
                    type = ExpenseType.INCOME;
                }
                SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
                Date date = new Date();
                try{
                    date = dateFormat.parse(cursorTransactions.getString(1));
                }catch(Exception e){
                    System.out.println(e);
                }

                transactionsArrayList.add(new Transaction(date,
                        cursorTransactions.getString(2),
                        type,
                        cursorTransactions.getDouble(4)));
            } while (cursorTransactions.moveToNext());
            // moving our cursor to next.
        }
        // at last closing our cursor
        // and returning our array list.
        cursorTransactions.close();
        return transactionsArrayList;
    }
    public boolean updateData(String accountNo, double balance) {

        // calling a method to get writable database.
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        // on below line we are passing all values
        // along with its key and value pair.
        values.put(BALANCE_COL, balance);
        long result;
        try{
            result = db.update(TABLE_ACCOUNT, values, "account_no = ?", new String[]{accountNo});
        }catch (Exception e){

            result = -1;
        }
        // on below line we are calling a update method to update our database and passing our values.
        // and we are comparing it with name of our course which is stored in original name variable.
        db.close();
        if(result == -1){
            return false;
        }
        else{
            return true;
        }
    }
    public Integer deleteData(String table_name, String column, String id){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(table_name, column+" = ?", new String[] {id});
    }

    public void deleteTableContent(String table_name){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+table_name);
    }
}
