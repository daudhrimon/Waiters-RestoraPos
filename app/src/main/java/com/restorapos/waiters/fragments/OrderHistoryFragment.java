package com.restorapos.waiters.fragments;

import static com.restorapos.waiters.MainActivity.btmNav;
import static com.restorapos.waiters.MainActivity.rootMenu;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.model.orderHistoryModel.OrderHistoryResponse;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import java.util.ArrayList;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryFragment extends Fragment {
    private double Overallorder, Overallamount, lastweekorder, lastweekamount;
    private String id;
    private WaitersService waitersService;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_order_history, container, false);

        btmNav.setVisibility(View.GONE);
        SharedPref.init(getActivity());
        final SpotsDialog progressDialog=new SpotsDialog(getActivity(),R.style.Custom);
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        }, 2000);
        waitersService = AppConfig.getRetrofit(getContext()).create(WaitersService.class);
        id = SharedPref.read("ID", "");

        rootMenu = true;

        final PieChart pieChart = view.findViewById(R.id.any_chart_view);
        final PieChart pieChart1 = view.findViewById(R.id.any_chart_view1);
        getOrderHistory();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                pieChart.setUsePercentValues(true);

                ArrayList<Entry> yvalues = new ArrayList<Entry>();
                yvalues.add(new Entry((float) Overallamount, 0));
                yvalues.add(new Entry((float) lastweekamount, 1));

                PieDataSet dataSet = new PieDataSet(yvalues, "Waiter Amount");

                ArrayList<String> xVals = new ArrayList<String>();

                xVals.add("Overall Amount");
                xVals.add("last week amount");

                PieData data = new PieData(xVals, dataSet);
                // In Percentage term
                data.setValueFormatter(new PercentFormatter());
                // Default value
                pieChart.setData(data);
                pieChart.setDescription("");
                pieChart.setDrawHoleEnabled(true);
                pieChart.setTransparentCircleRadius(25f);
                pieChart.setHoleRadius(25f);

                dataSet.setColors(VORDIPLOM_COLORS);
                data.setValueTextSize(13f);
                data.setValueTextColor(Color.WHITE);
                pieChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                        if (e == null)
                            return;
                        Log.i("VAL SELECTED",
                                "Value: " + e.getVal() + ", " +
                                        "xIndex: " + e.getXIndex()
                                        + ", DataSet index: " + dataSetIndex);
                        Toast.makeText(getActivity(), "Amount: $" + e.getVal(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected() {
                        Log.i("PieChart", "nothing selected");
                    }
                });
                pieChart.animateXY(1400, 1400);

                pieChart1.setUsePercentValues(true);

                // IMPORTANT: In a PieChart, no values (Entry) should have the same
                // xIndex (even if from different DataSets), since no values can be
                // drawn above each other.
                ArrayList<Entry> yvalues1 = new ArrayList<Entry>();
                yvalues1.add(new Entry((float) Overallorder, 0));
                yvalues1.add(new Entry((float) lastweekorder, 1));

                PieDataSet dataSet1 = new PieDataSet(yvalues1, "Waiter Order");

                ArrayList<String> xVals1 = new ArrayList<String>();

                xVals1.add("Overall order");
                xVals1.add("last week order");

                PieData data1 = new PieData(xVals1, dataSet1);
                // In Percentage term
                data1.setValueFormatter(new PercentFormatter());
                // Default value
                //data.setValueFormatter(new DefaultValueFormatter(0));
                pieChart1.setData(data1);
                pieChart1.setDescription("");
                pieChart1.setDrawHoleEnabled(true);
                pieChart1.setTransparentCircleRadius(25f);
                pieChart1.setHoleRadius(25f);

                dataSet1.setColors(VORDIPLOM_COLORS);
                data1.setValueTextSize(13f);
                data1.setValueTextColor(Color.WHITE);
                pieChart1.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
                    @Override
                    public void onValueSelected(Entry e, int dataSetIndex, Highlight h) {
                        if (e == null)
                            return;
                        Log.i("VAL SELECTED",
                                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                                        + ", DataSet index: " + dataSetIndex);
                        Toast.makeText(getActivity(), "Order: " + e.getVal(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNothingSelected() {
                        Log.i("PieChart", "nothing selected");
                    }
                });

                pieChart1.animateXY(1400, 1400);
            }
        }, 3000);

        return view;
    }

    private void getOrderHistory() {
        waitersService.getorderHistory(id).enqueue(new Callback<OrderHistoryResponse>() {
            @Override
            public void onResponse(Call<OrderHistoryResponse> call, Response<OrderHistoryResponse> response) {
                try {
                    Log.d("ppp", "onResponse: " + response.body().getStatus());
                    Overallorder = Double.parseDouble(response.body().getData().getOverallorder());
                    Overallamount = Double.parseDouble(response.body().getData().getOverallamount());
                    lastweekorder = Double.parseDouble(response.body().getData().getLastweekorder());
                    lastweekamount = Double.parseDouble(response.body().getData().getLastweekamount());


                } catch (Exception e) {
                    Log.d("ppp", "onResponse: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<OrderHistoryResponse> call, Throwable t) {
                Log.d("ppp", "onFailure: "+t.getLocalizedMessage());
            }
        });
    }


    public static final int[] VORDIPLOM_COLORS = {
            Color.rgb(255, 33, 59), Color.rgb(0, 0, 0)};




    @Override
    public void onResume() {
        super.onResume();
        MainActivity.onResumeAppFrags();
    }
}
