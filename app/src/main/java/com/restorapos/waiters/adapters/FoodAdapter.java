package com.restorapos.waiters.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignFoodsItemBinding;
import com.restorapos.waiters.interfaces.FoodDialogInterface;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.utils.SharedPref;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
    private List<Foodinfo> foodItems;
    private Context context;
    private FoodDialogInterface foodDialogInterface;


    public FoodAdapter(Context applicationContext, List<Foodinfo> foodItems, FoodDialogInterface foodDialogInterface) {
        SharedPref.init(context);
        this.context = applicationContext;
        this.foodItems = foodItems;
        this.foodDialogInterface = foodDialogInterface;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_foods_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(final ViewHolder holder, int pos) {

        String url = foodItems.get(pos).getProductImage();
        if (url != null) {
            Glide.with(context).load(url).into(holder.fBinding.categoryImage);
        }

        holder.fBinding.categoryName.setText(foodItems.get(pos).getProductName());
        holder.fBinding.price.setText(SharedPref.read("CURRENCY", "") + " " + foodItems.get(pos).getPrice());
        holder.fBinding.notes.setText(foodItems.get(pos).getDestcription());
        holder.fBinding.varient.setText(foodItems.get(pos).getVariantName());


        if (foodItems.get(pos).getOfferIsavailable() != null) {
            if (foodItems.get(pos).getOfferIsavailable().equals("1") && isOfferAvailable2(foodItems.get(pos).getOfferstartdate(), foodItems.get(pos).getOfferendate())) {
                holder.fBinding.offerLay.setVisibility(View.VISIBLE);
                holder.fBinding.offerRate.setText(foodItems.get(pos).getOffersRate());
            }
        }

           /* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toasty.success(context,items.get(pos).getProductId(),Toasty.LENGTH_SHORT).show();
                }
            });*/




        holder.itemView.setOnClickListener(v -> {

            if (holder.fBinding.selectedMark.getVisibility()==View.VISIBLE){
                holder.fBinding.selectedMark.setVisibility(View.GONE);
            }else{
                holder.fBinding.selectedMark.setVisibility(View.VISIBLE);

                foodDialogInterface.onFoodItemClick(context,foodItems.get(pos),holder.fBinding.selectedMark);

                /*if (foodtasks.size() == 0) {
                    if (items.get(pos).getAddons().equals(1) || itemss2.size() > 0) {
                        List<Addonsinfo> addons = items.get(pos).getAddonsinfo();

                        FoodCartDialog(addons, pos, items.get(pos).getVariantid(), items.get(pos).getVariantName(),holder.fBinding.qtyShowTv,items);

                    } else {

                        insertFood(items.get(pos));
                        Gson g = new Gson();
                        int count = items.get(pos).quantity;
                        count++;
                        items.get(pos).quantity = count;

                        //holder.qtyShowTv.setText(String.valueOf(items.get(pos).quantity));
                        List<Foodinfo> orderedItems = new ArrayList<>();
                        for (Foodinfo food : items) {
                            if (food.quantity > 0) {
                                orderedItems.add(food);
                            }
                        }
                        items.get(pos).setDestcription("");
                        Data data = new Data();
                        data.setFoodinfo(orderedItems);
                        String foods = g.toJson(data);

                        items.get(pos).quantity = 0;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFoodCart();
                            }
                        }, 1000);

                    }
                }
                else if (foodtasks.size() > 0 && itemss2.size() > 0) {

                    if (items.get(pos).getAddons().equals(1) || itemss2.size() > 0) {
                        List<Addonsinfo> addons = items.get(pos).getAddonsinfo();
                            *//*Gson gson = new Gson();
                            String string = gson.toJson(addons);*//*

                        FoodCartDialog(addons, pos, items.get(pos).getVariantid(), items.get(0).getVariantName(), holder.fBinding.qtyShowTv, items);

                    } else {

                        insertFood(items.get(pos));
                        Gson g = new Gson();
                        int count = items.get(pos).quantity;
                        count++;
                        items.get(pos).quantity = count;
                        List<Foodinfo> orderedItems = new ArrayList<>();
                        for (Foodinfo food : items) {
                            if (food.quantity > 0) {
                                orderedItems.add(food);
                            }
                        }
                        items.get(pos).setDestcription("");
                        Data data = new Data();
                        data.setFoodinfo(orderedItems);
                        String foods = g.toJson(data);

                        items.get(pos).quantity = 0;
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                getFoodCart();
                            }
                        }, 1000);

                    }
                }
                try {
                    if (holder.fBinding.qtyShowTv.getVisibility() == View.VISIBLE) {
                        String list = items.get(pos).getProductId() + items.get(pos).getVariantid();
                        for (int t = 0; t < foodtasks.size(); t++) {
                            String list1 = foodtasks.get(t).getProductId() + foodtasks.get(t).getVariantid();

                            if (list.equals(list1)) {

                                deleteFood(foodtasks.get(t));
                            }
                        }
                    } else {
                        Log.d("ooo", "onClick: ");
                        if (items.get(pos).getAddons().equals(1)) {
                            List<Addonsinfo> addons = items.get(pos).getAddonsinfo();
                            Gson gson = new Gson();
                            String string = gson.toJson(addons);

                            FoodCartDialog(addons, pos, items.get(pos).getVariantid(), items.get(0).getVariantName(), holder.fBinding.qtyShowTv, items);
                        } else {

                            insertFood(items.get(pos));
                            holder.fBinding.qtyShowTv.setVisibility(View.VISIBLE);
                            Gson g = new Gson();
                            int count = items.get(pos).quantity;
                            count++;
                            items.get(pos).quantity = count;
                            List<Foodinfo> orderedItems = new ArrayList<>();
                            for (Foodinfo food : items) {
                                if (food.quantity > 0) {
                                    orderedItems.add(food);
                                }
                            }
                            items.get(pos).setDestcription("");
                            Data data = new Data();
                            data.setFoodinfo(orderedItems);
                            String foods = g.toJson(data);


                            items.get(pos).quantity = 0;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getFoodCart();
                                }
                            }, 1000);

                        }
                    }
                }
                catch (Exception ignored) {*//**//*}*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return foodItems.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
        private DesignFoodsItemBinding fBinding;
        public ViewHolder(View view) {
            super(view);
            fBinding = DesignFoodsItemBinding.bind(view);
        }
    }

    private boolean isOfferAvailable2(String offerstartdate, String offerendate) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date strDate = null;
        Date endDate = null;
        try {
            strDate = sdf.parse(offerstartdate);
            endDate = sdf.parse(offerendate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() >= strDate.getTime() &&
                System.currentTimeMillis() <= endDate.getTime()) {
            Log.wtf("TRUE","");
            return true;
        } else {
            Log.wtf("False","");
            return false;
        }
    }






        /*public void FoodCartDialog(List<Addonsinfo> addonsinfoList, final int pos, String variantId, String variantName,
                                   ImageView qtyShowTv, List<Foodinfo> list) {
            SharedPref.init(context);

            if (addonsinfoList == null) {
                addonsinfoList = new ArrayList<>();
            }

            final Dialog dialog = new Dialog(context);
            View view = LayoutInflater.from(context).inflate(R.layout.food_cart_dialog,null);
            FoodCartDialogBinding dBinding = FoodCartDialogBinding.bind(view);
            dBinding.addonsHeader.setVisibility(View.GONE);
            dialog.setContentView(view);

            String productName = "",productId = "";
            List<Varientlist> varientlists = new ArrayList<>();
            List<String> variantnames = new ArrayList<>();

            quantity = dBinding.quantityFromUser.getText().toString();
            countNow = Integer.parseInt(quantity);

            Log.d("checkItemss2",new Gson().toJson(itemss2));
            Log.d("checkItemss3",new Gson().toJson(list));


            if (variantId.contains(list.get(pos).getVariantid())) {
                variantname = list.get(pos).getVariantName();
                variantid = list.get(pos).getVariantid();
                productName = list.get(pos).getProductName();
                productId = list.get(pos).getProductId();
                variantPric = list.get(pos).getPrice();
                varientlists = list.get(pos).getVarientlist();
                Log.d("detailsCheck", "pname: " + productName + "pPrice " + variantPric + "list " + new Gson().toJson(varientlists));
                dBinding.variantPriceTV.setText(variantPric);
                dBinding.variantNameTV.setText(productName);
                for (int j = 0; j < varientlists.size(); j++) {
                    variantnames.add(varientlists.get(j).getMultivariantName());

                }
                if (variantnames.size()==1){
                    variantnames.clear();
                    variantnames.add(variantName);
                }
                Log.d("variantCheck", String.valueOf(variantnames));


                dBinding.variantSpinner.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                        variantnames));
            }
            else {
                productName = list.get(pos).getProductName();
                variantPric = list.get(pos).getPrice();
                varientlists = list.get(pos).getVarientlist();
                dBinding.variantPriceTV.setText(variantPric);
                dBinding.variantNameTV.setText(productName);
                Log.d("detailsCheck", "pname: " + productName + "pPrice " + variantPric + "list " + new Gson().toJson(varientlists));
            }



            dBinding.plusBtn.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    countNow++;
                    dBinding.quantityFromUser.setText(String.valueOf(countNow));
                    double price =  Double.parseDouble(variantPric) * countNow;
                    dBinding.variantPriceTV.setText(""+price);
                }
            });



            dBinding.minusBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (countNow == 1) {
                        return;
                    } else {
                        countNow--;
                        dBinding.quantityFromUser.setText(String.valueOf(countNow));
                        double priceReduce = Double.parseDouble(variantPric);
                        double price =  Double.parseDouble(dBinding.variantPriceTV.getText().toString()) - priceReduce;
                        dBinding.variantPriceTV.setText(""+price);
                    }

                }
            });


            List<Varientlist> finalVarientlists = varientlists;
            Log.d("checkvarientId",new Gson().toJson(finalVarientlists));



            dBinding.variantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selecTedVariants = dBinding.variantSpinner.getSelectedItem().toString();
                    for (int j = 0; j < finalVarientlists.size(); j++) {
                        if (selecTedVariants.equals(finalVarientlists.get(j).getMultivariantName())){
                            variantid = finalVarientlists.get(j).getMultivariantid();
                            Log.d("checkvarientId",variantid);
                        }

                    }
                    for (int k = 0; k < finalVarientlists.size(); k++) {
                        if (finalVarientlists.get(k).getMultivariantName() == selecTedVariants) {
                            if (!finalVarientlists.get(k).getMultivariantPrice().isEmpty()) {
                                Log.d("variantprice", variantPric);
                                dBinding.variantPriceTV.setText(finalVarientlists.get(k).getMultivariantPrice());
                                variantPric = finalVarientlists.get(k).getMultivariantPrice();
                            } else if (!finalVarientlists.get(k).getMultivariantName().isEmpty()) {
                                variantname = finalVarientlists.get(k).getMultivariantName();
                                variantid = finalVarientlists.get(k).getMultivariantid();
                                Log.d("checkVarientId",variantId+","+variantName);

                            } else if (!finalVarientlists.get(k).getMultivariantid().isEmpty()) {
                                variantid = finalVarientlists.get(k).getMultivariantid();


                            }

                        }
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });




            dBinding.recyclerDialog.setLayoutManager(new LinearLayoutManager(context));



            try {
                Log.d("addonslisize", "" + addonsinfoList.size());
                addOnsitems = addonsinfoList;


                if (addOnsitems.size() == 0) {
                    dBinding.recyclerDialog.setVisibility(View.GONE);
                }
                else {
                    dBinding.addonsHeader.setVisibility(View.VISIBLE);
                    dBinding.recyclerDialog.setAdapter(new AddOnsItemAdapter(context, addOnsitems,dBinding.variantPriceTV.getText().toString(),dBinding.variantPriceTV));
                }
            }
            catch (Exception e) {
            }


            SharedPref.write("name", "");
            SharedPref.write("SUM", "");


            String finalProductName = productName;
            String finalProductId = productId;





            dBinding.addToCartBtn.setOnClickListener(v -> {
                List<Addonsinfo> addonsinfos = new ArrayList<>();
                Gson g = new Gson();
                int counts = this.items.get(pos).quantity;
                counts++;
                quantity = dBinding.quantityFromUser.getText().toString();
                if (orderQuantity == 0) {
                    orderQuantity = Integer.parseInt(quantity);
                } else {
                    orderQuantity = Integer.parseInt(quantity) + orderQuantity;
                }
                Log.d("quantitycheck", "" + quantity);


                if(items.get(pos).getProductName() == finalProductName &&
                        items.get(pos).getProductId() == finalProductId &&
                        items.get(pos).getVariantName() == variantName &&
                        items.get(pos).getVariantid() == variantId){


                    this.items.get(pos).setVariantName(variantname);
                    this.items.get(pos).setVariantid(variantid);
                    this.items.get(pos).setPrice(String.valueOf(Double.parseDouble(this.items.get(pos).getPrice())+Double.parseDouble(variantPric)));
                    this.items.get(pos).quantitys += orderQuantity;
                    this.items.get(pos).quantity += orderQuantity;
                    this.items.get(pos).setQuantity( this.items.get(pos).getQuantity()+Integer.parseInt(quantity));
                    this.items.get(pos).setVariantName(selecTedVariants);
                    this.items.get(pos).setAddons(list.get(pos).getAddons());

                    Log.wtf("ITEMS",items.toString());
                }

                *//*this.items.get(pos).setPrice(variantPric);
                this.items.get(pos).setVariantName(variantname);
                this.items.get(pos).setVariantid(variantid);
                this.items.get(pos).quantitys = orderQuantity;
                this.items.get(pos).quantity = orderQuantity;
                this.items.get(pos).setQuantity(Integer.parseInt(quantity));
                this.items.get(pos).setVariantName(selecTedVariants);
                this.items.get(pos).setAddons(list.get(pos).getAddons());
                Log.d("addOnsChecker",""+addOnsChecker);*//*

                if (addOnsChecker==1){
                    if (list.get(pos).getAddons() == 1){
                        if (!SharedPref.read("addOnslist","").equals("")){
                            Type type = new TypeToken<List<Addonsinfo>>() {}.getType();
                            addonsinfos = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                            this.items.get(pos).setAddonsinfo(addonsinfos);
                        }
                    }

                } else {
                    this.items.get(pos).setAddons(0);
                }

                orderedItems.clear();
                orderedItems.removeAll(orderedItems);
                for (Foodinfo foodss : this.items)
                    if (foodss.quantity > 0) {
                        Log.d("foodss", "foods" + new Gson().toJson(foodss));
                        orderedItems.add(foodss);
                    }

                this.items.get(pos).setDestcription("");
                Data data = new Data();
                data.setFoodinfo(orderedItems);
                String updateFoods = g.toJson(orderedItems);
                String foods = g.toJson(data);
                Log.d("ppok", "onClick: " + updateFoods);

                this.items.get(pos).quantity = 0;

                insertFood(this.items.get(pos));

                Toasty.success(FoodActivity.this,"Item added to cart").show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() { getFoodCart(); }
                }, 1000);

                dialog.dismiss();

                qtyShowTv.setVisibility(View.GONE);
                //getAllFoodItem();
            });


            List<Addonsinfo> finalAddonsinfoList1 = addonsinfoList;



            dBinding.closeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    qtyShowTv.setVisibility(View.GONE);
                }
            });





            dBinding.addMultipleBtn.setOnClickListener(v -> {
                List<Addonsinfo> addonsinfos = new ArrayList<>();
                Gson g = new Gson();
                int counts = this.items.get(pos).quantity;
                counts++;
                quantity = dBinding.quantityFromUser.getText().toString();
                if (orderQuantity == 0) {
                    orderQuantity = Integer.parseInt(quantity);
                } else {
                    orderQuantity = Integer.parseInt(quantity) + orderQuantity;
                }

                Log.d("quantitycheck", "" + quantity);

                this.items.get(pos).setPrice(variantPric);
                this.items.get(pos).setVariantName(variantname);
                this.items.get(pos).setVariantid(variantid);
                this.items.get(pos).quantitys = orderQuantity;
                this.items.get(pos).quantity = orderQuantity;
                this.items.get(pos).setQuantity(Integer.parseInt(quantity));
                this.items.get(pos).setVariantName(selecTedVariants);
                this.items.get(pos).setAddons(list.get(pos).getAddons());

                if (addOnsChecker==1){
                    if (list.get(pos).getAddons()==1){
                        if (!SharedPref.read("addOnslist","").equals("")){
                            Type type = new TypeToken<List<Addonsinfo>>() {}.getType();
                            addonsinfos = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                            this.items.get(pos).setAddonsinfo(addonsinfos);

                        }
                    }

                }
                else{
                    this.items.get(pos).setAddons(0);
                }

                orderedItems.clear();
                orderedItems.removeAll(orderedItems);
                for (Foodinfo foodss : this.items)
                    if (foodss.quantity > 0) {
                        Log.d("foodss", "foods" + new Gson().toJson(foodss));
                        orderedItems.add(foodss);

                    }

                this.items.get(pos).setDestcription("");
                Data data = new Data();
                data.setFoodinfo(orderedItems);
                String updateFoods = g.toJson(orderedItems);
                String foods = g.toJson(data);
                Log.d("ppok", "onClick: " + updateFoods);

                this.items.get(pos).quantity = 0;

                insertFood(this.items.get(pos));

                Toasty.success(FoodActivity.this,"Item added to cart").show();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodCart();
                    }
                }, 1000);

                //getAllFoodItem();
            });

            dialog.show();
            int width = getResources().getDisplayMetrics().widthPixels;
            Window win = dialog.getWindow();
            win.setLayout((9*width)/10, WindowManager.LayoutParams.WRAP_CONTENT);
            win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/
}
