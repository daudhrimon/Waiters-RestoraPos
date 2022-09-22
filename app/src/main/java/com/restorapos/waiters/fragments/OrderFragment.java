package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.appHeader;
import static com.restorapos.waiters.MainActivity.btmNav;
import static com.restorapos.waiters.MainActivity.completeOrder;
import static com.restorapos.waiters.MainActivity.logout;
import static com.restorapos.waiters.MainActivity.menu;
import static com.restorapos.waiters.MainActivity.orderHistory;
import static com.restorapos.waiters.MainActivity.orderList;
import static com.restorapos.waiters.MainActivity.rootMenu;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.utils.SharedPref;

public class OrderFragment extends Fragment implements View.OnClickListener {
    private TextView pending, processing, complete, cancel;
    private Intent intent;
    @SuppressLint("StaticFieldLeak")
    public static SwipeRefreshLayout orderSwipe;
    private int go = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);


        initial(view);

        try{
            if (SharedPref.read("RED","").equals("2")){
                getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.orderContainer, new ProcessingFragment(), "ovi")
                        .commit();
                processing.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btmNav.setSelectedItemId(R.id.bProcess);
                MainActivity.appBarDefault();
                appHeader.setText("Processing Orders");
                go = 2;
            }
            else{
                PendingFragment pendingFragment = new PendingFragment();
                getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).add(R.id.orderContainer, pendingFragment, pendingFragment.getTag())
                        .commit();
                pending.setBackgroundColor(getResources().getColor(R.color.colorRed));
                btmNav.setSelectedItemId(R.id.bOrders);
                MainActivity.appBarDefault();
                appHeader.setText("Pending Orders");
                go = 1;
            }
        }
        catch (Exception ignored){/**/}
        pending.setOnClickListener(this);
        processing.setOnClickListener(this);
        complete.setOnClickListener(this);
        cancel.setOnClickListener(this);
        return view;
    }

    private void initial(View view) {
        btmNav.setVisibility(View.VISIBLE);
        MainActivity.appBarDefault();
        pending = view.findViewById(R.id.pendingId);
        processing = view.findViewById(R.id.processingId);
        complete = view.findViewById(R.id.readyId);
        cancel = view.findViewById(R.id.cancelId);
        orderList.setBackgroundColor(0x30ffffff);
        menu.setBackgroundColor(0x00000000);
        completeOrder.setBackgroundColor(0x00000000);
        orderHistory.setBackgroundColor(0x00000000);
        logout.setBackgroundColor(0x00000000);
        orderSwipe = view.findViewById(R.id.orderSwipe);
        rootMenu = true;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pendingId:
                if (go != 1) {
                    PendingFragment pendingFragment = new PendingFragment();
                    getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.orderContainer, pendingFragment, pendingFragment.getTag())
                            .commit();
                    processing.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    pending.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    complete.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    cancel.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    MainActivity.appBarDefault();
                    appHeader.setText("Pending Orders");
                    go = 1;
                }
                break;
            case R.id.processingId:
                if (go != 2) {
                    ProcessingFragment processingFragment = new ProcessingFragment();
                    getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.orderContainer, processingFragment, processingFragment.getTag())
                            .commit();
                    processing.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    pending.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    complete.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    cancel.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    MainActivity.appBarDefault();
                    appHeader.setText("Processing Orders");
                    go = 2;
                }
                break;
            case R.id.readyId:
                if (go != 3) {
                    ReadyFragment readyFragment = new ReadyFragment();
                    getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.orderContainer, readyFragment, readyFragment.getTag())
                            .commit();
                    processing.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    pending.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    complete.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    cancel.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    MainActivity.appBarDefault();
                    appHeader.setText("Ready Orders");
                    go = 3;
                }
                break;
            case R.id.cancelId:
                if (go != 4) {
                    CancelFragment cancelFragment = new CancelFragment();
                    getParentFragmentManager().beginTransaction().setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).replace(R.id.orderContainer, cancelFragment, cancelFragment.getTag())
                            .commit();
                    processing.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    pending.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    complete.setBackgroundColor(getResources().getColor(R.color.colorBlack));
                    cancel.setBackgroundColor(getResources().getColor(R.color.colorRed));
                    MainActivity.appBarDefault();
                    appHeader.setText("Cancel Orders");
                    go = 4;
                }
                break;
        }
    }
}
