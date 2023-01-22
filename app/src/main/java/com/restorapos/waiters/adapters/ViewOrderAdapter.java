package com.restorapos.waiters.adapters;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignFoodViewOrderItemBinding;
import com.restorapos.waiters.model.viewOrderModel.IteminfoItem;

import java.text.DecimalFormat;
import java.util.List;

public class ViewOrderAdapter extends RecyclerView.Adapter<ViewOrderAdapter.ViewOrderVH> {
    private final List<IteminfoItem> items;
    private final String orderTag;
    private final String currency;

    public ViewOrderAdapter(List<IteminfoItem> items, String orderTag, String currency) {
        this.items = items;
        this.orderTag = orderTag;
        this.currency = currency;
    }

    @NonNull
    @Override
    public ViewOrderVH onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.design_food_view_order_item, parent, false);
        return new ViewOrderVH(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewOrderVH holder, int pos) {
        double addonsTotal = 0.0;
        if (items.get(pos).getAddons().equals(1)) {
            for (int i = 0; i < items.get(pos).getAddonsinfo().size(); i++) {
                if (Integer.parseInt(items.get(pos).getAddonsinfo().get(i).getAddOnQty()) > 0) {
                    holder.binding.addOnName.append(items.get(pos).getAddonsinfo().get(i).getAddonsName() + " x" + items.get(pos).getAddonsinfo().get(i).getAddOnQty() + "\n");
                    addonsTotal += Double.parseDouble(items.get(pos).getAddonsinfo().get(i).getPrice()) * Double.parseDouble(items.get(pos).getAddonsinfo().get(i).getAddOnQty());
                }
            }

        }
        holder.binding.productName.setText(items.get(pos).getProductName());
        if (orderTag.equals("Ready")) {
            holder.binding.statusTv.setBackgroundResource(R.drawable.shape_ready);
        }
        try {
            if (items.get(pos).getItemqty() != null && !items.get(pos).getItemqty().isEmpty()) {
                String [] parts = items.get(pos).getItemqty().split("\\.");
                if (Integer.parseInt(parts[1]) > 0) {
                    holder.binding.quantityTv.setText("Quantity: " + items.get(pos).getItemqty());
                } else {
                    holder.binding.quantityTv.setText("Quantity: " + parts[0]);
                }
            }
        }catch (Exception e) {
            holder.binding.quantityTv.setText("Quantity: " + items.get(pos).getItemqty());
        }
        try {
            holder.binding.statusTv.setText(orderTag);
            holder.binding.sizeTv.setText("Size: " + items.get(pos).getVarientname());
        } catch (Exception e) {/**/}
        try {
            double total = (Double.parseDouble(items.get(pos).getPrice()) * Double.parseDouble(items.get(pos).getItemqty())) + addonsTotal;
            holder.binding.totalPriceTv.setText("Total Price: " + currency + new DecimalFormat("#.##").format(total));
        } catch (Exception e) {
            holder.binding.totalPriceTv.setText("Total Price: " + currency + "0.0");
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewOrderVH extends RecyclerView.ViewHolder {
        private final DesignFoodViewOrderItemBinding binding;
        public ViewOrderVH(View view) {
            super(view);
            binding = DesignFoodViewOrderItemBinding.bind(view);
        }
    }
}

