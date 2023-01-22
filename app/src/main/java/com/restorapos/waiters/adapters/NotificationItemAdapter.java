package com.restorapos.waiters.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignFoodViewOrderItemBinding;
import com.restorapos.waiters.model.notificationModel.ItemsInfo;

import java.util.List;

public class NotificationItemAdapter extends RecyclerView.Adapter<NotificationItemAdapter.NotificationItemVH> {
    private final List<ItemsInfo> itemList;
    private final String currency;

    public NotificationItemAdapter(List<ItemsInfo> itemList, String currency) {
        this.itemList = itemList;
        this.currency = currency;
    }

    @NonNull
    @Override
    public NotificationItemVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_food_view_order_item, parent, false);
        return new NotificationItemVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationItemVH holder, int pos) {

        double addonsTotal = 0.0;
        if (itemList.get(pos).getAddons().equals(1)) {
            for (int i = 0; i < itemList.get(pos).getAddonsinfo().size(); i++) {
                if (Integer.parseInt(itemList.get(pos).getAddonsinfo().get(i).getAddOnQty()) > 0) {

                    holder.binding.addOnName.append(itemList.get(pos).getAddonsinfo().get(i).getAddonsName() + " x" + itemList.get(pos).getAddonsinfo().get(i).getAddOnQty() + "\n");
                    addonsTotal += Double.parseDouble(itemList.get(pos).getAddonsinfo().get(i).getPrice()) * Double.parseDouble(itemList.get(pos).getAddonsinfo().get(i).getAddOnQty());
                }
            }
        }

        try {
            holder.binding.productName.setText(itemList.get(pos).getProductName());
        } catch (Exception e) {/**/}

        try {
            holder.binding.sizeTv.setText("Size: " + itemList.get(pos).getVarientname());
        } catch (Exception e) {/**/}

        try {
            holder.binding.quantityTv.setText("Quantity: " + itemList.get(pos).getItemqty());
        } catch (Exception e) {/**/}

        try {
            double total = (Double.parseDouble(itemList.get(pos).getPrice()) * Double.parseDouble(itemList.get(pos).getItemqty())) + addonsTotal;
            holder.binding.totalPriceTv.setText("Total Price: " + currency + total);
        } catch (Exception e) {/**/}
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class NotificationItemVH extends RecyclerView.ViewHolder {
        private final DesignFoodViewOrderItemBinding binding;
        public NotificationItemVH(@NonNull View itemView) {
            super(itemView);
            binding = DesignFoodViewOrderItemBinding.bind(itemView);
        }
    }
}
