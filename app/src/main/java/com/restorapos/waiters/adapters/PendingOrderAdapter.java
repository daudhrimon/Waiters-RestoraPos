package com.restorapos.waiters.adapters;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.CartActivity;
import com.restorapos.waiters.databinding.DesignEditViewOrderBinding;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.pendingOrderModel.DataItem;
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.utils.SharedPref;

import java.util.List;

public class PendingOrderAdapter extends RecyclerView.Adapter<PendingOrderAdapter.OrderViewHolder> {
    private final List<DataItem> items;
    private final Context context;
    ViewInterface viewInterface;

    public PendingOrderAdapter(Context context, List<DataItem> itemArrayList, ViewInterface viewInterface) {
        this.context = context;
        this.items = itemArrayList;
        this.viewInterface = viewInterface;
        SharedPref.init(context);
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_edit_view_order, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OrderViewHolder holder, int pos) {
        holder.binding.orderId.setText("Order Id: " + items.get(pos).getOrderId());
        if (items.get(pos).getTokenno() != null) {
            holder.binding.token.setText("Token No: " + items.get(pos).getTokenno());
        }
        if (items.get(pos).getCustomerName().length() == 1) {
            holder.binding.customerName.setText("0000" + items.get(pos).getCustomerName());
        } else if (items.get(pos).getCustomerName().length() == 2) {
            holder.binding.customerName.setText("000" + items.get(pos).getCustomerName());
        } else if (items.get(pos).getCustomerName().length() == 3) {
            holder.binding.customerName.setText("00" + items.get(pos).getCustomerName());
        } else if (items.get(pos).getCustomerName().length() == 4) {
            holder.binding.customerName.setText("0" + items.get(pos).getCustomerName());
        } else {
            holder.binding.customerName.setText(items.get(pos).getCustomerName());
        }
        holder.binding.tableTv.setText(items.get(pos).getTableName());
        holder.binding.dateTv.setText(items.get(pos).getOrderDate());
        holder.binding.amountTv.setText(SharedPref.read("CURRENCY", "") + items.get(pos).getTotalAmount());


        holder.binding.view.setOnClickListener(view -> {
            viewInterface.viewOrder(items.get(pos).getOrderId());
        });


        holder.binding.edit.setOnClickListener(view -> {
            Intent intent = new Intent(context, CartActivity.class);
            SharedPref.write("ORDERID", items.get(pos).getOrderId());
            intent.putExtra("ORDERID", items.get(pos).getOrderId());
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            deleteTable();
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {
        private final DesignEditViewOrderBinding binding;
        public OrderViewHolder(View view) {
            super(view);
            binding = DesignEditViewOrderBinding.bind(view);
        }
    }

    private void deleteTable() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(context).getAppDatabase()
                        .taskDao()
                        .deleteFoodTable();
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }

        DeleteTask dt = new DeleteTask();
        dt.execute();

    }
}

