package com.example.android.inventorytracker;

import android.provider.BaseColumns;

/**
 * Created by IrvinShandy on 7/3/16.
 */
public class InventoryContract {
    public static final int DATABASE_VERSION = 3;
    public static final String DATABASE_NAME = "InventoryDB";

    public InventoryContract() {
    }

    public static abstract class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "inventory";
        public static final String COLUMN_NAME_PRODUCT_NAME = "productName";
        public static final String COLUMN_NAME_PRODUCT_QUANTITY = "productQuantity";
        public static final String COLUMN_NAME_PRODUCT_PRICE = "productPrice";
        public static final String COLUMN_NAME_PRODUCT_SOLD = "productSold";
        public static final String COLUMN_NAME_PRODUCT_IMAGE_URL = "productImageUrl";
        public static final String COLUMN_NAME_SUPPLIER_NAME = "supplierName";
        public static final String COLUMN_NAME_SUPPLIER_CONTACT = "supplierContact";
    }
}
