package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignNotificationBinding;
import com.restorapos.waiters.model.notificationModel.OrderinfoItem;
import com.restorapos.waiters.interfaces.NotificationInterface;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.NotificationVH> {
    private final List<OrderinfoItem> items;
    NotificationInterface notificationInterface;

    public NotificationAdapter(List<OrderinfoItem> itemArrayList, NotificationInterface notificationInterface) {
        this.items = itemArrayList;
        this.notificationInterface = notificationInterface;
    }

    @NonNull
    @Override
    public NotificationVH onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_notification, viewGroup, false);
        return new NotificationVH(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(NotificationVH holder, int pos) {

        holder.binding.orderNo.setText(items.get(pos).getOrderid());
        holder.binding.cusName.setText(items.get(pos).getCustomer());
        holder.binding.amount.setText(items.get(pos).getAmount());


        holder.binding.acceptBtn.setOnClickListener(view -> {

            notificationInterface.acceptOrder(items.get(pos).getOrderid(), null);
        });


        holder.itemView.setOnClickListener(view -> {

            notificationInterface.viewOrder(items.get(pos));
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class NotificationVH extends RecyclerView.ViewHolder {
        private final DesignNotificationBinding binding;
        NotificationVH(View view) {
            super(view);
            binding = DesignNotificationBinding.bind(view);
        }
    }
}

