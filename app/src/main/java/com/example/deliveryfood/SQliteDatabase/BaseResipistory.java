package com.example.deliveryfood.SQliteDatabase;
        import android.app.Activity;
        import android.content.ContentValues;
        import android.content.Context;
        import android.database.Cursor;
        import android.database.SQLException;
        import android.database.sqlite.SQLiteDatabase;
        import android.database.sqlite.SQLiteException;
        import android.database.sqlite.SQLiteOpenHelper;
        import android.util.Log;


        import com.example.deliveryfood.Model.Order;

        import java.io.FileOutputStream;
        import java.io.IOException;
        import java.io.InputStream;
        import java.io.OutputStream;
        import java.util.ArrayList;
public class BaseResipistory extends SQLiteOpenHelper {

    private static String DB_PATH = "/data/data/com.example.deliveryfood/databases/";
    private static String DB_NAME = "DFoodDB.db";
    private SQLiteDatabase myDataBase;
    private final Context myContext;

    public BaseResipistory(Context context) {
        super(context, DB_NAME, null, 1);
        this.myContext = context;
        Log.e("Path 1", DB_PATH);
    }


    public void createDataBase() throws IOException {
        boolean dbExist = checkDataBase();
        if (dbExist) {
        } else {
            this.getReadableDatabase();
            try {
                copyDataBase();
            } catch (IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase checkDB = null;
        try {
            String myPath = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
        } catch (SQLiteException e) {
        }
        if (checkDB != null) {
            checkDB.close();
        }
        return checkDB != null ? true : false;
    }

    private void copyDataBase() throws IOException {
        InputStream myInput = myContext.getAssets().open(DB_NAME);
        String outFileName = DB_PATH + DB_NAME;
        OutputStream myOutput = new FileOutputStream(outFileName);
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    public void openDataBase() throws SQLException {
        String myPath = DB_PATH + DB_NAME;
        myDataBase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);

    }

    @Override
    public synchronized void close() {
        if (myDataBase != null)
            myDataBase.close();
        super.close();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (newVersion > oldVersion)
            try {
                copyDataBase();
            } catch (IOException e) {
                e.printStackTrace();

            }
    }

    public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        return myDataBase.query("OrderDetail", null, null, null, null, null, null);
    }


    public void insert(Order order) {
        openDataBase();
      //  myDataBase=this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductId",order.getProductId());
        contentValues.put("ProductName",order.getProductName());
        contentValues.put("Price",order.getPrice());
        contentValues.put("Quantity",order.getQuantity());
        contentValues.put("Discount",order.getDiscount());
        myDataBase.insert("OrderDetail", null, contentValues);
        close();
    }
    public void cleanCart() {
        openDataBase();
        myDataBase.execSQL("DELETE FROM OrderDetail");
        close();
    }
    public void update(Order order, String productName,String productId,String price,String quantity,String discount){
        openDataBase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("ProductName",productName);
        contentValues.put("ProductId",productId);
        contentValues.put("Price",price);
        contentValues.put("Quantity",quantity);
        contentValues.put("Discount",discount);
        myDataBase.update("OrderDetail", contentValues, "id = ?", new String[] { String.valueOf(order.getProductId())+ ""});
        close();

    }
        public ArrayList<Order> getinform() {
        openDataBase();
        ArrayList<Order>orderList = new ArrayList<>();;
        //database = BaseResipistory.initDatabase(this, "StoreManagement.db");
        Cursor c = myDataBase.rawQuery("SELECT * FROM OrderDetail ", null);

        if (c.moveToFirst()) {
            do {
                Order order = new Order();
                order.setProductId(c.getString(c.getColumnIndex("ProductId")));
                order.setProductName(c.getString(c.getColumnIndex("ProductName")));
                order.setQuantity(c.getString(c.getColumnIndex("Quantity")));
                order.setPrice(c.getString(c.getColumnIndex("Price")));
                order.setDiscount(c.getString(c.getColumnIndex("Discount")));
                //khohang.setId(c.getInt(c.getColumnIndex(Contract.InforrmationTable._ID)));
                orderList.add(order);


            } while (c.moveToNext());
        }


        c.close();
        close();
        return orderList;
    }
//    public ArrayList<Store> getListProduct() {
//        Store student = null;
//        ArrayList<Store> StudentList = new ArrayList<>();
//        openDataBase();
//        Cursor cursor = myDataBase.rawQuery("SELECT * FROM Store", null);
//        cursor.moveToFirst();
//        while (!cursor.isAfterLast()) {
//            student = new Store(cursor.getInt(0),
//                    cursor.getString(1),
//                    cursor.getInt(2),cursor.getDouble(3));
//            StudentList.add(student);
//            cursor.moveToNext();
//        }
//        cursor.close();
//        close();
//        return StudentList;
//    }
}

