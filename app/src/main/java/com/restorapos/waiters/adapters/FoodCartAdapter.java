package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignFoodCartItemBinding;
import com.restorapos.waiters.interfaces.SumInterface;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.utils.SharedPref;

import java.text.DecimalFormat;
import java.util.List;

public class FoodCartAdapter extends RecyclerView.Adapter<FoodCartAdapter.ViewHolder> {
    private final List<Foodinfo> items;
    double total = 0.0;
    SumInterface sumInterface;

    public FoodCartAdapter(Context context, List<Foodinfo> itemArrayList, SumInterface sumInterface) {
        SharedPref.init(context);
        this.items = itemArrayList;
        this.sumInterface = sumInterface;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_food_cart_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RecyclerView"})
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int pos) {
        boolean isCustomQty;
        if (items.get(pos).getIscustqty() != null && items.get(pos).getIscustqty().equals("1")) {
            isCustomQty = true;
        } else {
            isCustomQty = false;
        }
        
        if (items.get(pos).getAddOnsName() == null) {
            holder.binding.productName.setText(items.get(pos).getProductName());
        } else {
            holder.binding.productName.setText(items.get(pos).getProductName());
            try {
                for (int j = 0; j < items.get(pos).getAddonsinfo().size(); j++) {
                    try {
                        int p = items.get(pos).getAddonsinfo().get(j).addonsquantity;
                        Log.d("poisdfsd", "onBindViewHolder: " + p);
                        if (p > 0) {
                            holder.binding.productName.append("," + items.get(pos).getAddonsinfo().get(j).getAddOnName());
                        }
                    } catch (Exception ign) {
                        Log.d("poisdfsd", "onBindViewHolder: " + ign.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {/**/}
        }

        holder.binding.unitPriceTv.setText(SharedPref.read("CURRENCY", "") + items.get(pos).getPrice());
        holder.binding.sizeTv.setText(items.get(pos).getVariantName());
        holder.binding.quantityTv.setText(getQuantity(items.get(pos).quantitys,isCustomQty));
        holder.binding.variantTv.setText(items.get(pos).getVariantName());
        holder.binding.itemNoteEt.setText(items.get(pos).getItemNote());

        double x = 0;
        try {
            for (int p = 0; p < items.get(pos).getAddonsinfo().size(); p++) {
                items.get(pos).setAddons(1);
                Log.d("TAG", "onBindViewHolder: " + items.get(pos).getAddonsinfo().get(p).getAddOnName());
            }
        } catch (Exception ignored) {
        }
        Log.d("OK", "onBindViewHolder: " + x);
        float counts = items.get(pos).quantitys;
        items.get(pos).setQuantity(counts);
        total = Double.parseDouble(items.get(pos).getPrice()) * counts + Double.valueOf(items.get(pos).getAddOnsTotal());
        holder.binding.totalPriceEt.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));


        holder.binding.itemNoteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                items.get(pos).setItemNote(String.valueOf(charSequence));
            }

            @Override
            public void afterTextChanged(Editable editable) {/**/}
        });


        holder.binding.noteShowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.itemNoteEt.getVisibility() == View.GONE) {
                    holder.binding.noteShowTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_up_24, 0);
                    holder.binding.itemNoteEt.setVisibility(View.VISIBLE);
                } else {
                    holder.binding.noteShowTv.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_baseline_arrow_drop_down_24, 0);
                    holder.binding.itemNoteEt.setVisibility(View.GONE);
                }
            }
        });


        holder.binding.plusBtn.setOnClickListener(v -> {
            float count = Float.parseFloat(holder.binding.quantityTv.getText().toString());
            count++;
            holder.binding.quantityTv.setText(getQuantity(count,isCustomQty));
            items.get(pos).quantitys = count;
            items.get(pos).setQuantity(count);
            total = Double.parseDouble(items.get(pos).getPrice()) * count + Double.valueOf(items.get(pos).getAddOnsTotal());
            holder.binding.totalPriceEt.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));
            sumInterface.addedSum(items.get(pos));
        });


        holder.binding.minusBtn.setOnClickListener(v -> {
            float count = Float.parseFloat(holder.binding.quantityTv.getText().toString());
            if (count > 1) {
                count--;
                holder.binding.quantityTv.setText(getQuantity(count,isCustomQty));
                items.get(pos).quantitys = count;
                items.get(pos).setQuantity(count);
                total = Double.parseDouble(items.get(pos).getPrice()) * count + Double.valueOf(items.get(pos).getAddOnsTotal());
                if (items.get(pos).quantitys == 0) {
                    total = total - Double.valueOf(items.get(pos).getAddOnsTotal());
                }
                holder.binding.totalPriceEt.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));

                sumInterface.divideSum(items.get(pos));
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DesignFoodCartItemBinding binding;
        public ViewHolder(View view) {
            super(view);
            binding = DesignFoodCartItemBinding.bind(view);
        }
    }

    private String getQuantity(float quantity, boolean isCustom) {
        if (isCustom == true) {
            return String.valueOf(quantity);
        } else {
            return String.valueOf(quantity).split("\\.")[0];
        }
    }
}

