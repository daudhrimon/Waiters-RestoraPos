package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.activities.FoodItemActivity;
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

    public AddOnsItemAdapter(Context applicationContext, List<Addonsinfo> itemArrayList, String s) {
        this.context = applicationContext;
        this.items = itemArrayList;
        this.mainPrice = s;
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
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {

        viewHolder.productName.setText(items.get(i).getAddOnName());
        viewHolder.unitPrice.setText(items.get(i).getAddonsprice());
        viewHolder.qty.setText("" + addOnsCounter);
        final int count = Integer.parseInt(String.valueOf(viewHolder.qty.getText()));
        if (!items.get(i).getAddonsprice().equals("")) {
            total = Double.parseDouble(items.get(i).getAddonsprice()) * count;
        }
        FoodItemActivity.addOnsChecker = addOnsChecker;
        viewHolder.totalPrice.setText(String.valueOf(total));

        viewHolder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (viewHolder.checkBox.isChecked()) {
                    checker++;
                    SharedPref.write("AddOnsCheck", "" + checker);
                    foodPrice = Double.parseDouble(FoodItemActivity.variantPriceTV.getText().toString());
                    double itemPice = foodPrice + Double.parseDouble(viewHolder.unitPrice.getText().toString());
                    FoodItemActivity.variantPriceTV.setText("" + itemPice);
                    items.get(i).setAddonsquantity(Integer.parseInt(viewHolder.qty.getText().toString()));

                    Addonsinfo addonsinfo = new Addonsinfo(items.get(i).getAddonsid(),
                            items.get(i).getAddOnName(), viewHolder.totalPrice.getText().toString(),
                            Integer.parseInt(viewHolder.qty.getText().toString()));

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
                    FoodItemActivity.addOnsChecker = addOnsChecker;
                }
                if (!viewHolder.checkBox.isChecked()) {
                    checker--;
                    SharedPref.write("AddOnsCheck", "" + checker);
                    foodPrice = Double.parseDouble(FoodItemActivity.variantPriceTV.getText().toString());
                    Double addsonPrice = Double.valueOf(viewHolder.unitPrice.getText().toString());
                    FoodItemActivity.variantPriceTV.setText("" + (foodPrice - addsonPrice));

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
                        FoodItemActivity.addOnsChecker = addOnsChecker;
                    }
                }
            }
        });

        viewHolder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodPrice = Double.parseDouble(FoodItemActivity.variantPriceTV.getText().toString());
                addOnsCounter++;
                viewHolder.qty.setText("" + addOnsCounter);

                viewHolder.totalPrice.setText("" + Double.parseDouble(items.get(i).getAddonsprice()) * addOnsCounter);

                if (viewHolder.checkBox.isChecked()) {
                    double itemPice = foodPrice + Double.parseDouble(items.get(i).getAddonsprice());
                    FoodItemActivity.variantPriceTV.setText("" + itemPice);
                    items.get(i).setAddonsquantity(Integer.parseInt(viewHolder.qty.getText().toString()));
                    Type type = new TypeToken<List<Addonsinfo>>() {
                    }.getType();
                    items2 = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);

                    for (int j = 0; j < items2.size(); j++) {
                        if (items.get(i).getAddonsid().equals(items2.get(j).getAddonsid()) &&
                                items.get(i).getAddOnName().equals(items2.get(j).getAddOnName())) {
                            items2.get(j).setAddonsquantity(Integer.parseInt(viewHolder.qty.getText().toString()));
                            SharedPref.write("addOnslist", new Gson().toJson(items2));
                        }
                    }
                }

            }
        });

        viewHolder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                foodPrice = Double.parseDouble(FoodItemActivity.variantPriceTV.getText().toString());
                double price = Double.parseDouble(viewHolder.totalPrice.getText().toString());
                addOnsCounter = Integer.parseInt(viewHolder.qty.getText().toString());
                addOnPrice = Double.parseDouble(items.get(i).getAddonsprice());

                if (addOnsCounter > 1) {
                    addOnsCounter--;
                    viewHolder.qty.setText("" + addOnsCounter);

                    viewHolder.totalPrice.setText("" + (price - addOnPrice));

                    if (viewHolder.checkBox.isChecked()) {

                        double itemPrice = foodPrice - addOnPrice;
                        FoodItemActivity.variantPriceTV.setText("" + itemPrice);
                        items.get(i).setAddonsquantity(Integer.parseInt(viewHolder.qty.getText().toString()));
                        Type type = new TypeToken<List<Addonsinfo>>() {
                        }.getType();
                        items2 = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);

                        for (int j = 0; j < items2.size(); j++) {
                            if (items.get(i).getAddonsid().equals(items2.get(j).getAddonsid()) &&
                                    items.get(i).getAddOnName().equals(items2.get(j).getAddOnName())) {
                                items2.get(j).setAddonsquantity(Integer.parseInt(viewHolder.qty.getText().toString()));
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
        TextView productName, unitPrice, qty, totalPrice, plus, minus;
        CheckBox checkBox;

        public ViewHolder(View view) {
            super(view);
            productName = view.findViewById(R.id.productNameId);
            unitPrice = view.findViewById(R.id.unitPriceId);
            qty = view.findViewById(R.id.quantityId);
            totalPrice = view.findViewById(R.id.totalPriceId);
            plus = view.findViewById(R.id.plusId);
            minus = view.findViewById(R.id.minusId);
            checkBox = view.findViewById(R.id.checkId);


        }
    }
}

