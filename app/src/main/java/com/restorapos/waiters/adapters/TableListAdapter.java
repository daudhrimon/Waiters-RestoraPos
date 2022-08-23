package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.FoodCartActivity;
import com.restorapos.waiters.model.tableModel.TableInfo;
import com.restorapos.waiters.utils.SharedPref;
import java.util.List;
import es.dmoral.toasty.Toasty;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder> {
    private List<TableInfo> items;
    private Context context;
    int counter = 0;
    int row_index = -1;
    private FoodCartActivity activity;
    private String tableId = "";

    public
    TableListAdapter(Context applicationContext, List<TableInfo> itemArrayList, FoodCartActivity activity) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.activity = activity;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_tablelist_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, @SuppressLint("RecyclerView") final int i) {
        tableId = SharedPref.read("UPDATETABLE", "");
        viewHolder.categoryName.setText(items.get(i).getTableName());
        viewHolder.personAvailable.setText(items.get(i).getAvailable() + ":" + items.get(i).getCapacity());
        Log.wtf("onBindViewHolderOVISSSSSSSS", "onBindViewHolder: " + tableId);
        Log.wtf("onBindViewHolderOVISSSSSSSS", "onBindViewHolder: " + items.get(i).getTableId());

        if (!tableId.equals("")){
            if (tableId.equals(items.get(i).getTableId())) {
                row_index = i;
                if (row_index==i){
                    viewHolder.layout.setBackgroundColor(0xFFFF213B);
                    viewHolder.categoryName.setTextColor(Color.parseColor("#FFFFFF"));
                    viewHolder.personAvailable.setTextColor(Color.parseColor("#FFFFFF"));
                    viewHolder.categoryImage.setColorFilter(Color.argb(255, 255, 255, 255));
                    FoodCartActivity.tableID = items.get(i).getTableId();
                }
            }

        }

        if (SharedPref.read("UPDATETABLE", "") != null
                && SharedPref.read("UPDATETABLE", "").equals(items.get(i).getTableId())) {
            row_index = i;
        }

        if (SharedPref.read("tableMap","").equals("0")){
            viewHolder.personAvailable.setVisibility(View.GONE);
        }else{
            viewHolder.personAvailable.setVisibility(View.VISIBLE);
        }
        viewHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.checktableExistence(items.get(i).getTableId()) == 0 && row_index != i){
                    if (SharedPref.read("tableMap","").equals("1")){
                        selectPerson(i, items);
                    }else{
                        activity.setTableData(items.get(i).getTableId(),"0");
                    }
                    SharedPref.write("TABLE", items.get(i).getTableId());
                    FoodCartActivity.tableID = items.get(i).getTableId();
                    viewHolder.layout.setBackgroundColor(0xFFFF213B);
                    viewHolder.categoryName.setTextColor(Color.parseColor("#FFFFFF"));
                    viewHolder.personAvailable.setTextColor(Color.parseColor("#FFFFFF"));
                    viewHolder.categoryImage.setColorFilter(Color.argb(255, 255, 255, 255));
                }
                else{
                    row_index = -1;
                    activity.checktableExistence(items.get(i).getTableId());
                    viewHolder.layout.setBackgroundColor(0xFFFFFFFF);
                    viewHolder.categoryName.setTextColor(Color.parseColor("#000000"));
                    viewHolder.personAvailable.setTextColor(Color.parseColor("#ff213b"));
                    viewHolder.categoryImage.setColorFilter(Color.argb(100, 0, 0, 0));
                }

            }
        });

    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private void selectPerson(int i, List<TableInfo> items) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.person_select_layout, null);
        ImageView plusBtn = view2.findViewById(R.id.plusBtn);
        ImageView minusBtn = view2.findViewById(R.id.minusBtn);
        TextView personTxt = view2.findViewById(R.id.personTxt);
        Button okBtn = view2.findViewById(R.id.okBtn);
        personTxt.setText("" + counter);
        builder.setView(view2);
        AlertDialog alert = builder.create();

        plusBtn.setOnClickListener(v -> {
            int availableSeat1 = Integer.parseInt(items.get(i).getAvailable());

            if (counter < availableSeat1) {
                counter++;
                personTxt.setText("" + counter);
                FoodCartActivity.countedPerson = personTxt.getText().toString();
            } else {
                Toasty.normal(context, "Only " + items.get(i).getAvailable() + " person availabe in table").show();
            }
        });

        minusBtn.setOnClickListener(v -> {
            if (counter > 0) {
                counter--;
                personTxt.setText("" + counter);
                FoodCartActivity.countedPerson = personTxt.getText().toString();
            }
        });

        okBtn.setOnClickListener(v -> {
            int availableSeat1 = Integer.parseInt(items.get(i).getAvailable());
            if (counter <= availableSeat1) {
                activity.setTableData(items.get(i).getTableId(),personTxt.getText().toString());
                alert.dismiss();
            } else {
                Toasty.error(context, "Check person limit of the table!").show();
            }
        });

        alert.show();
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, personAvailable;
        ImageView categoryImage;
        LinearLayout layout;

        public ViewHolder(View view) {
            super(view);
            categoryImage = view.findViewById(R.id.catergoryImageId);
            categoryName = view.findViewById(R.id.catergoryNameId);
            personAvailable = view.findViewById(R.id.personAvailable);
            layout = view.findViewById(R.id.layoutId);

        }
    }
}

