package com.example.android.inventorytracker;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by IrvinShandy on 7/3/16.
 */
public class ProductAdapter extends ArrayAdapter<Product> {

    InventoryFragment mFragment;

    public ProductAdapter(Activity context, ArrayList<Product> products, InventoryFragment fragment) {
        super(context, 0, products);
        mFragment = fragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Product currentProduct = getItem(position);
        final InventoryDbHelper dbHelper = new InventoryDbHelper(getContext());
        final InventoryFragment inventoryFragment = new InventoryFragment();

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_products, parent, false);
        }

        Button saleButton = (Button) listItemView.findViewById(R.id.sale_button);
        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentProduct.getStockQuantity() > 0) {
                    currentProduct.setProductStockQuantity(currentProduct.getStockQuantity() - 1);
                    currentProduct.setProductSoldQuantity(currentProduct.getProductSoldQuantity() + 1);
                    dbHelper.updateProduct(currentProduct);
                    mFragment.notifyChange();
                } else {
                    Toast.makeText(getContext(), "You don't have enough stock!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TextView productNameTextView = (TextView) listItemView.findViewById(R.id.product_name_text_view);
        productNameTextView.setText(currentProduct.getProductName());

        TextView productQuantityTextView = (TextView) listItemView.findViewById(R.id.product_quantity_text_view);
        productQuantityTextView.setText("Quantity: " + currentProduct.getStockQuantity());

        TextView productPriceTextView = (TextView) listItemView.findViewById(R.id.product_price_text_view);
        productPriceTextView.setText("$" + currentProduct.getProductPrice());

        TextView productSoldTextView = (TextView) listItemView.findViewById(R.id.product_sold_text_view);
        productSoldTextView.setText("Item Sold: " + currentProduct.getProductSoldQuantity());

        ImageView thumbnailImageView = (ImageView) listItemView.findViewById(R.id.thumbnail_image_view);
        new ImageLoadTask(currentProduct.getImageUrl(), thumbnailImageView).execute();

        return listItemView;
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
