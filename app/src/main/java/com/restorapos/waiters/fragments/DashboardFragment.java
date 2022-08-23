package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.appSearchBar;
import static com.restorapos.waiters.MainActivity.btmNav;
import static com.restorapos.waiters.MainActivity.rootMenu;
import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.DashboardAdapter;
import com.restorapos.waiters.model.dashboardModel.DashboardDatum;
import com.restorapos.waiters.model.dashboardModel.DashboardResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private boolean search = false;
    private RecyclerView dashboardRecyclerView;
    private WaitersService waitersService;
    private String id;
    private SpotsDialog progressDialog;
    private List<DashboardDatum> dbItems;
    private SwipeRefreshLayout dashSwipe;
    private ImageView swipeAlert;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);

        initial(view);

        getDashboardItem();

        appSearchBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search = true;
                if (search){
                    SearchDashboardItem(query);
                }
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                search = true;
                if (search){
                    SearchDashboardItem(newText);
                }
                return false;
            }
        });

        dashSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDashboardItem();
            }
        });

        return view;
    }

    private void initial(View view) {
        btmNav.setVisibility(View.VISIBLE);
        SharedPref.init(getActivity());
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        dashboardRecyclerView = view.findViewById(R.id.dashboardRecyclerViewId);
        dashSwipe = view.findViewById(R.id.dashSwipe);
        dbItems = new ArrayList<>();
        swipeAlert = view.findViewById(R.id.swipeAlert);
        id = SharedPref.read("ID", "");
        rootMenu = false;
        appSearchBar.setInputType(InputType.TYPE_CLASS_TEXT);
        appSearchBar.setQueryHint("Search Food Category");
        progressDialog.show();
    }

    private void getDashboardItem() {
        waitersService.getDashboardItem(id).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                swipeAlert.setVisibility(View.GONE);
                try {
                    dbItems.clear();
                    dbItems = response.body().getData();
                    if (dbItems.size() > 0){
                        swipeAlert.setVisibility(View.GONE);
                        dashboardRecyclerView.setVisibility(View.VISIBLE);
                        dashboardRecyclerView.setAdapter(new DashboardAdapter(getActivity().getApplicationContext(), dbItems));
                    } else {
                        dashboardRecyclerView.setVisibility(View.GONE);
                        swipeAlert.setVisibility(View.VISIBLE);
                    }
                    dashSwipe.setRefreshing(false);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    dashSwipe.setRefreshing(false);
                    dashboardRecyclerView.setVisibility(View.GONE);
                    swipeAlert.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dashSwipe.setRefreshing(false);
                        dashboardRecyclerView.setVisibility(View.GONE);
                        swipeAlert.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }

    private void SearchDashboardItem(String query) {
        if (query != null && !query.isEmpty()) {
            List<DashboardDatum> tempDbItems = new ArrayList<>();
            for (int i = 0; i < dbItems.size(); i++) {
                if (dbItems.get(i).getName().toLowerCase(Locale.ROOT).contains(query.toLowerCase(Locale.ROOT))) {
                    tempDbItems.add(dbItems.get(i));
                }
            }
            dashboardRecyclerView.setBackgroundColor(Color.parseColor("#ECF0F1"));
            dashboardRecyclerView.setAdapter(new DashboardAdapter(getActivity().getApplicationContext(), tempDbItems));
        } else {
            dashboardRecyclerView.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            dashboardRecyclerView.setAdapter(new DashboardAdapter(getActivity().getApplicationContext(), dbItems));
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Utils.hideKeyboard(getActivity());
        MainActivity.onResumeAppFrags();
    }
}
