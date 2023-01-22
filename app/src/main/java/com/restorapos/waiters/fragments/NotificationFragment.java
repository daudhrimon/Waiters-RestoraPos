package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.btmNav;
import static com.restorapos.waiters.MainActivity.rootMenu;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.NotificationAdapter;
import com.restorapos.waiters.adapters.NotificationItemAdapter;
import com.restorapos.waiters.databinding.DialogViewNotificationBinding;
import com.restorapos.waiters.databinding.FragmentNotificationBinding;
import com.restorapos.waiters.model.notificationModel.NotificationResponse;
import com.restorapos.waiters.model.notificationModel.OrderAcceptResponse;
import com.restorapos.waiters.model.notificationModel.OrderinfoItem;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.interfaces.NotificationInterface;
import com.restorapos.waiters.utils.SharedPref;

import java.util.List;

import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationFragment extends Fragment implements NotificationInterface {
    private FragmentNotificationBinding binding;
    private NotificationInterface notificationInterface;
    private String waiterId;
    private WaitersService waitersService;
    private SpotsDialog progressDialog;
    private String currency = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);


        btmNav.setVisibility(View.VISIBLE);
        SharedPref.init(getContext());
        setHasOptionsMenu(true);
        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        notificationInterface = this;
        waiterId = SharedPref.read("ID", "");
        rootMenu = true;
        currency = SharedPref.read("CURRENCY", "");
        progressDialog.show();


        getAllOnlineOrder();


        binding.notySwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getAllOnlineOrder();
            }
        });


        return binding.getRoot();
    }


    @Override
    public void acceptOrder(String orderId, Dialog dialog) {
        Log.wtf("Waiter ID", "WaiterId: " + waiterId);
        Log.wtf("Waiter ID", "OrderID: " + orderId);
        waitersService.acceptOrder(waiterId, orderId).enqueue(new Callback<OrderAcceptResponse>() {
            @Override
            public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                try {
                    if (response.body().getStatusCode() == 1) {
                        Toasty.success(getContext(), "Order Received Successfully", Toast.LENGTH_SHORT).show();
                    } else {
                        Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                    if (dialog != null) {
                        dialog.dismiss();
                    }
                    getAllOnlineOrder();

                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
                Log.wtf("poiuy", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    @Override
    public void viewOrder(OrderinfoItem orderInfo) {
        Dialog dialog = new Dialog(getContext());
        DialogViewNotificationBinding dBind = DialogViewNotificationBinding.inflate(getLayoutInflater());
        dialog.setContentView(dBind.getRoot());

        dBind.crossBtn.setOnClickListener(view -> {
            dialog.dismiss();
        });

        if (orderInfo.getIteminfo().size() > 0) {
            dBind.foodCartRecycler.setVisibility(View.VISIBLE);
            dBind.foodCartRecycler.setAdapter(new NotificationItemAdapter(orderInfo.getIteminfo(), currency));
        }

        dBind.acceptBtn.setOnClickListener(view -> {
            NotificationInterface notificationInterface = this;
            notificationInterface.acceptOrder(orderInfo.getOrderid(), dialog);
        });


        dialog.show();
        Window win = dialog.getWindow();
        int width = getResources().getDisplayMetrics().widthPixels;
        win.setLayout((6 * width) / 7, WindowManager.LayoutParams.WRAP_CONTENT);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Log.wtf("OrderInfo", new Gson().toJson(orderInfo));
    }


    private void getAllOnlineOrder() {
        Log.wtf("Waiter ID", "WaiterId: " + waiterId);
        waitersService.allOnlineOrder(waiterId).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                try {
                    if (response.body().getStatusCode() == 1) {
                        List<OrderinfoItem> list = response.body().getData().getOrderinfo();
                        if (list.size() > 0) {
                            binding.emptyLay.setVisibility(View.GONE);
                            binding.notifyRecycler.setVisibility(View.VISIBLE);
                            binding.notifyRecycler.setAdapter(new NotificationAdapter(list, notificationInterface));
                            MainActivity.notifyBadge.setVisible(true);
                            MainActivity.notifyBadge.setNumber(list.size());
                        } else {
                            binding.emptyLay.setVisibility(View.VISIBLE);
                            binding.notifyRecycler.setVisibility(View.GONE);
                            MainActivity.notifyBadge.setVisible(false);
                        }
                        binding.notySwipe.setRefreshing(false);
                        progressDialog.dismiss();
                    } else {
                        binding.emptyLay.setVisibility(View.VISIBLE);
                        binding.notifyRecycler.setVisibility(View.GONE);
                        binding.notySwipe.setRefreshing(false);
                        progressDialog.dismiss();
                        MainActivity.notifyBadge.setVisible(false);
                    }
                } catch (Exception e) {
                    binding.emptyLay.setVisibility(View.VISIBLE);
                    binding.notifyRecycler.setVisibility(View.GONE);
                    binding.notySwipe.setRefreshing(false);
                    progressDialog.dismiss();
                    MainActivity.notifyBadge.setVisible(false);
                }
            }

            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.emptyLay.setVisibility(View.VISIBLE);
                        binding.notifyRecycler.setVisibility(View.GONE);
                        binding.notySwipe.setRefreshing(false);
                        progressDialog.dismiss();
                        MainActivity.notifyBadge.setVisible(false);
                    }
                }, 269);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
        MainActivity.onResumeAppFrags();
    }
}
