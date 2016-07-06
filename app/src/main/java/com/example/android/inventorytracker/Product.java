package com.example.android.inventorytracker;

/**
 * Created by IrvinShandy on 7/3/16.
 */
public class Product {
    private int mId;
    private String mProductName;
    private int mProductPrice;
    private int mProductStockQuantity;
    private int mProductSoldQuantity;
    private String mImageUrl;
    private String mSupplierName;
    private String mSupplierContact;

    public Product() {
    }

    public Product(int id, String productName, int productPrice, int stockQuantity, int productSold, String imageUrl,
                   String supplierName, String supplierContact) {
        mId = id;
        mProductName = productName;
        mProductPrice = productPrice;
        mProductStockQuantity = stockQuantity;
        mProductSoldQuantity = productSold;
        mImageUrl = imageUrl;
        mSupplierName = supplierName;
        mSupplierContact = supplierContact;
    }

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getProductName() {
        return mProductName;
    }

    public void setProductName(String productName) {
        mProductName = productName;
    }

    public int getProductPrice() {
        return mProductPrice;
    }

    public void setProductPrice(int productPrice) {
        mProductPrice = productPrice;
    }

    public String getImageUrl() {
        return mImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        mImageUrl = imageUrl;
    }

    public String getSupplierName() {
        return mSupplierName;
    }

    public void setSupplierName(String supplierName) {
        mSupplierName = supplierName;
    }

    public String getSupplierContact() {
        return mSupplierContact;
    }

    public void setSupplierContact(String supplierContact) {
        mSupplierContact = supplierContact;
    }

    public int getStockQuantity() {
        return mProductStockQuantity;
    }

    public int getProductSoldQuantity() {
        return mProductSoldQuantity;
    }

    public void setProductSoldQuantity(int soldQuantity) {
        mProductSoldQuantity = soldQuantity;
    }

    public void setProductStockQuantity(int stockQuantity) {
        mProductStockQuantity = stockQuantity;
    }
}