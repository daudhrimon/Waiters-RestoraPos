package com.restorapos.waiters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignViewOrderBinding;
import com.restorapos.waiters.interfaces.ViewInterface;
import com.restorapos.waiters.model.Readyorder.ReadyorderData;
import com.restorapos.waiters.utils.SharedPref;

import java.util.List;

public class ReadyOrderAdapter extends RecyclerView.Adapter<ReadyOrderAdapter.ViewHolder> {
    private final List<ReadyorderData> items;
    private final ViewInterface viewInterface;

    public ReadyOrderAdapter(Context context, List<ReadyorderData> itemArrayList, ViewInterface viewInterface) {
        SharedPref.init(context);
        this.items = itemArrayList;
        this.viewInterface = viewInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_view_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int pos) {
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

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DesignViewOrderBinding binding;
        public ViewHolder(View view) {
            super(view);

            binding = DesignViewOrderBinding.bind(view);
        }
    }
}

