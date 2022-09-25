package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.FoodActivity;
import com.restorapos.waiters.databinding.DesignFoodCartItemAddonsBinding;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class AddOnsItemAdapter extends RecyclerView.Adapter<AddOnsItemAdapter.ViewHolder> {
    private List<Addonsinfo> addonsInfo;
    private List<Addonsinfo> addonsList = new ArrayList<>();
    private Context context;
    private double total;
    private double foodPrice;
    private int checker;
    private int addOnsCounter = 1;
    private double addOnPrice;
    private int addOnsChecker = 0;
    private TextView totalPriceTV;

    public AddOnsItemAdapter(Context applicationContext, List<Addonsinfo> addonsInfo, String variantPrice, TextView totalPriceTV) {
        this.context = applicationContext;
        this.addonsInfo = addonsInfo;
        this.totalPriceTV = totalPriceTV;
    }

    public AddOnsItemAdapter(Context applicationContext, List<Addonsinfo> itemArrayList) {
        this.context = applicationContext;
        this.addonsInfo = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.design_food_cart_item_addons, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        holder.binding.productName.setText(addonsInfo.get(i).getAddOnName());
        holder.binding.unitPriceTv.setText(addonsInfo.get(i).getAddonsprice());
        holder.binding.quantityTv.setText(String.valueOf(addOnsCounter));
        final int count = Integer.parseInt(String.valueOf(holder.binding.quantityTv.getText()));

        if (!addonsInfo.get(i).getAddonsprice().equals("")) {
            total = Double.parseDouble(addonsInfo.get(i).getAddonsprice()) * count;
        }

        FoodActivity.addOnsChecker = addOnsChecker;
        holder.binding.addonPriceTv.setText(String.valueOf(total));



        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.binding.checkBox.isChecked()) {
                    checker++;

                    SharedPref.write("AddOnsCheck", "" + checker);

                    foodPrice = Double.parseDouble(totalPriceTV.getText().toString());
                    double addonPrice = Double.parseDouble(holder.binding.addonPriceTv.getText().toString());
                    totalPriceTV.setText(String.valueOf(foodPrice+addonPrice));
                    addonsInfo.get(i).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));

                    Addonsinfo tempAddons = new Addonsinfo(addonsInfo.get(i).getAddonsid(),
                            addonsInfo.get(i).getAddOnName(), holder.binding.addonPriceTv.getText().toString(),
                            Integer.parseInt(holder.binding.quantityTv.getText().toString()));

                    if (addonsList.size() != 0) {
                        Type type = new TypeToken<List<Addonsinfo>>() {}.getType();
                        addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                    }
                    addonsList.add(tempAddons);

                    SharedPref.write("addOnslist", new Gson().toJson(addonsList));

                    addOnsChecker = 1;
                    FoodActivity.addOnsChecker = addOnsChecker;
                }




                if (!holder.binding.checkBox.isChecked()) {
                    checker--;

                    SharedPref.write("AddOnsCheck", "" + checker);

                    foodPrice = Double.parseDouble(totalPriceTV.getText().toString());
                    double addonPrice = Double.parseDouble(holder.binding.addonPriceTv.getText().toString());
                    totalPriceTV.setText(String.valueOf(foodPrice-addonPrice));

                    Type type = new TypeToken<List<Addonsinfo>>() {
                    }.getType();

                    addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                    for (int j = 0; j < addonsList.size(); j++) {
                        if (addonsList.get(j).getAddonsid().equals(addonsInfo.get(i).getAddonsid())) {
                            addonsList.remove(j);
                            SharedPref.write("addOnslist", new Gson().toJson(addonsList));
                        }
                    }
                    if (addonsList.size()==0){
                        addOnsChecker =0;
                        FoodActivity.addOnsChecker = addOnsChecker;
                    }
                }
            }
        });



        holder.binding.plusTv.setOnClickListener(view -> {
            addOnsCounter = Integer.parseInt(holder.binding.quantityTv.getText().toString());
            foodPrice = Double.parseDouble(totalPriceTV.getText().toString());

            addOnsCounter++;
            holder.binding.quantityTv.setText(String.valueOf(addOnsCounter));

            holder.binding.addonPriceTv.setText(String.valueOf(Double.parseDouble(addonsInfo.get(i).getAddonsprice()) * addOnsCounter));

            if (holder.binding.checkBox.isChecked()) {
                double itemPrice = foodPrice + Double.parseDouble(addonsInfo.get(i).getAddonsprice());
                totalPriceTV.setText(String.valueOf(itemPrice));

                addonsInfo.get(i).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                Type type = new TypeToken<List<Addonsinfo>>() {
                }.getType();
                addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);

                for (int j = 0; j < addonsList.size(); j++) {
                    if (addonsInfo.get(i).getAddonsid().equals(addonsList.get(j).getAddonsid()) &&
                            addonsInfo.get(i).getAddOnName().equals(addonsList.get(j).getAddOnName())) {

                        addonsList.get(j).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                        SharedPref.write("addOnslist", new Gson().toJson(addonsList));
                    }
                }
            }
        });



        holder.binding.minusTv.setOnClickListener(view -> {
            addOnsCounter = Integer.parseInt(holder.binding.quantityTv.getText().toString());
            foodPrice = Double.parseDouble(totalPriceTV.getText().toString());
            double price = Double.parseDouble(holder.binding.addonPriceTv.getText().toString());
            addOnPrice = Double.parseDouble(addonsInfo.get(i).getAddonsprice());

            if (addOnsCounter > 1) {
                addOnsCounter--;
                holder.binding.quantityTv.setText(String.valueOf(addOnsCounter));

                holder.binding.addonPriceTv.setText(String.valueOf(price - addOnPrice));

                if (holder.binding.checkBox.isChecked()) {

                    double itemPrice = foodPrice - addOnPrice;
                    totalPriceTV.setText(String.valueOf(itemPrice));

                    addonsInfo.get(i).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                    Type type = new TypeToken<List<Addonsinfo>>() {
                    }.getType();
                    addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);

                    for (int j = 0; j < addonsList.size(); j++) {
                        if (addonsInfo.get(i).getAddonsid().equals(addonsList.get(j).getAddonsid()) &&
                                addonsInfo.get(i).getAddOnName().equals(addonsList.get(j).getAddOnName())) {
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
        private DesignFoodCartItemAddonsBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DesignFoodCartItemAddonsBinding.bind(view);
        }
    }
}

