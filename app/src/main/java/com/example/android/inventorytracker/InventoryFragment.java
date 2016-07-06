package com.example.android.inventorytracker;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class InventoryFragment extends Fragment {

    private ArrayList<Product> mProductList = new ArrayList<Product>();
    private ProductAdapter mProductAdapter;
    private InventoryDbHelper mDbHelper;

    public InventoryFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_products, container, false);

        final TextView noDataView = (TextView) rootView.findViewById(R.id.no_data_text_view);

        // Get a reference to the ListView, and attach this adapter to it.
        ListView listView = (ListView) rootView.findViewById(R.id.listview_product);

        mDbHelper = new InventoryDbHelper(getActivity());

        updateUI(listView);

        if (mDbHelper.isEmpty()) {
            noDataView.setVisibility(View.VISIBLE);
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ProductDetailActivity.class);
                intent.putExtra("productIndex", mProductAdapter.getItem(position).getId());
                startActivity(intent);
            }
        });

        return rootView;
    }

    public void updateUI(ListView listView) {
        mProductList = mDbHelper.getAllProducts();

        if (mProductAdapter == null) {
            mProductAdapter = new ProductAdapter(getActivity(), mProductList, this);
        } else {
            mProductAdapter.clear();
            mProductAdapter.addAll(mProductList);
            mProductAdapter.notifyDataSetChanged();
        }

        listView.setAdapter(mProductAdapter);
    }

    public ProductAdapter getProductAdapter() {
        return mProductAdapter;
    }

    public void notifyChange() {
        mProductAdapter.notifyDataSetChanged();
    }
}