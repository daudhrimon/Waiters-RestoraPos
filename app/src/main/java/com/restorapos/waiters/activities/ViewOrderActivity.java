package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.ViewOrderAdapter;
import com.restorapos.waiters.model.viewOrderModel.IteminfoItem;
import com.restorapos.waiters.model.viewOrderModel.ViewOrderResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class ViewOrderActivity extends AppCompatActivity {
    private String orderId, id, orderStatus;
    private TextView vat, total, grandTotal, discount, serviceChage, orderDate, table;
    private final String TAG = "ViewOrderActivity";
    private WaitersService waitersService;
    private RecyclerView foodCartRecyclerView;
    private ImageButton backbtn;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        init();

        backbtn.setOnClickListener(v -> {
            Intent intent=new Intent(ViewOrderActivity.this, MainActivity.class);
            intent.putExtra("PENDING","1");
            startActivity(intent);
            finishAffinity();
        });

        viewOrder();
    }

    private void init() {
        SharedPref.init(this);
        waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        foodCartRecyclerView = findViewById(R.id.foodCartRecyclerViewId);
        backbtn = findViewById(R.id.backId);
        vat = findViewById(R.id.vatId);
        total = findViewById(R.id.totalId);
        grandTotal = findViewById(R.id.grandTotalId);
        discount = findViewById(R.id.discountId);
        serviceChage = findViewById(R.id.serviceChargeId);
        orderDate = findViewById(R.id.OrderDateId);
        table = findViewById(R.id.tableId);
        orderId = getIntent().getStringExtra("ORDERID");
        id = SharedPref.read("ID", "");
        orderStatus = SharedPref.read("ORDERSTATUS", "");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent=new Intent(ViewOrderActivity.this,MainActivity.class);
        intent.putExtra("PENDING","1");
        startActivity(intent);
        finishAffinity();
    }

    private void viewOrder() {
        Log.d(TAG, "viewOrder: "+id+" "+orderStatus+" "+orderId);
        waitersService.viewOrder(id, orderStatus, orderId).enqueue(new Callback<ViewOrderResponse>() {
            @Override
            public void onResponse(Call<ViewOrderResponse> call, Response<ViewOrderResponse> response) {
                Log.d(TAG, "onResponse: " + new Gson().toJson(response.body()));
                try {
                    List<IteminfoItem> items = response.body().getData().getIteminfo();
                    foodCartRecyclerView.setAdapter(new ViewOrderAdapter(ViewOrderActivity.this, items));
                    vat.setText(SharedPref.read("CURRENCY","")+response.body().getData().getVAT());
                    total.setText(SharedPref.read("CURRENCY","")+response.body().getData().getSubtotal());
                    grandTotal.setText(SharedPref.read("CURRENCY","")+response.body().getData().getOrderTotal());
                    discount.setText(SharedPref.read("CURRENCY","")+response.body().getData().getDiscount());
                    serviceChage.setText(SharedPref.read("CURRENCY","")+response.body().getData().getServiceCharge());
                    orderDate.setText("Date: "+response.body().getData().getOrderdate());
                    table.setText("Table : " + response.body().getData().getTableName());
                } catch (Exception ignored) {/**/}
            }
            @Override
            public void onFailure(Call<ViewOrderResponse> call, Throwable t) {/**/}
        });
    }
}
