package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.restorapos.waiters.R;
import com.restorapos.waiters.model.appModel.AppFoodInfo;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Data;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class FoodsAdapter extends RecyclerView.Adapter<FoodsAdapter.ViewHolder> {
    private List<Foodinfo> items;
    private List<AppFoodInfo> itemss;
    private Context context;
    WaitersService waitersService;
    List<Addonsinfo> addOnsitems = null;

    public FoodsAdapter(Context applicationContext, List<Foodinfo> itemArrayList, List<AppFoodInfo> itemss) {
        SharedPref.init(context);
        this.context = applicationContext;
        this.items = itemArrayList;
        this.itemss = itemss;
        waitersService = AppConfig.getRetrofit(context).create(WaitersService.class);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_foods_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {

        viewHolder.categoryName.setText(items.get(position).getProductName());
        viewHolder.price.setText(SharedPref.read("CURRENCY","")+ items.get(position).getPrice());
        viewHolder.notes.setText(items.get(position).getDestcription());

        String url = items.get(position).getProductImage();
        itemss = new ArrayList<>();
        if (url != null) {
            Glide.with(context).load(url).into(viewHolder.categoryImage);
        }
        try {
            String sellRequestBodyText = SharedPref.read("FOOD", "");
            Gson g = new Gson();
            Data appRequestBody = g.fromJson(sellRequestBodyText, Data.class);
            String list = items.get(position).getProductId() + "" + items.get(position).getVariantid();

            for (int t = 0; t < 10; t++) {
                String list1 = appRequestBody.getFoodinfo().get(t).getProductId() + "" + appRequestBody.getFoodinfo().get(t).getVariantid();
                Log.d("000", "onBindViewHolder: " + list + " " + list1);
                if (list.equals(list1)) {
                    Log.d("000", "onBindViewHolder: " + list + " " + list1);
                    Log.d("ooo", "onBind: " + list);
                    viewHolder.qtyShowTv.setVisibility(View.VISIBLE);
                }
            }

            Log.d("ooo", "onBindViewHolder: " + list);

        } catch (Exception e) {
        }

        viewHolder.layoutDesignFoodItem.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (items.get(position).getAddons().equals(1)) {
                    List<Addonsinfo> addons = items.get(position).getAddonsinfo();
                    Gson gson = new Gson();
                    String string = gson.toJson(addons);
                    Log.d("ppppp", "onClick: " + string);
                    CUstomAlertDialog(addons, position);
                } else {
                    Gson g=new Gson();
                    int count = items.get(position).quantity;
                    count++;
                    items.get(position).quantity = count;
                    viewHolder.qtyShowTv.setText(String.valueOf(items.get(position).quantity));
                    List<Foodinfo> orderedItems = new ArrayList<>();
                    for (Foodinfo food : items) {
                        if (food.quantity > 0) {
                            orderedItems.add(food);
                        }

                    }
                    items.get(position).setDestcription("");
                    Data data = new Data();
                    data.setFoodinfo(orderedItems);
                    String foods = g.toJson(data);
                    Log.d("pok", "ArrayMAde: " + foods);

                }
                //Toast.makeText(context, items.get(position).getProductName(), Toast.LENGTH_SHORT).show();
            }

        });

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName, price, notes, qtyShowTv;
        ImageView categoryImage;
        LinearLayout layoutDesignFoodItem;

        public ViewHolder(View view) {
            super(view);
            categoryImage = view.findViewById(R.id.categoryImageId);
            categoryName = view.findViewById(R.id.categoryNameId);
            price = view.findViewById(R.id.categoryPriceId);
            notes = view.findViewById(R.id.categoryNotesId);
            qtyShowTv = view.findViewById(R.id.quantityShowId);
            layoutDesignFoodItem = view.findViewById(R.id.layoutDesignFoodItemId);

        }
    }

    private void ArrayMAde() {
        Gson g = new Gson();
        List<AppFoodInfo> orderedItems = new ArrayList<>();
        for (AppFoodInfo food : itemss) {
            if (food.quantity > 0) {
                orderedItems.add(food);
            }
        }
        String foods = g.toJson(orderedItems);
        Log.d("pok", "ArrayMAde: " + foods);


    }

    public void CUstomAlertDialog(final List<Addonsinfo> addonsinfoList, final int pos) {
        SharedPref.init(context);
        final AlertDialog.Builder builder = new AlertDialog.Builder(context);

        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.food_cart_dialog, null);
        builder.setView(view2);
        RecyclerView rv = view2.findViewById(R.id.recyclerDialogId);
        Button ok = view2.findViewById(R.id.okId);
        rv.setLayoutManager(new LinearLayoutManager(context));
        try {

            addOnsitems = addonsinfoList;

            rv.setAdapter(new AddOnsItemAdapter(context, addOnsitems));
        } catch (Exception e) {
        }
        final AlertDialog alert = builder.create();

        SharedPref.write("name", "");
        Log.d("ppppp", "CUstomAlertDialog: " + addonsinfoList.get(0).getAddOnName());
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson g = new Gson();

                int counts = items.get(pos).quantity;
                counts++;
                items.get(pos).quantity = counts;
                List<Foodinfo> orderedItems = new ArrayList<>();
                for (Foodinfo foodss : items)
                    if (foodss.quantity > 0) {
                        orderedItems.add(foodss);
                        List<Addonsinfo> orderedItemss = new ArrayList<>();
                        for (Addonsinfo foods : addonsinfoList) {
                            if (foods.addonsquantity > 0) {
                                orderedItemss.add(foods);
                            }
                        }

                    }

                items.get(pos).addOnsName = SharedPref.read("name", "");
                items.get(pos).addOnsTotal = Double.parseDouble(SharedPref.read("SUM", ""));
                items.get(pos).setDestcription("");
                Data data = new Data();
                data.setFoodinfo(orderedItems);
                String foods = g.toJson(data);
                Log.d("pok", "ArrayMAde: " + foods);
                SharedPref.write("FOOD", foods);
                alert.dismiss();
            }
        });
        alert.show();

    }
}

