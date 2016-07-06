package com.example.android.inventorytracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by IrvinShandy on 7/3/16.
 */
public class InventoryDbHelper extends SQLiteOpenHelper {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + InventoryContract.ProductEntry.TABLE_NAME + " (" +
                    InventoryContract.ProductEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                    InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_NAME + TEXT_TYPE + COMMA_SEP +
                    InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_QUANTITY + INTEGER_TYPE + COMMA_SEP +
                    InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_PRICE + INTEGER_TYPE + COMMA_SEP +
                    InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_SOLD + INTEGER_TYPE + COMMA_SEP +
                    InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_IMAGE_URL + TEXT_TYPE + COMMA_SEP +
                    InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_NAME + TEXT_TYPE + COMMA_SEP +
                    InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_CONTACT + TEXT_TYPE +
                    " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + InventoryContract.ProductEntry.TABLE_NAME;

    public InventoryDbHelper(Context context) {
        super(context, InventoryContract.DATABASE_NAME, null, InventoryContract.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_NAME, product.getProductName());
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_QUANTITY, product.getStockQuantity());
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_PRICE, product.getProductPrice());
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_IMAGE_URL, product.getImageUrl());
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_NAME, product.getSupplierName());
        values.put(InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_CONTACT, product.getSupplierContact());

        db.insert(InventoryContract.ProductEntry.TABLE_NAME,
                null,
                values);

        db.close();
    }

    public Product getProduct(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        String[] projection = {
                InventoryContract.ProductEntry._ID,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_NAME,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_PRICE,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_QUANTITY,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_SOLD,
                InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_IMAGE_URL,
                InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_NAME,
                InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_CONTACT
        };

        Cursor c = db.query(InventoryContract.ProductEntry.TABLE_NAME, projection,
                InventoryContract.ProductEntry._ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        c.moveToFirst();

        Product product = new Product(c.getInt((c.getColumnIndex(InventoryContract.ProductEntry._ID))),
                c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_NAME))),
                c.getInt((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_PRICE))),
                c.getInt((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_QUANTITY))),
                c.getInt((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_SOLD))),
                c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_IMAGE_URL))),
                c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_NAME))),
                c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_CONTACT)))
        );
        // return contact
        return product;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + InventoryContract.ProductEntry.TABLE_NAME;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        if (c.moveToFirst()) {
            do {
                Product td = new Product(c.getInt((c.getColumnIndex(InventoryContract.ProductEntry._ID))),
                        c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_NAME))),
                        c.getInt((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_PRICE))),
                        c.getInt((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_QUANTITY))),
                        c.getInt((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_SOLD))),
                        c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_PRODUCT_IMAGE_URL))),
                        c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_NAME))),
                        c.getString((c.getColumnIndex(InventoryContract.ProductEntry.COLUMN_NAME_SUPPLIER_CONTACT)))
                );
                products.add(td);
            } while (c.moveToNext());
        }

        return products;
    }

    public int updateProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("productName", product.getProductName());
        values.put("productQuantity", product.getStockQuantity());
        values.put("productSold", product.getProductSoldQuantity());
        values.put("productPrice", product.getProductPrice());
        values.put("productImageUrl", product.getImageUrl());
        values.put("supplierName", product.getSupplierName());
        values.put("supplierContact", product.getSupplierContact());


        int i = db.update(InventoryContract.ProductEntry.TABLE_NAME,
                values,
                InventoryContract.ProductEntry._ID + " = ?",
                new String[]{String.valueOf(product.getId())});

        db.close();

        return i;
    }

    public void deleteProduct(Product product) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(InventoryContract.ProductEntry.TABLE_NAME,
                InventoryContract.ProductEntry._ID + " = ?",
                new String[]{String.valueOf(product.getId())});

        db.close();
    }

    public boolean isEmpty() {
        boolean flag = true;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cur = db.rawQuery("SELECT COUNT(*) FROM " + InventoryContract.ProductEntry.TABLE_NAME, null);
        if (cur != null && cur.moveToFirst()) {
            flag = (cur.getInt(0) == 0);
        }
        cur.close();
        return flag;
    }
}
