package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.FoodActivity;
import com.restorapos.waiters.databinding.DesignCartAddonsBinding;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddOnsItemAdapter extends RecyclerView.Adapter<AddOnsItemAdapter.ViewHolder> {
    private final List<Addonsinfo> addonsInfo;
    private List<Addonsinfo> addonsList = new ArrayList<>();
    private double total;
    private double foodPrice;
    private int addOnsCounter = 1;
    private int addOnsChecker = 0;
    private final TextView totalPriceTv;

    public AddOnsItemAdapter(List<Addonsinfo> addonsInfo, TextView totalPriceTv) {
        this.addonsInfo = addonsInfo;
        this.totalPriceTv = totalPriceTv;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_cart_addons, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int pos) {

        holder.binding.productName.setText(addonsInfo.get(pos).getAddOnName());
        holder.binding.unitPriceTv.setText(addonsInfo.get(pos).getAddonsprice());
        holder.binding.quantityTv.setText(String.valueOf(addOnsCounter));
        final int count = Integer.parseInt(String.valueOf(holder.binding.quantityTv.getText()));

        if (!addonsInfo.get(pos).getAddonsprice().equals("")) {
            total = Double.parseDouble(addonsInfo.get(pos).getAddonsprice()) * count;
        }

        FoodActivity.addOnsChecker = addOnsChecker;
        holder.binding.addonPriceTv.setText(String.valueOf(total));


        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (holder.binding.checkBox.isChecked()) {
                    addonsInfo.get(pos).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                    Addonsinfo tempAddons = new Addonsinfo(addonsInfo.get(pos).getAddonsid(),
                            addonsInfo.get(pos).getAddOnName(), holder.binding.addonPriceTv.getText().toString(),
                            Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                    if (addonsList.size() != 0) {
                        Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
                        addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                    }
                    addonsList.add(tempAddons);
                    SharedPref.write("addOnslist", new Gson().toJson(addonsList));
                    addOnsChecker = 1;
                    FoodActivity.addOnsChecker = 1;
                    foodPrice = Double.parseDouble(totalPriceTv.getText().toString());
                    double addonPrice = Double.parseDouble(holder.binding.addonPriceTv.getText().toString());
                    totalPriceTv.setText(String.valueOf(foodPrice + addonPrice));
                } else {
                    Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
                    addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                    for (int j = 0; j < addonsList.size(); j++) {
                        if (addonsList.get(j).getAddonsid().equals(addonsInfo.get(pos).getAddonsid())) {
                            addonsList.remove(j);
                            SharedPref.write("addOnslist", new Gson().toJson(addonsList));
                        }
                    }
                    if (addonsList.size() == 0) {
                        addOnsChecker = 0;
                        FoodActivity.addOnsChecker = 0;
                    }
                    foodPrice = Double.parseDouble(totalPriceTv.getText().toString());
                    double addonPrice = Double.parseDouble(holder.binding.addonPriceTv.getText().toString());
                    totalPriceTv.setText(String.valueOf(foodPrice - addonPrice));
                }
            }
        });


        holder.binding.plusTv.setOnClickListener(view -> {
            addOnsCounter = Integer.parseInt(holder.binding.quantityTv.getText().toString());
            addOnsCounter++;
            holder.binding.quantityTv.setText(String.valueOf(addOnsCounter));
            holder.binding.addonPriceTv.setText(String.valueOf(Double.parseDouble(addonsInfo.get(pos).getAddonsprice()) * addOnsCounter));
            if (holder.binding.checkBox.isChecked()) {
                foodPrice = Double.parseDouble(totalPriceTv.getText().toString());
                double itemPrice = foodPrice + Double.parseDouble(addonsInfo.get(pos).getAddonsprice());
                totalPriceTv.setText(String.valueOf(itemPrice));
                addonsInfo.get(pos).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
                addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                for (int j = 0; j < addonsList.size(); j++) {
                    if (addonsInfo.get(pos).getAddonsid().equals(addonsList.get(j).getAddonsid()) &&
                            addonsInfo.get(pos).getAddOnName().equals(addonsList.get(j).getAddOnName())) {
                        addonsList.get(j).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                        SharedPref.write("addOnslist", new Gson().toJson(addonsList));
                    }
                }
            }
        });


        holder.binding.minusTv.setOnClickListener(view -> {
            addOnsCounter = Integer.parseInt(holder.binding.quantityTv.getText().toString());
            if (addOnsCounter > 1) {
                addOnsCounter--;
                holder.binding.quantityTv.setText(String.valueOf(addOnsCounter));
                holder.binding.addonPriceTv.setText(String.valueOf(Double.parseDouble(addonsInfo.get(pos).getAddonsprice()) * addOnsCounter));
                if (holder.binding.checkBox.isChecked()) {
                    foodPrice = Double.parseDouble(totalPriceTv.getText().toString());
                    double itemPrice = foodPrice - Double.parseDouble(addonsInfo.get(pos).getAddonsprice());
                    totalPriceTv.setText(String.valueOf(itemPrice));
                    addonsInfo.get(pos).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                    Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
                    addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                    for (int j = 0; j < addonsList.size(); j++) {
                        if (addonsInfo.get(pos).getAddonsid().equals(addonsList.get(j).getAddonsid()) &&
                                addonsInfo.get(pos).getAddOnName().equals(addonsList.get(j).getAddOnName())) {
                            addonsList.get(j).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                            SharedPref.write("addOnslist", new Gson().toJson(addonsList));
                        }
                    }
                }
            }
        });

    }


    @Override
    public int getItemCount() {
        return addonsInfo.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private final DesignCartAddonsBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DesignCartAddonsBinding.bind(view);
        }
    }
}

