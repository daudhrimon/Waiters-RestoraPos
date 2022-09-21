package com.restorapos.waiters.activities;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.restorapos.waiters.adapters.ViewOrderAdapter;
import com.restorapos.waiters.databinding.CustomAlertViewBinding;
import com.restorapos.waiters.model.viewOrderModel.IteminfoItem;
import com.restorapos.waiters.model.viewOrderModel.ViewOrderResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewOrderDialog extends Dialog {
    private CustomAlertViewBinding binding;
    private WaitersService waitersService;
    private String waiterId;
    private String orderId;
    private String currency;
    private String orderStatus;
    private String orderTag;

    public ViewOrderDialog(Context context, String waiterId, String orderId, String orderStatus, String orderTag) {
        super(context);
        this.waiterId = waiterId;
        this.orderId = orderId;
        this.orderStatus = orderStatus;
        this.orderTag = orderTag;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = CustomAlertViewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        SharedPref.init(getContext());

        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        currency = SharedPref.read("CURRENCY", "");

        binding.crossBtn.setOnClickListener(view -> dismiss());


        Log.wtf("ViewOrder Info: ","WaiterId: "+waiterId+" ,OrderStatus: "+orderStatus+" ,Order Id: "+orderId);


        waitersService.viewOrder(waiterId,orderStatus, orderId).enqueue(new Callback<ViewOrderResponse>() {
            @Override
            public void onResponse(Call<ViewOrderResponse> call, Response<ViewOrderResponse> response) {
                Log.d("TAG", "onResponse: " + new Gson().toJson(response.body()));
                try {
                    List<IteminfoItem> items = response.body().getData().getIteminfo();
                    if (items.size() > 0){
                        binding.foodCartRecycler.setVisibility(View.VISIBLE);
                        binding.foodCartRecycler.setAdapter(new ViewOrderAdapter(getContext(), items,orderTag));
                    }
                    binding.vatTv.setText(response.body().getData().getVAT()+currency);
                    binding.totalTv.setText(response.body().getData().getSubtotal()+currency);
                    binding.grandTotalTv.setText(response.body().getData().getOrderTotal()+currency);
                    binding.discountTv.setText(response.body().getData().getDiscount()+currency);
                    binding.serviceChargeTv.setText(response.body().getData().getServiceCharge()+currency);
                    binding.orderDateTv.setText("Date: " + response.body().getData().getOrderdate());
                    binding.tableTv.setText("Table No: " + response.body().getData().getTableName());
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<ViewOrderResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getLocalizedMessage());
            }
        });
    }
}
