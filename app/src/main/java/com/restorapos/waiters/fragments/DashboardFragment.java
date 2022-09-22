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
import com.restorapos.waiters.databinding.FragmentDashboardBinding;
import com.restorapos.waiters.model.dashboardModel.DashboardDatum;
import com.restorapos.waiters.model.dashboardModel.DashboardResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardFragment extends Fragment {
    private FragmentDashboardBinding binding;
    private boolean search = false;
    private WaitersService waitersService;
    private String id;
    private SpotsDialog progressDialog;
    private List<DashboardDatum> dbItems;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);



        initial();

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

        binding.dashSwipe.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDashboardItem();
            }
        });



        return binding.getRoot();
    }




    private void initial() {
        btmNav.setVisibility(View.VISIBLE);
        btmNav.setSelectedItemId(R.id.bMenu);
        SharedPref.init(getActivity());
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        dbItems = new ArrayList<>();
        id = SharedPref.read("ID", "");
        rootMenu = false;
        appSearchBar.setInputType(InputType.TYPE_CLASS_TEXT);
        appSearchBar.setQueryHint("Search Food Category");
        progressDialog = new SpotsDialog(getContext(), R.style.Custom);
        progressDialog.show();
    }




    private void getDashboardItem() {
        waitersService.getDashboardItem(id).enqueue(new Callback<DashboardResponse>() {
            @Override
            public void onResponse(Call<DashboardResponse> call, Response<DashboardResponse> response) {
                binding.swipeAlert.setVisibility(View.GONE);
                try {
                    dbItems.clear();
                    dbItems = response.body().getData();
                    if (dbItems.size() > 0){
                        binding.swipeAlert.setVisibility(View.GONE);
                        binding.dashboardRecycler.setVisibility(View.VISIBLE);
                        binding.dashboardRecycler.setAdapter(new DashboardAdapter(getActivity().getApplicationContext(), dbItems));
                    } else {
                        binding.dashboardRecycler.setVisibility(View.GONE);
                        binding.swipeAlert.setVisibility(View.VISIBLE);
                    }
                    binding.dashSwipe.setRefreshing(false);
                    progressDialog.dismiss();
                } catch (Exception e) {
                    binding.dashSwipe.setRefreshing(false);
                    binding.dashboardRecycler.setVisibility(View.GONE);
                    binding.swipeAlert.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }
            @Override
            public void onFailure(Call<DashboardResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.dashSwipe.setRefreshing(false);
                        binding.dashboardRecycler.setVisibility(View.GONE);
                        binding.swipeAlert.setVisibility(View.VISIBLE);
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
            binding.dashboardRecycler.setBackgroundColor(Color.parseColor("#ECF0F1"));
            binding.dashboardRecycler.setAdapter(new DashboardAdapter(getActivity().getApplicationContext(), tempDbItems));
        } else {
            binding.dashboardRecycler.setBackgroundColor(getResources().getColor(R.color.colorWhite));
            binding.dashboardRecycler.setAdapter(new DashboardAdapter(getActivity().getApplicationContext(), dbItems));
        }
    }




    @Override
    public void onResume() {
        super.onResume();
        MainActivity.onResumeAppFrags();
    }
}
