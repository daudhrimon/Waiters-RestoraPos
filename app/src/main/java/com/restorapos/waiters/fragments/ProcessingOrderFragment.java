package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.appSearchBar;
import static com.restorapos.waiters.MainActivity.rootMenu;
import static com.restorapos.waiters.fragments.OrderListFragment.orderSwipe;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.PendingOrderAdapter;
import com.restorapos.waiters.adapters.ViewOrderAdapter;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.pendingOrderModel.DataItem;
import com.restorapos.waiters.model.pendingOrderModel.PendingOrderResponse;
import com.restorapos.waiters.model.viewOrderModel.IteminfoItem;
import com.restorapos.waiters.model.viewOrderModel.ViewOrderResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingOrderFragment extends Fragment implements ViewInterface {
    private boolean search = false;
    private List<DataItem> items= new ArrayList<>();
    private RecyclerView processingOrderRecyclerView;
    private WaitersService waitersService;
    private String id;
    private LinearLayout layout;
    private SpotsDialog progressDialog;
    private RecyclerView foodCartRecyclerView;
    private TextView vat, total, grandTotal, discount, serviceChage, orderDate, table;
    private PendingOrderAdapter pendingOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_processing_order, container, false);

        innitial(view);

        getProcessingOrder();

        SharedPref.write("RED","3");

        appSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = true;
                if (search){
                    customfilterList(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search = true;
                if (search){
                    customfilterList(newText);
                }
                return false;
            }
        });

        orderSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getProcessingOrder();
            }
        });

        return view;
    }

    private void customfilterList(String value) {
        List<DataItem> newList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            newList.clear();
            if(items.size()!= 0){
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getOrderId().toLowerCase().startsWith(value.toLowerCase())) {
                        newList.add(items.get(i));
                    }
                }
                pendingOrderAdapter = new PendingOrderAdapter(getActivity().getApplicationContext(), newList,ProcessingOrderFragment.this::view);
                processingOrderRecyclerView.setAdapter(pendingOrderAdapter);

            }else {
                pendingOrderAdapter = new PendingOrderAdapter(getActivity().getApplicationContext(), items,ProcessingOrderFragment.this::view);
                processingOrderRecyclerView.setAdapter(pendingOrderAdapter);
            }
        }
    }
    private void getProcessingOrder() {
        waitersService.getProcessingOrder(id).enqueue(new Callback<PendingOrderResponse>() {
            @Override
            public void onResponse(Call<PendingOrderResponse> call, Response<PendingOrderResponse> response) {
                try {
                    if (response.body().getStatus().equals("success")) {
                        //Log.d("ppp", "onResponse: " + response.body().getMessage());
                        items = response.body().getData();

                        if (items.size() > 0){
                            layout.setVisibility(View.GONE);
                            processingOrderRecyclerView.setVisibility(View.VISIBLE);
                            pendingOrderAdapter = new PendingOrderAdapter(getActivity().getApplicationContext(), items,ProcessingOrderFragment.this::view);
                            processingOrderRecyclerView.setAdapter(pendingOrderAdapter);
                        } else {
                            processingOrderRecyclerView.setVisibility(View.GONE);
                            layout.setVisibility(View.VISIBLE);
                        }
                        orderSwipe.setRefreshing(false);
                        progressDialog.dismiss();
                    } else {
                        processingOrderRecyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        orderSwipe.setRefreshing(false);
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    processingOrderRecyclerView.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    orderSwipe.setRefreshing(false);
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<PendingOrderResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        orderSwipe.setRefreshing(false);
                        processingOrderRecyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }


    @Override
    public void view(String orderId) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.custom_alert_view, null);
        builder.setView(view2);
        ImageView close = view2.findViewById(R.id.closeId);
        foodCartRecyclerView = view2.findViewById(R.id.foodCartRecyclerViewId);
        vat = view2.findViewById(R.id.vatId);
        total = view2.findViewById(R.id.totalId);
        grandTotal = view2.findViewById(R.id.grandTotalId);
        discount = view2.findViewById(R.id.discountId);
        serviceChage = view2.findViewById(R.id.serviceChargeId);
        orderDate = view2.findViewById(R.id.OrderDateId);
        table = view2.findViewById(R.id.tableId);
        foodCartRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        viewOrder(orderId, SharedPref.read("ORDERSTATUS", ""));
        AlertDialog alert = builder.create();
        close.setOnClickListener(view -> alert.dismiss());
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        alert.setCancelable(false);
        alert.show();
        Log.d("asdd", "view: " + orderId);
    }


    private void viewOrder(String orderId, String orderStatus) {
        waitersService.viewOrder(id, orderStatus, orderId).enqueue(new Callback<ViewOrderResponse>() {
            @Override
            public void onResponse(Call<ViewOrderResponse> call, Response<ViewOrderResponse> response) {
                try {
                    List<IteminfoItem> items = response.body().getData().getIteminfo();
                    foodCartRecyclerView.setAdapter(new ViewOrderAdapter(getContext(), items,"Processing"));
                    vat.setText(response.body().getData().getVAT()+SharedPref.read("CURRENCY", ""));
                    total.setText(response.body().getData().getSubtotal()+SharedPref.read("CURRENCY", ""));
                    grandTotal.setText(response.body().getData().getOrderTotal()+SharedPref.read("CURRENCY", ""));
                    discount.setText(response.body().getData().getDiscount()+SharedPref.read("CURRENCY", ""));
                    serviceChage.setText(response.body().getData().getServiceCharge()+SharedPref.read("CURRENCY", ""));
                    orderDate.setText("Date: " + response.body().getData().getOrderdate());
                    table.setText("Table No: " + response.body().getData().getTableName());
                } catch (Exception ignored) {
                }
            }
            @Override
            public void onFailure(Call<ViewOrderResponse> call, Throwable t) {/**/}
        });
    }

    private void innitial(View view) {
        SharedPref.init(getContext());
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        id = SharedPref.read("ID", "");
        // Log.d("idcekk",""+new Gson().toJson(SharedPref.read("ID", "")));
        SharedPref.write("ORDERSTATUS", "2");
        processingOrderRecyclerView = view.findViewById(R.id.processingOrderRecyclerViewId);
        layout = view.findViewById(R.id.layoutId);
        rootMenu = true;
        appSearchBar.setInputType(InputType.TYPE_CLASS_PHONE);
        appSearchBar.setQueryHint("Search Here");
        progressDialog.show();
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hideKeyboard(getActivity());
        MainActivity.onResumeAppFrags();
    }
}
