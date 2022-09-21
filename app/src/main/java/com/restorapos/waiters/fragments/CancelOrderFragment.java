package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.appSearchBar;
import static com.restorapos.waiters.MainActivity.rootMenu;
import static com.restorapos.waiters.fragments.OrderListFragment.orderSwipe;

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
import com.restorapos.waiters.adapters.CompleteCancelOrderAdapter;
import com.restorapos.waiters.databinding.FragmentCancelOrderBinding;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.completeCancelOrder.CompleteCancelResponse;
import com.restorapos.waiters.model.completeCancelOrder.OrderinfoItem;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CancelOrderFragment extends Fragment implements ViewInterface {
    private FragmentCancelOrderBinding binding;
    private boolean search = false;
    private List<OrderinfoItem> items = new ArrayList<>();
    private CompleteCancelOrderAdapter completeCancelOrderAdapter ;
    private WaitersService waitersService;
    private String id;
    private int start = 0;
    private SpotsDialog progressDialog;
    private final String ORDER_STATUS = "5";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCancelOrderBinding.inflate(inflater, container, false);


        initial();

        getCancelOrder(start);


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
                getCancelOrder(start);
            }
        });



        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start += 10;
                getCancelOrder(start);
                if (0 < start) {
                    binding.prevBtn.setVisibility(View.VISIBLE);
                }
            }
        });




        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start -= 10;
                getCancelOrder(start);
                if (start < 10) {
                    binding.prevBtn.setVisibility(View.GONE);
                }
            }
        });



        return binding.getRoot();
    }

    private void customFilterList(String value) {
        List<OrderinfoItem> newList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            newList.clear();
            if(items.size()!= 0){
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getOrderId().toLowerCase().startsWith(value.toLowerCase())) {
                        newList.add(items.get(i));
                    }
                }
                completeCancelOrderAdapter = new CompleteCancelOrderAdapter(getActivity().getApplicationContext(), newList,CancelOrderFragment.this::viewOrder);
                binding.processingRecycler.setAdapter(completeCancelOrderAdapter);

            }else {
                completeCancelOrderAdapter = new CompleteCancelOrderAdapter(getActivity().getApplicationContext(), items,CancelOrderFragment.this::viewOrder);
                binding.processingRecycler.setAdapter(completeCancelOrderAdapter);
            }
        }

    }


    private void getCancelOrder(int start) {
        waitersService.getCancelOrder(id, start).enqueue(new Callback<CompleteCancelResponse>() {
            @Override
            public void onResponse(Call<CompleteCancelResponse> call, Response<CompleteCancelResponse> response) {
                // Log.d("ppp", "onResponse: " + response.body().getStatus());
                try {
                    if (response.body().getStatus().equals("success")) {
                        Log.d("ppp", "onResponse: " + new Gson().toJson(response.body()));
                        int starts = start + 10;
                        if (starts > response.body().getData().getTotalorder()) {
                            binding.nextBtn.setVisibility(View.GONE);
                        } else {
                            binding.nextBtn.setVisibility(View.VISIBLE);
                        }
                        items = response.body().getData().getOrderinfo();
                        if (items.size() > 0){
                            binding.emptyLay.setVisibility(View.GONE);
                            binding.processingRecycler.setVisibility(View.VISIBLE);
                            completeCancelOrderAdapter = new CompleteCancelOrderAdapter(getActivity().getApplicationContext(), items,CancelOrderFragment.this::viewOrder);
                            binding.processingRecycler.setAdapter(completeCancelOrderAdapter);
                            binding.nextBtn.setVisibility(View.VISIBLE);
                        } else {
                            binding.processingRecycler.setVisibility(View.GONE);
                            binding.nextBtn.setVisibility(View.GONE);
                            binding.emptyLay.setVisibility(View.VISIBLE);
                        }
                    } else {
                        binding.processingRecycler.setVisibility(View.GONE);
                        binding.nextBtn.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                    }
                    orderSwipe.setRefreshing(false);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    binding.processingRecycler.setVisibility(View.GONE);
                    binding.nextBtn.setVisibility(View.GONE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                    orderSwipe.setRefreshing(false);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<CompleteCancelResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.processingRecycler.setVisibility(View.GONE);
                        binding.nextBtn.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                        orderSwipe.setRefreshing(false);
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }




    @Override
    public void viewOrder(String orderId) {
        Dialog dialog = new ViewOrderDialog(getContext(),id,orderId,"5","Canceled");
        dialog.show();
        Window win = dialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        win.setLayout((14*width)/15,(19*height)/20);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }




    private void initial() {
        SharedPref.init(getContext());
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        id = SharedPref.read("ID", "");
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        //SharedPref.write("ORDERSTATUS", "5");
        rootMenu = true;
        appSearchBar.setInputType(InputType.TYPE_CLASS_PHONE);
        appSearchBar.setQueryHint("Search Here");
        progressDialog.show();
    }




    @Override
    public void onResume() {
        super.onResume();
        MainActivity.onResumeAppFrags();
    }
}
