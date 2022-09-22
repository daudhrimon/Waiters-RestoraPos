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
    private List<Addonsinfo> items;
    private List<Addonsinfo> items2 = new ArrayList<>();
    private Context context;
    Double total;
    Double sum = 0.0;
    private String checkName;
    private String mainPrice;
    private double foodPrice;
    private int checker;
    private int addOnsCounter=1;
    private double addOnPrice;
    private int addOnsChecker = 0;
    private TextView variantPriceTV;

    public AddOnsItemAdapter(Context applicationContext, List<Addonsinfo> itemArrayList, String s, TextView variantPriceTV) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.mainPrice = s;
        this.variantPriceTV = variantPriceTV;
    }

    public AddOnsItemAdapter(Context applicationContext, List<Addonsinfo> itemArrayList) {
        this.context = applicationContext;
        this.items = itemArrayList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_food_cart_item_addons, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder holder, final int i) {

        holder.binding.productName.setText(items.get(i).getAddOnName());
        holder.binding.unitPriceTv.setText(items.get(i).getAddonsprice());
        holder.binding.quantityTv.setText("" + addOnsCounter);
        final int count = Integer.parseInt(String.valueOf(holder.binding.quantityTv.getText()));
        if (!items.get(i).getAddonsprice().equals("")) {
            total = Double.parseDouble(items.get(i).getAddonsprice()) * count;
        }
        FoodActivity.addOnsChecker = addOnsChecker;
        holder.binding.totalPriceTv.setText(String.valueOf(total));

        holder.binding.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (holder.binding.checkBox.isChecked()) {
                    checker++;
                    SharedPref.write("AddOnsCheck", "" + checker);
                    foodPrice = Double.parseDouble(variantPriceTV.getText().toString());
                    double itemPice = foodPrice + Double.parseDouble(holder.binding.unitPriceTv.getText().toString());
                    variantPriceTV.setText("" + itemPice);
                    items.get(i).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));

                    Addonsinfo addonsinfo = new Addonsinfo(items.get(i).getAddonsid(),
                            items.get(i).getAddOnName(), holder.binding.totalPriceTv.getText().toString(),
                            Integer.parseInt(holder.binding.quantityTv.getText().toString()));

                    if (items2.size()==0) {
                        items2.add(addonsinfo);
                        SharedPref.write("addOnslist", new Gson().toJson(items2));
                    } else {
                        Type type = new TypeToken<List<Addonsinfo>>() {
                        }.getType();
                        items2 = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                        items2.add(addonsinfo);
                        SharedPref.write("addOnslist", new Gson().toJson(items2));
                    }
                    addOnsChecker = 1;
                    FoodActivity.addOnsChecker = addOnsChecker;
                }
                if (!holder.binding.checkBox.isChecked()) {
                    checker--;
                    SharedPref.write("AddOnsCheck", "" + checker);
                    foodPrice = Double.parseDouble(variantPriceTV.getText().toString());
                    Double addsonPrice = Double.valueOf(holder.binding.unitPriceTv.getText().toString());
                    variantPriceTV.setText("" + (foodPrice - addsonPrice));

                    Type type = new TypeToken<List<Addonsinfo>>() {
                    }.getType();

                    items2 = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                    for (int j = 0; j < items2.size(); j++) {
                        if (items2.get(j).getAddonsid().equals(items.get(i).getAddonsid())) {
                            items2.remove(j);
                            SharedPref.write("addOnslist", new Gson().toJson(items2));
                        }
                    }
                    if (items2.size()==0){
                        addOnsChecker =0;
                        FoodActivity.addOnsChecker = addOnsChecker;
                    }
                }
            }
        });



        holder.binding.plusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodPrice = Double.parseDouble(variantPriceTV.getText().toString());
                addOnsCounter++;
                holder.binding.quantityTv.setText("" + addOnsCounter);

                holder.binding.totalPriceTv.setText("" + Double.parseDouble(items.get(i).getAddonsprice()) * addOnsCounter);

                if (holder.binding.checkBox.isChecked()) {
                    double itemPice = foodPrice + Double.parseDouble(items.get(i).getAddonsprice());
                    variantPriceTV.setText("" + itemPice);
                    items.get(i).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                    Type type = new TypeToken<List<Addonsinfo>>() {
                    }.getType();
                    items2 = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);

                    for (int j = 0; j < items2.size(); j++) {
                        if (items.get(i).getAddonsid().equals(items2.get(j).getAddonsid()) &&
                                items.get(i).getAddOnName().equals(items2.get(j).getAddOnName())) {
                            items2.get(j).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                            SharedPref.write("addOnslist", new Gson().toJson(items2));
                        }
                    }
                }

            }
        });



        holder.binding.minusTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodPrice = Double.parseDouble(variantPriceTV.getText().toString());
                double price = Double.parseDouble(holder.binding.totalPriceTv.getText().toString());
                addOnsCounter = Integer.parseInt(holder.binding.quantityTv.getText().toString());
                addOnPrice = Double.parseDouble(items.get(i).getAddonsprice());

                if (addOnsCounter > 1) {
                    addOnsCounter--;
                    holder.binding.quantityTv.setText("" + addOnsCounter);

                    holder.binding.totalPriceTv.setText("" + (price - addOnPrice));

                    if (holder.binding.checkBox.isChecked()) {

                        double itemPrice = foodPrice - addOnPrice;
                        variantPriceTV.setText("" + itemPrice);
                        items.get(i).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                        Type type = new TypeToken<List<Addonsinfo>>() {
                        }.getType();
                        items2 = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);

                        for (int j = 0; j < items2.size(); j++) {
                            if (items.get(i).getAddonsid().equals(items2.get(j).getAddonsid()) &&
                                    items.get(i).getAddOnName().equals(items2.get(j).getAddOnName())) {
                                items2.get(j).setAddonsquantity(Integer.parseInt(holder.binding.quantityTv.getText().toString()));
                                SharedPref.write("addOnslist", new Gson().toJson(items2));
                            }
                        }
                    }
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private DesignFoodCartItemAddonsBinding binding;

        public ViewHolder(View view) {
            super(view);
            binding = DesignFoodCartItemAddonsBinding.bind(view);
        }
    }
}

