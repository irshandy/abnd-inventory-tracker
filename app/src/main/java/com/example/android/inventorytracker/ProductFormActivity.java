package com.example.android.inventorytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ProductFormActivity extends AppCompatActivity {
    InventoryDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_form);

        mDbHelper = new InventoryDbHelper(ProductFormActivity.this);

        final EditText productNameEntry = (EditText) findViewById(R.id.product_name_entry);
        final EditText productPriceEntry = (EditText) findViewById(R.id.product_price_entry);
        final EditText productQuantityEntry = (EditText) findViewById(R.id.product_quantity_entry);
        final EditText productImageUrlEntry = (EditText) findViewById(R.id.product_image_url_entry);
        final EditText supplierNameEntry = (EditText) findViewById(R.id.supplier_name_entry);
        final EditText supplierContactEntry = (EditText) findViewById(R.id.supplier_contact_entry);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String productName = String.valueOf(productNameEntry.getText());
                String productQuantityString = String.valueOf(productQuantityEntry.getText().toString());
                String productPriceString = String.valueOf(productPriceEntry.getText().toString());
                String productImageUrl = String.valueOf(productImageUrlEntry.getText().toString());
                String supplierName = String.valueOf(supplierNameEntry.getText());
                String supplierContact = String.valueOf(supplierContactEntry.getText());
                int productQuantity = 0;
                int productPrice = 0;

                try {
                    productQuantity = Integer.parseInt(productQuantityString);
                } catch (NumberFormatException e) {
                    Log.e(ProductFormActivity.class.getSimpleName(), "Product Quantity Not a Number");
                }

                try {
                    productPrice = Integer.parseInt(productPriceString);
                } catch (NumberFormatException e) {
                    Log.e(ProductFormActivity.class.getSimpleName(), "Product Price Not a Number");
                }

                if (productName.matches("") || productQuantityString.matches("") || productPriceString.matches("") ||
                        productImageUrl.matches("") || supplierName.matches("") || supplierContact.matches("")) {
                    Toast.makeText(ProductFormActivity.this, "You missed some information!", Toast.LENGTH_SHORT).show();
                } else {
                    Product product = new Product(1, productName, productPrice, productQuantity, 0, productImageUrl, supplierName, supplierContact);
                    mDbHelper.addProduct(product);

                    Intent intent = new Intent(ProductFormActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });
    }
}
