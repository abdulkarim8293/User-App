package com.abdulkarim.userapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "product.db";
    private static final String TABLE_CART = "carts";

    private static final String COLUMN_CART_ID = "cart_id";
    private static final String COLUMN_PRODUCT_ID = "product_id";
    private static final String COLUMN_PRODUCT_NAME = "product_name";
    private static final String COLUMN_PRODUCT_IMAGE = "product_image";
    private static final String COLUMN_PRODUCT_PRICE = "product_price";
    private static final String COLUMN_PRODUCT_QUANTITY = "product_quantity";


    public SQLiteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_CART_TABLE = "CREATE TABLE " + TABLE_CART + " ("
                + COLUMN_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_PRODUCT_ID + " TEXT,"
                + COLUMN_PRODUCT_NAME + " TEXT," + COLUMN_PRODUCT_IMAGE + " TEXT," + COLUMN_PRODUCT_PRICE + " TEXT," + COLUMN_PRODUCT_QUANTITY + " INTEGER" + ");";

        db.execSQL(CREATE_CART_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String DROP_CART_TABLE = "DROP TABLE IF EXISTS " + TABLE_CART;
        db.execSQL(DROP_CART_TABLE);

        onCreate(db);

    }

    public void addToCart(Cart cart) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_ID, cart.getId());
        values.put(COLUMN_PRODUCT_NAME, cart.getName());
        values.put(COLUMN_PRODUCT_IMAGE, cart.getImage());
        values.put(COLUMN_PRODUCT_PRICE, cart.getPrice());
        values.put(COLUMN_PRODUCT_QUANTITY, cart.getQuantity());

        // Inserting Row
        db.insert(TABLE_CART, null, values);
        db.close();

    }

    public boolean isInCart(String id) {

        String[] columns = {COLUMN_PRODUCT_ID};

        SQLiteDatabase db = this.getReadableDatabase();

        // selection criteria
        String selection = COLUMN_PRODUCT_ID + " = ?";

        // selection argument
        String[] selectionArgs = {id};

        // query user table with condition
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id FROM user WHERE user_email = 'admin@gmail.com';
         */
        Cursor cursor = db.query(TABLE_CART, //Table to query
                columns,                    //columns to return
                selection,                  //columns for the WHERE clause
                selectionArgs,              //The values for the WHERE clause
                null,                       //group the rows
                null,                      //filter by row groups
                null);                      //The sort order
        int cursorCount = cursor.getCount();
        cursor.close();
        db.close();

        if (cursorCount > 0) {
            return true;
        }

        return false;
    }

    public void updateCartProduct(Cart cart) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_PRODUCT_QUANTITY, cart.getQuantity());

        // updating row
        db.update(TABLE_CART, values, COLUMN_PRODUCT_ID + " = ?",
                new String[]{String.valueOf(cart.getId())});
        db.close();
    }

    public List<Cart> getAllCartProduct() {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CART_ID,
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY
        };
        // sorting orders
        String sortOrder = COLUMN_CART_ID + " ASC";
        List<Cart> cartList = new ArrayList<Cart>();

        SQLiteDatabase db = this.getReadableDatabase();

        // query the user table
        /**
         * Here query function is used to fetch records from user table this function works like we use sql query.
         * SQL query equivalent to this query function is
         * SELECT user_id,user_name,user_email,user_password FROM user ORDER BY user_name;
         */
        Cursor cursor = db.query(TABLE_CART, //Table to query
                columns,    //columns to return
                null,        //columns for the WHERE clause
                null,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                sortOrder); //The sort order


        // Traversing through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Cart cart = new Cart();
                cart.setId(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                cart.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)));
                cart.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)));
                cart.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)));
                cart.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))));
                // Adding user record to list
                cartList.add(cart);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        // return user list
        return cartList;
    }

    public Cart getSingleCartProduct(String product_id) {
        // array of columns to fetch
        String[] columns = {
                COLUMN_CART_ID,
                COLUMN_PRODUCT_ID,
                COLUMN_PRODUCT_NAME,
                COLUMN_PRODUCT_IMAGE,
                COLUMN_PRODUCT_PRICE,
                COLUMN_PRODUCT_QUANTITY
        };

        String selection = COLUMN_PRODUCT_ID + " = ?";
        String[] selectionArgs = {product_id};

        Cart cart = new Cart();

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_CART, //Table to query
                columns,    //columns to return
                selection,        //columns for the WHERE clause
                selectionArgs,        //The values for the WHERE clause
                null,       //group the rows
                null,       //filter by row groups
                null); //The sort order


        if (cursor.moveToFirst()) {
            do {

                cart.setId(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_ID)));
                cart.setName(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_NAME)));
                cart.setImage(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_IMAGE)));
                cart.setPrice(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_PRICE)));
                cart.setQuantity(Integer.parseInt(cursor.getString(cursor.getColumnIndex(COLUMN_PRODUCT_QUANTITY))));

            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return cart;
    }

    public void removeProductFromCart(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COLUMN_PRODUCT_ID    + "    = ?", new String[] { String.valueOf(id)});
    }

}
