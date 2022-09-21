package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignFoodCartItemBinding;
import com.restorapos.waiters.interfaces.SumInterface;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.utils.SharedPref;
import java.text.DecimalFormat;
import java.util.List;

public class FoodCartsAdapter extends RecyclerView.Adapter<FoodCartsAdapter.ViewHolder> {

    private List<Foodinfo> items;
    private Context context;
    double total = 0.0;
    SumInterface sumInterface;

    public FoodCartsAdapter(Context applicationContext, List<Foodinfo> itemArrayList, SumInterface sumInterface) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.sumInterface = sumInterface;
        SharedPref.init(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_food_cart_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint({"SetTextI18n", "RecyclerView"})
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        Log.d("ASDas", "onBindViewHolder: ");
        if (items.get(i).getAddOnsName() == null) {
            holder.binding.productName.setText(items.get(i).getProductName());
        } else {
            holder.binding.productName.setText(items.get(i).getProductName());
            try {
                for (int j = 0; j < items.get(i).getAddonsinfo().size(); j++) {
                    try {
                        int p = items.get(i).getAddonsinfo().get(j).addonsquantity;
                        Log.d("poisdfsd", "onBindViewHolder: " + p);
                        if (p > 0) {
                            holder.binding.productName.append("," + items.get(i).getAddonsinfo().get(j).getAddOnName());
                        }
                    } catch (Exception ign) {
                        Log.d("poisdfsd", "onBindViewHolder: " + ign.getLocalizedMessage());
                    }
                }
            } catch (Exception e) {
            }
        }


        holder.binding.unitPriceTv.setText(SharedPref.read("CURRENCY", "") + items.get(i).getPrice());
        holder.binding.sizeTv.setText(items.get(i).getVariantName());
        holder.binding.quantityTv.setText(String.valueOf(items.get(i).quantitys));
        holder.binding.variantTv.setText(items.get(i).getVariantName());

        double x = 0;
        try {
            for (int p = 0; p < items.get(i).getAddonsinfo().size(); p++) {
                items.get(i).setAddons(1);
                Log.d("TAG", "onBindViewHolder: " + items.get(i).getAddonsinfo().get(p).getAddOnName());
            }
        } catch (Exception ignored) {
        }
        Log.d("OK", "onBindViewHolder: " + x);
        int counts = items.get(i).quantitys;
        items.get(i).setQuantity(counts);
        total = Double.parseDouble(items.get(i).getPrice()) * counts + Double.valueOf(items.get(i).getAddOnsTotal());
        holder.binding.totalPriceTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));



        holder.binding.itemNoteEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                items.get(i).itemNote = String.valueOf(editable);
            }
        });




        holder.binding.noteShowTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.binding.itemNoteEt.getVisibility()==View.GONE){
                    holder.binding.noteShowTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_up_24,0);
                    holder.binding.itemNoteEt.setVisibility(View.VISIBLE);
                }
                else {
                    holder.binding.noteShowTv.setCompoundDrawablesWithIntrinsicBounds(0,0,R.drawable.ic_baseline_arrow_drop_down_24,0);
                    holder.binding.itemNoteEt.setVisibility(View.GONE);
                }
            }
        });



        holder.binding.plusBtn.setOnClickListener(v -> {
            int count = Integer.parseInt(String.valueOf(holder.binding.quantityTv.getText()));
            count++;
            holder.binding.quantityTv.setText(String.valueOf(count));
            items.get(i).quantitys = count;
            items.get(i).setQuantity(count);
            total = Double.parseDouble(items.get(i).getPrice()) * count + Double.valueOf(items.get(i).getAddOnsTotal());
            holder.binding.totalPriceTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));

            sumInterface.addedSum(items.get(i));

        });



        holder.binding.minusBtn.setOnClickListener(v -> {
            int count = Integer.parseInt(String.valueOf(holder.binding.quantityTv.getText()));
            if (count > 1) {
                count--;
                holder.binding.quantityTv.setText(String.valueOf(count));
                items.get(i).quantitys = count;
                items.get(i).setQuantity(count);
                total = Double.parseDouble(items.get(i).getPrice()) * count + Double.valueOf(items.get(i).getAddOnsTotal());
                if (items.get(i).quantitys == 0) {
                    total = total - Double.valueOf(items.get(i).getAddOnsTotal());
                }
                holder.binding.totalPriceTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(total)));

                sumInterface.divideSum(items.get(i));
            } else {
                sumInterface.deleteSum(items.get(i),i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private DesignFoodCartItemBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DesignFoodCartItemBinding.bind(view);
        }
    }
}

