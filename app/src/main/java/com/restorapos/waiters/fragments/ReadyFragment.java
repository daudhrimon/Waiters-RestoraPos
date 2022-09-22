package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.appSearchBar;
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
import com.restorapos.waiters.adapters.ReadyOrderAdapter;
import com.restorapos.waiters.databinding.FragmentReadyBinding;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.Readyorder.ReadyOrderResponse;
import com.restorapos.waiters.model.Readyorder.ReadyorderData;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReadyFragment extends Fragment implements ViewInterface {
    private FragmentReadyBinding binding;
    private boolean search = false;
    private WaitersService waitersService;
    private String id;
    private int start = 0;
    private SpotsDialog progressDialog;
    private List<ReadyorderData> items = new ArrayList<>();
    private ReadyOrderAdapter readyOrderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentReadyBinding.inflate(inflater, container, false);

        initial();


        getAllReadyOrder(start);


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
                getAllReadyOrder(start);
            }
        });

        binding.nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start += 10;
                Log.d("TAG", "onClick: " + start);
                getAllReadyOrder(start);
                if (0 < start) {
                    binding.prevBtn.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                start -= 10;
                getAllReadyOrder(start);
                if (start < 10) {
                    binding.prevBtn.setVisibility(View.GONE);
                }
            }
        });




        return binding.getRoot();
    }


    private void customfilterList(String value) {
        List<ReadyorderData> newList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            newList.clear();
            if(items.size()!= 0){
                for (int i = 0; i < items.size(); i++) {
                    if (items.get(i).getOrderId().toLowerCase().startsWith(value.toLowerCase())) {
                        newList.add(items.get(i));
                    }
                }
                readyOrderAdapter = new ReadyOrderAdapter(getActivity().getApplicationContext(), newList, ReadyFragment.this::viewOrder);
                binding.readyRecycler.setAdapter(readyOrderAdapter);

            }else {
                readyOrderAdapter = new ReadyOrderAdapter(getActivity().getApplicationContext(), items, ReadyFragment.this::viewOrder);
                binding.readyRecycler.setAdapter(readyOrderAdapter);
            }
        }

    }

    private void getAllReadyOrder(final int start){
        waitersService.getAllReadyOrder(id,start).enqueue(new Callback<ReadyOrderResponse>() {
            @Override
            public void onResponse(Call<ReadyOrderResponse> call, Response<ReadyOrderResponse> response) {
//                Log.d("ppp", "onResponse: " + response.body().getReadyOrderOtherInfo().getTotalorder());
                int starts = start + 10;
                if (response.body().getStatus().equals("success")){
                    if(response.body().getReadyOrderOtherInfo().getTotalorder()!=null){
                        if (starts > response.body().getReadyOrderOtherInfo().getTotalorder()) {
                            binding.nextBtn.setVisibility(View.GONE);
                        } else {
                            binding.nextBtn.setVisibility(View.VISIBLE);
                        }
                    }
                    items = response.body().getReadyOrderOtherInfo().getReadyOrderData();
                    if (items.size() > 0){
                        binding.emptyLay.setVisibility(View.GONE);
                        binding.readyRecycler.setVisibility(View.VISIBLE);
                        readyOrderAdapter = new ReadyOrderAdapter(getActivity().getApplicationContext(), items, ReadyFragment.this::viewOrder);
                        binding.readyRecycler.setAdapter(readyOrderAdapter);
                    } else {
                        binding.readyRecycler.setVisibility(View.GONE);
                        binding.nextBtn.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                    }
                } else {
                    binding.readyRecycler.setVisibility(View.GONE);
                    binding.nextBtn.setVisibility(View.GONE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                }
                orderSwipe.setRefreshing(false);
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ReadyOrderResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.readyRecycler.setVisibility(View.GONE);
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
        Dialog dialog = new ViewOrderDialog(getContext(),id,orderId,"3","Ready");
        dialog.show();
        Window win = dialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        int height = getResources().getDisplayMetrics().heightPixels;
        win.setLayout((14*width)/15,(19*height)/20);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }





    private void initial() {
        SharedPref.init(getContext());
        progressDialog = new SpotsDialog(getActivity(), R.style.Custom);
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        id = SharedPref.read("ID", "");
       // SharedPref.write("ORDERSTATUS", "3");
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