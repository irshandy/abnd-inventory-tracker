package com.example.android.inventorytracker;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class ProductDetailActivity extends AppCompatActivity {
    Product currentProduct = new Product();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Intent intent = getIntent();
        final int productIndex = intent.getIntExtra("productIndex", 0);

        final InventoryDbHelper dbHelper = new InventoryDbHelper(ProductDetailActivity.this);

        currentProduct = dbHelper.getProduct(productIndex);
        updateInfo();

        final Button receiveShipment = (Button) findViewById(R.id.receive_product_button);
        receiveShipment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                currentProduct.setProductStockQuantity(currentProduct.getStockQuantity() + 1);
                updateInfo();
            }
        });

        Button sellItem = (Button) findViewById(R.id.sell_product_button);
        sellItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProduct.getStockQuantity() > 0) {
                    currentProduct.setProductStockQuantity(currentProduct.getStockQuantity() - 1);
                    currentProduct.setProductSoldQuantity(currentProduct.getProductSoldQuantity() + 1);
                    updateInfo();
                } else {
                    Toast.makeText(ProductDetailActivity.this, "You don't have enough stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button orderItem = (Button) findViewById(R.id.order_product_button);
        orderItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent send = new Intent(Intent.ACTION_SENDTO);
                String mailingAddress = currentProduct.getSupplierContact();
                String messageSubject = currentProduct.getProductName() + " Order Request";
                String uriText = "mailto:" + Uri.encode(mailingAddress) +
                        "?subject=" + Uri.encode(messageSubject);
                Uri uri = Uri.parse(uriText);

                send.setData(uri);
                startActivity(Intent.createChooser(send, "Send mail..."));
            }
        });

        Button deleteItem = (Button) findViewById(R.id.delete_product_button);
        deleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(ProductDetailActivity.this)
                        .setTitle("Delete entry")
                        .setMessage("Are you sure you want to delete this entry?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dbHelper.deleteProduct(currentProduct);
                                Intent intent = new Intent(ProductDetailActivity.this, MainActivity.class);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // do nothing
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }

    private void updateInfo() {
        TextView productNameTextView = (TextView) findViewById(R.id.product_name_text_view);
        productNameTextView.setText(currentProduct.getProductName());

        TextView productQuantityTextView = (TextView) findViewById(R.id.product_quantity_text_view);
        productQuantityTextView.setText("Quantity: " + currentProduct.getStockQuantity());

        TextView productPriceTextView = (TextView) findViewById(R.id.product_price_text_view);
        productPriceTextView.setText("$" + currentProduct.getProductPrice());

        TextView productSoldTextView = (TextView) findViewById(R.id.product_sold_text_view);
        productSoldTextView.setText("Item Sold: " + currentProduct.getProductSoldQuantity());

        TextView supplierNameTextView = (TextView) findViewById(R.id.supplier_name_text_view);
        supplierNameTextView.setText("Supplier: " + currentProduct.getSupplierName());

        TextView supplierContactTextView = (TextView) findViewById(R.id.supplier_contact_text_view);
        supplierContactTextView.setText("Contact: " + currentProduct.getSupplierContact());

        ImageView imageView = (ImageView) findViewById(R.id.thumbnail_image_view);
        new ImageLoadTask(currentProduct.getImageUrl(), imageView).execute();
    }

    public class ImageLoadTask extends AsyncTask<Void, Void, Bitmap> {

        private String url;
        private ImageView imageView;

        public ImageLoadTask(String url, ImageView imageView) {
            this.url = url;
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            try {
                URL urlConnection = new URL(url);
                HttpURLConnection connection = (HttpURLConnection) urlConnection
                        .openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);
            imageView.setImageBitmap(result);
        }
    }
}
