package com.restorapos.waiters;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.restorapos.waiters.activities.CartActivity;
import com.restorapos.waiters.activities.LoginActivity;
import com.restorapos.waiters.fragments.CompleteFragment;
import com.restorapos.waiters.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.fragments.DashboardFragment;
import com.restorapos.waiters.fragments.NotificationFragment;
import com.restorapos.waiters.fragments.HistoryFragment;
import com.restorapos.waiters.fragments.OrderFragment;
import com.restorapos.waiters.model.loginModel.LoginResponse;
import com.restorapos.waiters.model.notificationModel.NotificationResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    //btmNav
    public static BottomNavigationView btmNav;
    public static BadgeDrawable notifyBadge;
    //drawer
    private DrawerLayout drawer;
    private View headerView;
    public static TextView menu;
    public static TextView orderList;
    public static TextView completeOrder;
    public static TextView orderHistory;
    public static TextView logout;
    //appBar
    private ImageView appMenu;
    public static TextView appHeader;
    public static SearchView appSearchBar;
    private ImageView appSearch;
    private ImageView appCart;
    public static TextView appCartBadge;
    public static LinearLayout appSearchLay,appBtnLay;
    private ImageView appCross;
    public static Boolean rootMenu;
    private boolean go = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        SharedPref.init(this);
        SharedPref.write("FOOD", "");
        SharedPref.write("OVI", "");
        //btmNav
        btmNav = findViewById(R.id.btmNav);
        notifyBadge = btmNav.getOrCreateBadge(R.id.bNotify);
        notifyBadge.setVisible(false);
        drawer = findViewById(R.id.drawer_layout);
        //appBar initial
        appMenu = findViewById(R.id.appMenu);
        appHeader = findViewById(R.id.appHeader);
        appSearchBar = findViewById(R.id.appSearchBar);
        appCart = findViewById(R.id.appCart);
        appBtnLay = findViewById(R.id.appBtnLay);
        appSearchLay = findViewById(R.id.appSearchLay);
        appSearch = findViewById(R.id.appSearch);
        appCross = findViewById(R.id.appCross);
        appCartBadge = findViewById(R.id.appCartBadge);
        //Drawer initial
        NavigationView navigationView = findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        menu = headerView.findViewById(R.id.menuId);
        menu.setBackgroundColor(0x30ffffff);
        logout = headerView.findViewById(R.id.logoutId);
        orderList = headerView.findViewById(R.id.orderlistId);
        completeOrder = headerView.findViewById(R.id.completeOrderlistid);
        orderHistory = headerView.findViewById(R.id.orderhistoryId);
        rootMenu = false;


        // AppBars OnClicks

        appMenu.setOnClickListener(v -> {
            if (drawer.isDrawerVisible(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {
                drawer.openDrawer(GravityCompat.START);
            }
        });



        appCart.setOnClickListener(view -> {
            startActivity(new Intent(MainActivity.this.getApplicationContext(), CartActivity.class));
        });



        appSearch.setOnClickListener(view -> {
            if (appHeader.getVisibility() == View.VISIBLE){
                appHeader.setVisibility(View.GONE);
                appBtnLay.setVisibility(View.GONE);
                appSearchLay.setVisibility(View.VISIBLE);
                Utils.showKeyboard(this);
                appSearchBar.requestFocus();
            }
        });



        appCross.setOnClickListener(view -> {
            appSearchLay.setVisibility(View.GONE);
            appHeader.setVisibility(View.VISIBLE);
            appBtnLay.setVisibility(View.VISIBLE);
            Utils.hideKeyboard(this);
        });



        // Load Fragments

        DashboardFragment dashboardFragment = new DashboardFragment();
        try {
            if (getIntent().getStringExtra("OVI").equals("TSET")) {
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
                appBarDefault();
                appHeader.setText("Customer Orders");
            } else if (getIntent().getStringExtra("OVI").equals("admin")) {
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.content_main, new OrderFragment(), "ovi")
                        .commit();
                appBarDefault();
            } /*else {
                SharedPref.write("ORDERSTATUS", "2");
                Intent intent = new Intent(MainActivity.this, ViewOrderActivity.class);
                intent.putExtra("ORDERID", getIntent().getStringExtra("OVI"));
                startActivity(intent);
                appBarDefault();
            }*/
        } catch (Exception e) {
            try {
                if (getIntent().getStringExtra("PENDING").equals("1")) {
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, new OrderFragment(), "ovi")
                            .commit();
                    appBarDefault();
                } else {
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, dashboardFragment, dashboardFragment.getTag())
                            .commit();
                    appBarDefault();
                    appHeader.setText("Menu");
                }
            } catch (Exception el) {
                // initial Open
                try {
                    getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, dashboardFragment, dashboardFragment.getTag())
                            .commit();
                    appBarDefault();
                    appHeader.setText("Menu");
                } catch (Exception ig) {}
            }
        }




        //bottom Nav OnClicks

        btmNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                if (item.getItemId() != btmNav.getSelectedItemId() && go){

                    switch (item.getItemId()) {


                        case R.id.bMenu: {
                            DashboardFragment dashboardFragment = new DashboardFragment();
                            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, dashboardFragment, dashboardFragment.getTag())
                                    .commit();
                            drawer.closeDrawer(GravityCompat.START);
                            setSelectorColor(menu, orderList, completeOrder, orderHistory, logout);
                            appBarDefault();
                            appHeader.setText("Menu");
                            appSearch.setVisibility(View.VISIBLE);
                        }
                        break;


                        case R.id.bOrders: {
                            OrderFragment orderFragment = new OrderFragment();
                            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, orderFragment, orderFragment.getTag())
                                    .commit();
                            drawer.closeDrawer(GravityCompat.START);
                            setSelectorColor(orderList, menu, completeOrder, orderHistory, logout);
                            appSearch.setVisibility(View.VISIBLE);
                        }
                        break;


                        case R.id.bProcess: {
                            if (SharedPref.read("OVI", "").equals("admin")) {
                                SharedPref.write("RED", "1");
                            } else {
                                SharedPref.write("RED", "2");
                            }
                            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, new OrderFragment(), "ovi").commit();
                            appSearch.setVisibility(View.VISIBLE);
                        }
                        break;


                        case R.id.bCmplt: {
                            CompleteFragment completeFragment = new CompleteFragment();
                            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, completeFragment, completeFragment.getTag())
                                    .commit();
                            drawer.closeDrawer(GravityCompat.START);
                            setSelectorColor(completeOrder, menu, orderList, orderHistory, logout);
                            appBarDefault();
                            appHeader.setText("Complete Orders");
                            appSearch.setVisibility(View.VISIBLE);
                        }
                        break;


                        case R.id.bNotify: {
                            getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
                            appBarDefault();
                            appHeader.setText("Customer Orders");
                            appSearch.setVisibility(View.GONE);
                        }
                        break;

                    }
                }

                return true;
            }
        });




        // drawer On Clicks

        menu.setOnClickListener(v -> {
            go = true;
            btmNav.setSelectedItemId(R.id.bMenu);
        });



        orderList.setOnClickListener(v -> {
            go = true;
            btmNav.setSelectedItemId(R.id.bOrders);
        });



        completeOrder.setOnClickListener(v -> {
            go = true;
            btmNav.setSelectedItemId(R.id.bCmplt);
        });



        orderHistory.setOnClickListener(v -> {
            if (go){
                HistoryFragment historyFragment = new HistoryFragment();
                getSupportFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.content_main, historyFragment, historyFragment.getTag())
                        .commit();
                drawer.closeDrawer(GravityCompat.START);
                setSelectorColor(orderHistory, menu, orderList, completeOrder, logout);
                go = false;
                appHeader.setText("Order History");
                appSearch.setVisibility(View.GONE);
                btmNav.setSelectedItemId(R.id.bNotify);
            }
        });



        logout.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            showAlertDialog("Are you sure to logout ?",false);
        });





        //drawer views set

        LoginResponse loginResponse = new Gson().fromJson(SharedPref.read("LOGINRESPONSE", ""), LoginResponse.class);
        CircleImageView profilePic = headerView.findViewById(R.id.profile_pic);
        TextView profileName = headerView.findViewById(R.id.profile_name);
        TextView profileEmail = headerView.findViewById(R.id.profile_email);
        TextView poweredBy = findViewById(R.id.poweredId);
        String url = loginResponse.getData().getUserPictureURL();
        Log.d("getUserPictureURL", url);
        if (url != null && !url.isEmpty()) {
            Glide.with(this).load(url).placeholder(R.drawable.logo).error(R.drawable.logo).into(profilePic);
        }
        profileName.setText(loginResponse.getData().getFirstname() + " " + loginResponse.getData().getLastname());
        profileEmail.setText(loginResponse.getData().getEmail());
        poweredBy.setText(SharedPref.read("POWERBY", ""));




        // broadcast receiver

        LocalBroadcastManager.getInstance(this).registerReceiver(MessageReceiver,
                new IntentFilter("REALDATA"));
    }




    private BroadcastReceiver MessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            getAllOnlineOrderCount();
        }
    };




    private void getAllOnlineOrderCount() {
        WaitersService waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        String id = SharedPref.read("ID", "");
        waitersService.allOnlineOrder(id).enqueue(new Callback<NotificationResponse>() {
            @Override
            public void onResponse(Call<NotificationResponse> call, Response<NotificationResponse> response) {
                try {
                    SharedPref.write("NOTIFICATION", new Gson().toJson(response.body()));
                    int notificationSize = response.body().getData().getOrderinfo().size();
                    if (notificationSize > 0){
                        notifyBadge.setVisible(true);
                        notifyBadge.setNumber(notificationSize);
                    } else {
                        notifyBadge.setVisible(false);
                    }
                } catch (Exception ignored) {/**/}
            }
            @Override
            public void onFailure(Call<NotificationResponse> call, Throwable t) {/**/}
        });
    }




    @Override
    protected void onRestart() {
        super.onRestart();
        DashboardFragment dashboardFragment = new DashboardFragment();
        try {
            if (SharedPref.read("OVI", "").equals("TSET")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new NotificationFragment(), "ovi").commit();
                appHeader.setText("Customer Orders");
            } else if (SharedPref.read("OVI", "").equals("admin")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, new OrderFragment(), "ovi")
                        .commit();
            } else if (SharedPref.read("OVI", "").equals("")) {
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, dashboardFragment, dashboardFragment.getTag())
                        .commit();
                appHeader.setText("Menu");
            } else {
               /* SharedPref.write("ORDERSTATUS", "2");
                Intent intent = new Intent(MainActivity.this, ViewOrderActivity.class);
                intent.putExtra("ORDERID", SharedPref.read("OVI", ""));
                startActivity(intent);*/
            }
            SharedPref.write("OVI", "");
        } catch (Exception e) {
            try {
                Log.d("sadsdfsdf", "onRestart: " + e.getLocalizedMessage());
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main, dashboardFragment, dashboardFragment.getTag())
                        .commit();
                appHeader.setText("Menu");
            } catch (Exception e1) {/**/}
        }
    }




    private void setSelectorColor(TextView enable, TextView disable1, TextView disable2, TextView disable3, TextView disable4) {
        enable.setBackgroundColor(0x30ffffff);
        disable1.setBackgroundColor(0x00000000);
        disable2.setBackgroundColor(0x00000000);
        disable3.setBackgroundColor(0x00000000);
        disable4.setBackgroundColor(0x00000000);
    }




    private void showAlertDialog(String message, boolean exit) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setMessage(message);
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                if (exit) {
                    finish();
                } else {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    SharedPref.write("LOGGEDIN", "NO");
                    SharedPref.write("BASEURL", "");
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }




    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        getAllOnlineOrderCount();
    }




    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if(rootMenu){
            go = true;
            btmNav.setSelectedItemId(R.id.bMenu);
        } else {
            showAlertDialog("Are you sure to Exit ?",true);
        }
    }




    public static void onResumeAppFrags(){
        appSearchBar.setQuery("",false);
        if (Integer.parseInt(SharedPref.read("CartCount","0")) > 0){
            appCartBadge.setVisibility(View.VISIBLE);
            appCartBadge.setText(SharedPref.read("CartCount","0"));
        } else {
            appCartBadge.setVisibility(View.GONE);
        }
    }




    public static void appBarDefault(){
        appSearchLay.setVisibility(View.GONE);
        appHeader.setVisibility(View.VISIBLE);
        appBtnLay.setVisibility(View.VISIBLE);
        appSearchBar.setQuery("",false);
    }
}