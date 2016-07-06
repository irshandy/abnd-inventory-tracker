package com.example.android.inventorytracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private InventoryDbHelper mDbHelper = new InventoryDbHelper(MainActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final InventoryFragment inventoryFragment = new InventoryFragment();

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new InventoryFragment())
                    .commit();
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.add_product);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ProductFormActivity.class);
                startActivity(intent);
            }
        });
    }
}
