package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.appSearchBar;
import static com.restorapos.waiters.MainActivity.btmNav;
import static com.restorapos.waiters.MainActivity.rootMenu;
import static com.restorapos.waiters.fragments.OrderFragment.orderSwipe;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.ViewOrderDialog;
import com.restorapos.waiters.adapters.PendingOrderAdapter;
import com.restorapos.waiters.databinding.FragmentPendingBinding;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.pendingOrderModel.DataItem;
import com.restorapos.waiters.model.pendingOrderModel.PendingOrderResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PendingFragment extends Fragment implements ViewInterface {
    private FragmentPendingBinding binding;
    private boolean search = false;
    private List<DataItem> items = new ArrayList<>();
    private WaitersService waitersService;
    private String id;
    private SpotsDialog progressDialog;
    private PendingOrderAdapter pendingOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentPendingBinding.inflate(inflater, container, false);


        btmNav.setVisibility(View.VISIBLE);
        SharedPref.init(getContext());
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        id = SharedPref.read("ID", "");
        //SharedPref.write("ORDERSTATUS", "1");
        rootMenu = true;
        appSearchBar.setInputType(InputType.TYPE_CLASS_PHONE);
        appSearchBar.setQueryHint("Search Here");
        progressDialog.show();


        getPendingOrder();

        //orderClear();
        SharedPref.write("RED","3");


        appSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = true;
                if (search){
                    customFilterList(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search = true;
                if (search){
                    customFilterList(newText);
                }
                return false;
            }
        });



        orderSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getPendingOrder();
            }
        });



        return binding.getRoot();
    }



    private void getPendingOrder() {
        waitersService.getPendingOrder(id).enqueue(new Callback<PendingOrderResponse>() {
            @Override
            public void onResponse(Call<PendingOrderResponse> call, Response<PendingOrderResponse> response) {
                try {
                    if (response.body().getStatus().equals("success")) {
                        Log.d("ppp", "onResponse: " + new Gson().toJson(response.body()));
                        items = response.body().getData();
                        if (items.size() > 0){
                            binding.emptyLay.setVisibility(View.GONE);
                            binding.pendingRecycler.setVisibility(View.VISIBLE);
                            pendingOrderAdapter = new PendingOrderAdapter(getActivity().getApplicationContext(), items, PendingFragment.this::viewOrder);
                            binding.pendingRecycler.setAdapter(pendingOrderAdapter);
                        } else {
                            binding.pendingRecycler.setVisibility(View.GONE);
                            binding.emptyLay.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.pendingRecycler.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                    }
                    orderSwipe.setRefreshing(false);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    orderSwipe.setRefreshing(false);
                    binding.pendingRecycler.setVisibility(View.GONE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<PendingOrderResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        orderSwipe.setRefreshing(false);
                        binding.pendingRecycler.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }




    @Override
    public void viewOrder(String orderId) {
        Dialog dialog = new ViewOrderDialog(getContext(),id,orderId,"1","Pending");
        dialog.show();
        Window win = dialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        win.setLayout((14*width)/15,(19*height)/20);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }




    private void customFilterList(String value) {
        List<DataItem> newList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            newList.clear();
            for (int i = 0; i < items.size(); i++) {
                if ((items.get(i).getOrderId().toLowerCase().startsWith(value.toLowerCase())) || items.get(i).getTableName().toLowerCase().startsWith(value.toLowerCase())) {
                    newList.add(items.get(i));
                }
            }
            pendingOrderAdapter = new PendingOrderAdapter(getActivity().getApplicationContext(), newList, PendingFragment.this::viewOrder);
            binding.pendingRecycler.setAdapter(pendingOrderAdapter);

        }else {
            pendingOrderAdapter = new PendingOrderAdapter(getActivity().getApplicationContext(), items, PendingFragment.this::viewOrder);
            binding.pendingRecycler.setAdapter(pendingOrderAdapter);
        }

    }



    @Override
    public void onResume() {
        super.onResume();
        MainActivity.onResumeAppFrags();
    }
}
