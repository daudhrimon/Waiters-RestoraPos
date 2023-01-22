package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.CartActivity;
import com.restorapos.waiters.databinding.DesignTablelistItemBinding;
import com.restorapos.waiters.databinding.PersonSelectLayoutBinding;
import com.restorapos.waiters.model.tableModel.TableInfo;
import com.restorapos.waiters.utils.SharedPref;

import java.util.List;

import es.dmoral.toasty.Toasty;

public class TableListAdapter extends RecyclerView.Adapter<TableListAdapter.ViewHolder> {
    private final List<TableInfo> items;
    private final Context context;
    int counter = 0;
    int row_index = -1;
    private final CartActivity activity;

    public TableListAdapter(Context context, List<TableInfo> itemArrayList, CartActivity activity) {
        this.context = context;
        this.items = itemArrayList;
        this.activity = activity;
        SharedPref.init(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_tablelist_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, @SuppressLint("RecyclerView") final int pos) {
        String tableId = SharedPref.read("UPDATETABLE", "");
        holder.binding.categoryName.setText(items.get(pos).getTableName());
        holder.binding.personAvailable.setText(items.get(pos).getAvailable() + ":" + items.get(pos).getCapacity());
        Log.wtf("onBindViewHolderOVISSSSSSSS", "onBindViewHolder: " + tableId);
        Log.wtf("onBindViewHolderOVISSSSSSSS", "onBindViewHolder: " + items.get(pos).getTableId());

        if (!tableId.equals("")) {
            if (tableId.equals(items.get(pos).getTableId())) {
                row_index = pos;
                if (row_index == pos) {
                    holder.binding.tableLay.setBackgroundColor(ContextCompat.getColor(context,R.color.theme_color));
                    holder.binding.categoryName.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.binding.personAvailable.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.binding.categoryImage.setColorFilter(Color.argb(255, 255, 255, 255));
                    CartActivity.tableID = items.get(pos).getTableId();
                }
            }

        }

        if (SharedPref.read("UPDATETABLE", "") != null
                && SharedPref.read("UPDATETABLE", "").equals(items.get(pos).getTableId())) {
            row_index = pos;
        }

        if (SharedPref.read("tableMap", "").equals("0")) {
            holder.binding.personAvailable.setVisibility(View.GONE);
        } else {
            holder.binding.personAvailable.setVisibility(View.VISIBLE);
        }
        holder.binding.tableLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (activity.checkTableExistence(items.get(pos).getTableId()) == 0 && row_index != pos) {
                    if (SharedPref.read("tableMap", "").equals("1")) {
                        selectPerson(pos, items);
                    } else {
                        activity.setTableData(items.get(pos).getTableId(), "0");
                    }
                    SharedPref.write("TABLE", items.get(pos).getTableId());
                    CartActivity.tableID = items.get(pos).getTableId();
                    holder.binding.tableLay.setBackgroundColor(ContextCompat.getColor(context,R.color.theme_color));
                    holder.binding.categoryName.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.binding.personAvailable.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.binding.categoryImage.setColorFilter(Color.argb(255, 255, 255, 255));
                } else {
                    row_index = -1;
                    activity.checkTableExistence(items.get(pos).getTableId());
                    holder.binding.tableLay.setBackgroundColor(Color.parseColor("#00000000"));
                    holder.binding.categoryName.setTextColor(Color.parseColor("#000000"));
                    holder.binding.personAvailable.setTextColor(Color.parseColor("#ff213b"));
                    holder.binding.categoryImage.setColorFilter(Color.argb(100, 0, 0, 0));
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

        Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.person_select_layout, null);

        PersonSelectLayoutBinding dBinding = PersonSelectLayoutBinding.bind(view);

        dBinding.personTxt.setText("" + counter);

        dialog.setContentView(view);


        dBinding.plusBtn.setOnClickListener(v -> {
            int availableSeat1 = Integer.parseInt(items.get(i).getAvailable());

            if (counter < availableSeat1) {
                counter++;
                dBinding.personTxt.setText("" + counter);
                CartActivity.countedPerson = dBinding.personTxt.getText().toString();
            } else {
                Toasty.normal(context, "Only " + items.get(i).getAvailable() + " person availabe in table").show();
            }
        });


        dBinding.minusBtn.setOnClickListener(v -> {
            if (counter > 0) {
                counter--;
                dBinding.personTxt.setText("" + counter);
                CartActivity.countedPerson = dBinding.personTxt.getText().toString();
            }
        });


        dBinding.okBtn.setOnClickListener(v -> {
            int availableSeat1 = Integer.parseInt(items.get(i).getAvailable());
            if (counter <= availableSeat1) {
                activity.setTableData(items.get(i).getTableId(), dBinding.personTxt.getText().toString());
                dialog.dismiss();
            } else {
                Toasty.error(context, "Check person limit of the table!").show();
            }
        });


        dialog.show();
        Window win = dialog.getWindow();
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DesignTablelistItemBinding binding;
        public ViewHolder(View view) {
            super(view);
            binding = DesignTablelistItemBinding.bind(view);
        }
    }
}

