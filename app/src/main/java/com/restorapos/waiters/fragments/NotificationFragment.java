package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.btmNav;
import static com.restorapos.waiters.MainActivity.completeOrder;
import static com.restorapos.waiters.MainActivity.logout;
import static com.restorapos.waiters.MainActivity.orderHistory;
import static com.restorapos.waiters.MainActivity.orderList;
import static com.restorapos.waiters.MainActivity.menu;
import static com.restorapos.waiters.MainActivity.rootMenu;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.NotificationAdapter;
import com.restorapos.waiters.databinding.FragmentNotificationBinding;
import com.restorapos.waiters.model.notificationModel.NotificationResponse;
import com.restorapos.waiters.model.notificationModel.OrderAcceptResponse;
import com.restorapos.waiters.model.notificationModel.OrderinfoItem;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.NotificationInterface;
import com.restorapos.waiters.utils.SharedPref;

import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNotificationBinding.inflate(inflater, container, false);



        initial();

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
    public void acceptOrder(String orderId) {
        Log.d("poiuy", "acceptOrder: " + waiterId);
        waitersService.acceptOrder(waiterId, orderId).enqueue(new Callback<OrderAcceptResponse>() {
            @Override
            public void onResponse(Call<OrderAcceptResponse> call, Response<OrderAcceptResponse> response) {
                try {
                    if (response.body().getStatusCode() == 1) {
                        Toasty.success(getContext(), "Order Received Successfully", Toast.LENGTH_SHORT).show();
                        getAllOnlineOrder();
                    } else {
                        Toasty.error(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        getAllOnlineOrder();
                    }
                } catch (Exception ignored) {
                }
            }

            @Override
            public void onFailure(Call<OrderAcceptResponse> call, Throwable t) {
                Log.d("poiuy", "onFailure: " + t.getLocalizedMessage());
            }
        });
    }


    private void getAllOnlineOrder() {
        waitersService.allOnlineOrder(waiterId).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                try {
                    if (response.body().getStatusCode() == 1) {
                        List<OrderinfoItem> list = response.body().getData().getOrderinfo();
                        if (list.size() > 0){
                            binding.emptyLay.setVisibility(View.GONE);
                            binding.notifyRecycler.setVisibility(View.VISIBLE);
                            binding.notifyRecycler.setAdapter(new NotificationAdapter(getContext(), list, notificationInterface));
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

    /*@Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }*/



    private void initial() {
        btmNav.setVisibility(View.VISIBLE);
       /* menu.setBackgroundColor(0x00000000);
        orderList.setBackgroundColor(0x00000000);
        completeOrder.setBackgroundColor(0x00000000);
        orderHistory.setBackgroundColor(0x00000000);
        logout.setBackgroundColor(0x00000000);*/
        SharedPref.init(getContext());
        setHasOptionsMenu(true);
        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        notificationInterface = this;
        waiterId = SharedPref.read("ID", "");
        rootMenu = true;
        progressDialog.show();
    }



    @Override
    public void onResume() {
        super.onResume();
        MainActivity.onResumeAppFrags();
    }
}
