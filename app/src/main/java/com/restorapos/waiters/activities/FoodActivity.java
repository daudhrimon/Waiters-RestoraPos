package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import com.google.gson.reflect.TypeToken;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.databinding.DesignSubCategoryItemBinding;
import com.restorapos.waiters.databinding.FoodCartDialogBinding;
import com.restorapos.waiters.adapters.AddOnsItemAdapter;
import com.restorapos.waiters.adapters.FoodAdapter;
import com.restorapos.waiters.databinding.ActivityFoodBinding;
import com.restorapos.waiters.interfaces.FoodDialogInterface;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Categoryinfo;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.model.foodlistModel.FoodlistResponse;
import com.restorapos.waiters.model.foodlistModel.Varientlist;
import com.restorapos.waiters.offlineDb.AppDatabase;
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity implements FoodDialogInterface {
    private ActivityFoodBinding binding;
    private String pCategoryId;
    private String userId;
    private WaitersService waitersService;
    private List<Foodinfo> foodTask;
    private final String TAG = "FoodActivity";
    private SpotsDialog progressDialog;
    private List<Foodinfo> foodItems;
    /////////////////////////////////
    private FoodDialogInterface foodDialogInterface;
    private FoodAdapter foodAdapter;
    private AppDatabase appDatabase;
    ////////////////////////////////
    public static int addOnsChecker = 0;
    private List<Varientlist> variantList;
    private String variantPrice = "";
    private String variantId = "";
    private String variantName = "";
    private float countNow = 1.0F;
    private double addonsTotal = 0.0;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SharedPref.init(this);

        waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        pCategoryId = getIntent().getStringExtra("CATEGORYID");
        userId = SharedPref.read("ID", "");
        appDatabase = DatabaseClient.getInstance(this).getAppDatabase();
        foodDialogInterface = this;
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();
        MainActivity.appBarDefault();

        //getAllFoodItemWithMultipleVariant();

        getUnit();

        getSubCategoryItem();

        getAllFoodItem();


        binding.viewCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodActivity.this, CartActivity.class));
            }
        });


        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });


        binding.searchEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customFilterList(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {/**/}
        });


    }


    private void setFoodCartHeaders() {
        binding.countTv.setText(SharedPref.read("CartCount", "0"));
        binding.totalTv.setText(SharedPref.read("CURRENCY", "") + " " + SharedPref.read("CartTotal", "0.0"));
    }


    private void customFilterList(String value) {
        List<Foodinfo> newList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            newList.clear();
            for (int i = 0; i < foodItems.size(); i++) {
                if (foodItems.get(i).getProductName().toLowerCase().contains(value.toLowerCase())) {
                    newList.add(foodItems.get(i));
                }
            }
            binding.foodRecycler.setAdapter(new FoodAdapter(FoodActivity.this, newList, foodDialogInterface));
        } else {
            binding.foodRecycler.setAdapter(new FoodAdapter(FoodActivity.this, foodItems, foodDialogInterface));
        }
    }





    /*private void getAllFoodItemWithMultipleVariant() {
        waitersService.getallfoodwithMultipleVariants(userId, pCategoryId).enqueue(new Callback<FoodlistResponse2>() {
            @Override
            public void onResponse(Call<FoodlistResponse2> call, Response<FoodlistResponse2> response) {
                Log.d("userId22", "" + userId + " " + pCategoryId);
                try {
                    Log.d("userId", "" + userId + " " + pCategoryId);
                    itemss2 = response.body().getData().getFoodinfo();
                    Log.d("RESPONSSSSitesm", "" + new Gson().toJson(itemss2));

                } catch (Exception e) {

                }
            }
            @Override
            public void onFailure(Call<FoodlistResponse2> call, Throwable t) {

            }
        });
    }*/


    public void getSubCategoryItem() {
        waitersService.foodSubCategory(userId, pCategoryId).enqueue(new Callback<FoodlistResponse>() {
            @Override
            public void onResponse(Call<FoodlistResponse> call, Response<FoodlistResponse> response) {
                try {
                    List<Categoryinfo> items = response.body().getData().getCategoryinfo();
                    SharedPref.write("RESTAURANT_VAT", response.body().getData().getRestaurantvat());
                    binding.subCategoryRecycler.setAdapter(new FoodSubCategoryAdapter(FoodActivity.this, items));
                } catch (Exception e) {
                    Log.d("qqq", "Exc: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<FoodlistResponse> call, Throwable t) {/**/}
        });
    }


    public void getAllFoodItem() {
        Log.d("TAG3", "getAllFoodItem: " + userId + "\t" + pCategoryId);

        waitersService.foodSubCategory(userId, pCategoryId).enqueue(new Callback<FoodlistResponse>() {
            @Override
            public void onResponse(Call<FoodlistResponse> call, Response<FoodlistResponse> response) {
                try {
                    if (response.body().getStatus().equals("success")) {
                        Log.d("TAG2", "onResponse: " + response.body().getData().getFoodinfo());
                        foodItems = response.body().getData().getFoodinfo();
                        if (binding.foodRecycler.getVisibility() == View.GONE) {
                            binding.foodRecycler.setVisibility(View.VISIBLE);
                            binding.emptyLay.setVisibility(View.GONE);
                        }
                        foodAdapter = new FoodAdapter(FoodActivity.this, foodItems, foodDialogInterface);
                        binding.foodRecycler.setAdapter(foodAdapter);
                        progressDialog.dismiss();
                    } else {
                        binding.foodRecycler.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    binding.foodRecycler.setVisibility(View.GONE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<FoodlistResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        binding.foodRecycler.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }


    private void getFoodItem(final String categoryId) {
        Log.d("ppp455", "getFoodItem: " + categoryId);
        waitersService.foodItem(userId, categoryId, pCategoryId).enqueue(new Callback<FoodlistResponse>() {
            @Override
            public void onResponse(Call<FoodlistResponse> call, Response<FoodlistResponse> response) {
                try {
                    if (response.body().getStatus().equals("success")) {
                        Log.d(TAG, "onResponse: " + response.body().getData().getFoodinfo());

                        Log.d("qqq33", "onResponse: " + userId + " " + categoryId + " " + pCategoryId);
                        foodItems = response.body().getData().getFoodinfo();

                        Log.d("ppp33", "onResponse: ppp" + new Gson().toJson(foodItems));
                        if (binding.foodRecycler.getVisibility() == View.GONE) {
                            binding.foodRecycler.setVisibility(View.VISIBLE);
                            binding.emptyLay.setVisibility(View.GONE);
                        }
                        binding.foodRecycler.setAdapter(new FoodAdapter(FoodActivity.this, foodItems, foodDialogInterface));
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        binding.foodRecycler.setVisibility(View.GONE);
                        binding.emptyLay.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    binding.foodRecycler.setVisibility(View.GONE);
                    binding.emptyLay.setVisibility(View.VISIBLE);
                    Log.d("ppp", "Exception: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<FoodlistResponse> call, Throwable t) {
                Log.d("ppp", "onFailure: " + t.getLocalizedMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                binding.foodRecycler.setVisibility(View.GONE);
                binding.emptyLay.setVisibility(View.VISIBLE);
            }
        });
    }


    private void getFoodCart() {
        try {
            double Total = 0.0;
            for (int i = 0; i < foodTask.size(); i++) {
                Total += (Double.parseDouble(foodTask.get(i).getPrice()) * foodTask.get(i).quantitys) + foodTask.get(i).getAddOnsTotal();
            }

            String cartTotal = String.valueOf(Double.valueOf(new DecimalFormat("##.##").format(Total)));

            SharedPref.write("CartCount", String.valueOf(foodTask.size()));
            SharedPref.write("CartTotal", cartTotal);

            setFoodCartHeaders();

        } catch (Exception ignored) {/**/}
    }


    public class FoodSubCategoryAdapter extends RecyclerView.Adapter<FoodSubCategoryAdapter.ViewHolder> implements Filterable {
        private final List<Categoryinfo> items;
        int row_index = 0;

        public FoodSubCategoryAdapter(Context applicationContext, List<Categoryinfo> itemArrayList) {
            SharedPref.init(applicationContext);
            this.items = itemArrayList;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_sub_category_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(ViewHolder holder, int pos) {
            holder.sBinding.subCategory.setText(items.get(pos).getName());
            holder.sBinding.subCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = pos;
                    notifyDataSetChanged();
                    progressDialog.show();
                    if (items.get(pos).getName().equals("All")) {
                        getAllFoodItem();
                    } else {
                        Log.d("hahahaah", items.get(pos).getCategoryID());
                        getFoodItem(items.get(pos).getCategoryID());
                    }
                }
            });
            if (row_index == pos) {
                holder.sBinding.selector.setVisibility(View.VISIBLE);

            } else {
                holder.sBinding.selector.setVisibility(View.GONE);
            }

        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public Filter getFilter() {
            return exampleFilter;
        }

        private Filter exampleFilter = new Filter() {
            @Override protected FilterResults performFiltering(CharSequence constraint) {
                List<Foodinfo> filteredList = new ArrayList<>();
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override protected void publishResults(CharSequence constraint, FilterResults results) {/**/}
        };

        public class ViewHolder extends RecyclerView.ViewHolder {
            private final DesignSubCategoryItemBinding sBinding;
            public ViewHolder(View view) {
                super(view);
                sBinding = DesignSubCategoryItemBinding.bind(view);
            }
        }
    }


    private void getUnit() {
        class GetProduct extends AsyncTask<Void, Void, List<Foodinfo>> {
            @Override protected List<Foodinfo> doInBackground(Void... voids) {
                List<Foodinfo> productList = appDatabase.taskDao().getAllUnit();
                foodTask = productList;
                return productList;
            }
            @Override protected void onPostExecute(List<Foodinfo> tasks) {
                super.onPostExecute(tasks);
                foodTask = tasks;
                getFoodCart();
            }
        }
        GetProduct gt = new GetProduct();
        gt.execute();
    }


    @Override
    public void onFoodItemClick(Context context, Foodinfo food, ImageView selectedMark) {
        variantList = food.getVarientlist();
        List<String> variantNameList = new ArrayList<>();
        boolean isCustomQty;

        if (food.getTotalvariant().equals("1")) {
            variantNameList.add(food.getVariantName());
        } else {
            for (int i = 0; i < variantList.size(); i++) {
                variantNameList.add(variantList.get(i).getMultivariantName());
            }
        }

        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.food_cart_dialog, null);
        FoodCartDialogBinding dBinding = FoodCartDialogBinding.bind(view);
        dBinding.addonsHeader.setVisibility(View.GONE);
        dialog.setContentView(view);

        if (food.getIscustqty() != null && food.getIscustqty().equals("1")) {
            dBinding.quantityEt.setText("1.0");
            isCustomQty = true;
        } else {
            dBinding.quantityEt.setText("1");
            isCustomQty = false;
        }

        dBinding.productName.setText(food.getProductName());

        dBinding.variantSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, variantNameList));

        if (food.getAddons().equals(1)) {
            dBinding.addonsHeader.setVisibility(View.VISIBLE);
            dBinding.dialogAddonsRv.setVisibility(View.VISIBLE);
        }

        dBinding.closeBtn.setOnClickListener(view1 -> {
            dialog.dismiss();
        });

        if (food.getIscustqty() != null && food.getIscustqty().equals("1")) {
            dBinding.quantityEt.setFocusable(true);
            dBinding.quantityEt.setFocusableInTouchMode(true);
        }

        if (food.getIscustomeprice() != null && food.getIscustomeprice().equals("1")) {
            dBinding.customPriceLay.setVisibility(View.VISIBLE);
        }

        dBinding.customPriceEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                double addonTotal = 0.0;
                if (!charSequence.toString().isEmpty() && !charSequence.toString().equals(".")) {
                    variantPrice = charSequence.toString();
                    try {
                        Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
                        List<Addonsinfo> addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                        if (addonsList.size() > 0) {
                            for (int a = 0; a < addonsList.size(); a++) {
                                addonTotal += (addonsList.get(a).getAddonsquantity() * Double.parseDouble(addonsList.get(a).getAddonsprice()));
                            }
                        }
                    } catch (Exception e) {/**/}
                    setCartDialogTotalPrice(dBinding,addonTotal);
                } else {
                    variantPrice = "0.0";
                    setCartDialogTotalPrice(dBinding,addonTotal);
                }
            } @Override public void afterTextChanged(Editable editable) {/**/}
        });

        dBinding.variantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {
                variantName = dBinding.variantSpinner.getSelectedItem().toString();
                if (food.getTotalvariant().equals("1")) {
                    variantId = food.getVariantid();
                    variantPrice = food.getPrice();
                    dBinding.customPriceEt.setText(variantPrice);
                } else {
                    variantId = variantList.get(pos).getMultivariantid();
                    variantPrice = variantList.get(pos).getMultivariantPrice();
                    dBinding.customPriceEt.setText(variantPrice);
                }
            } @Override public void onNothingSelected(AdapterView<?> adapterView) {/**/}
        });

        //Setting addon Adapter
        if (food.getAddons().equals(1)) {
            SharedPref.write("addOnslist", new Gson().toJson(new ArrayList<Addonsinfo>()));
            dBinding.dialogAddonsRv.setAdapter(new AddOnsItemAdapter(food.getAddonsinfo(), dBinding.totalPriceTv));
        }

        dBinding.quantityEt.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override public void onTextChanged(CharSequence text, int i, int i1, int i2) {
                double addonTotal = 0.0;
                if (!text.toString().isEmpty() && !text.toString().equals(".")) {
                    try {
                        Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
                        List<Addonsinfo> addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
                        if (addonsList.size() > 0) {
                            for (int a = 0; a < addonsList.size(); a++) {
                                addonTotal += (addonsList.get(a).getAddonsquantity() * Double.parseDouble(addonsList.get(a).getAddonsprice()));
                            }
                        }
                    } catch (Exception e) {/**/}
                    dBinding.totalPriceTv.setText(new DecimalFormat("#.##").format((Double.parseDouble(dBinding.quantityEt.getText().toString())*Double.parseDouble(variantPrice))+addonTotal));
                } else {
                    dBinding.totalPriceTv.setText("0.0");
                }
            } @Override public void afterTextChanged(Editable editable) {/**/}
        });

        dBinding.plusBtn.setOnClickListener(view1 -> {
            dBinding.quantityEt.clearFocus();
            dBinding.customPriceEt.clearFocus();
            if (dBinding.quantityEt.getText().toString().isEmpty()) {
                dBinding.quantityEt.setText("1");
            } else {
                countNow = Float.parseFloat(dBinding.quantityEt.getText().toString());
                countNow++;
                Log.wtf("A:KLAKLKA",""+isCustomQty);
                dBinding.quantityEt.setText(getQuantity(countNow,isCustomQty));
            }
        });

        dBinding.minusBtn.setOnClickListener(view1 -> {
            dBinding.quantityEt.clearFocus();
            dBinding.customPriceEt.clearFocus();
            if (dBinding.quantityEt.getText().toString().isEmpty()) {
                dBinding.quantityEt.setText("1");
            } else {
                countNow = Float.parseFloat(dBinding.quantityEt.getText().toString());
                if (countNow > 1) {
                    countNow--;
                    dBinding.quantityEt.setText(getQuantity(countNow,isCustomQty));
                }
            }
        });

        dBinding.customPriceBtn.setOnClickListener(view1 -> {
            if (dBinding.customPriceEt.getVisibility() == View.VISIBLE) {
                dBinding.customPriceEt.setVisibility(View.GONE);
            } else {
                dBinding.customPriceEt.setVisibility(View.VISIBLE);
            }
        });

        dBinding.addToCartBtn.setOnClickListener(view1 -> {
            String quantity = dBinding.quantityEt.getText().toString();
            if (!quantity.equals(".") && Float.parseFloat(quantity) > 0) {
                AddFoodToCartHandler(food, dBinding);
                dialog.dismiss();
            } else {
                Toasty.info(FoodActivity.this,"Invalid Quantity",Toasty.LENGTH_SHORT,true).show();
            }
        });

        dBinding.addMultipleBtn.setOnClickListener(view1 -> {
            dBinding.quantityEt.clearFocus();
            dBinding.customPriceEt.clearFocus();
            String quantity = dBinding.quantityEt.getText().toString();
            if (!quantity.equals(".") && Float.parseFloat(quantity) > 0) {
                AddFoodToCartHandler(food, dBinding);
            } else {
                Toasty.info(FoodActivity.this,"Invalid Quantity",Toasty.LENGTH_SHORT,true).show();
            }
        });

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                SharedPref.write("addOnslist", new Gson().toJson(new ArrayList<Addonsinfo>()));
                selectedMark.setVisibility(View.GONE);
            }
        });

        dialog.show();
        int width = getResources().getDisplayMetrics().widthPixels;
        Window win = dialog.getWindow();
        win.setLayout((8 * width) / 9, WindowManager.LayoutParams.WRAP_CONTENT);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    private void setCartDialogTotalPrice(FoodCartDialogBinding dBinding, double addonsTotal) {
        String quantity = dBinding.quantityEt.getText().toString();
        if (!quantity.isEmpty() && !quantity.equals(".")){
            dBinding.totalPriceTv.setText(new DecimalFormat("#.##").format((Double.parseDouble(dBinding.quantityEt.getText().toString())*Double.parseDouble(variantPrice))+ addonsTotal));
        } else {
            dBinding.totalPriceTv.setText(new DecimalFormat("#.##").format((0.0*Double.parseDouble(variantPrice))+ addonsTotal));
        }
    }


    private void AddFoodToCartHandler(Foodinfo food, FoodCartDialogBinding dBinding) {
        Type type = new TypeToken<List<Addonsinfo>>() {/**/}.getType();
        List<Addonsinfo> addonsList = new Gson().fromJson(SharedPref.read("addOnslist", ""), type);
        boolean haveToInsert = false;

        if (food.getAddons().equals(1) && addonsList.size() > 0) {
            addonsTotal = 0.0;
            addOnsChecker = 1;
            for (int i = 0; i < addonsList.size(); i++) {
                addonsTotal += (addonsList.get(i).getAddonsquantity() * Double.parseDouble(addonsList.get(i).getAddonsprice()));
            }

        } else {
            addonsTotal = 0.0;
            addOnsChecker = 0;
        }

        //fake new AddonList for Checking
        List<String> newAddons = new ArrayList<>();
        for (int n = 0; n < addonsList.size(); n++) {
            newAddons.add(addonsList.get(n).getAddonsid());
        }

        if (foodTask.size() > 0) {
            for (int l = 0; l < foodTask.size(); l++) {
                if (foodTask.get(l).getProductId().equals(food.getProductId()) &&
                        foodTask.get(l).getVariantName().equals(variantName) &&
                        foodTask.get(l).getPrice().equals(new DecimalFormat("#.##").format(Double.parseDouble(variantPrice)))) {
                    //fake old AddonList for Checking
                    List<String> oldAddons = new ArrayList<>();
                    for (int o = 0; o < foodTask.get(l).getAddonsinfo().size(); o++) {
                        oldAddons.add(foodTask.get(l).getAddonsinfo().get(o).getAddonsid());
                    }
                    if (new Gson().toJson(newAddons).contains(new Gson().toJson(oldAddons))) {
                        for (int a = 0; a < foodTask.get(l).getAddonsinfo().size(); a++) {
                            for (int b = 0; b < addonsList.size(); b++) {
                                if (foodTask.get(l).getAddonsinfo().get(a).getAddonsid().equals(addonsList.get(b).getAddonsid())) {
                                    foodTask.get(l).getAddonsinfo().get(a).setAddonsquantity(foodTask.get(l).getAddonsinfo().get(a).getAddonsquantity()
                                                    + addonsList.get(b).getAddonsquantity());
                                }
                            }
                        }
                        //updating quantity
                        haveToInsert = false;
                        foodTask.get(l).setQuantitys(foodTask.get(l).getQuantitys() + Float.parseFloat(dBinding.quantityEt.getText().toString()));
                        foodTask.get(l).setAddOnsTotal(foodTask.get(l).getAddOnsTotal() + addonsTotal);
                        updateFood(foodTask.get(l));
                        break;
                    } else {
                        haveToInsert = true;
                    }
                } else {
                    haveToInsert = true;
                }
            }
        } else {
            haveToInsert = true;
        }
        if (haveToInsert) {
            insertFood(food, Float.parseFloat(dBinding.quantityEt.getText().toString()), addonsList);
        }
        getUnit();
    }

    private String getQuantity(float quantity, boolean isCustom) {
        if (isCustom == true) {
            return String.valueOf(quantity);
        } else {
            return String.valueOf(quantity).split("\\.")[0];
        }
    }

    private void insertFood(Foodinfo food, float quantity, List<Addonsinfo> addonsList) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Foodinfo unitListItem = new Foodinfo();
                unitListItem.setProductId(food.getProductId());
                unitListItem.setProductName(food.getProductName());
                unitListItem.setVariantid(variantId);
                unitListItem.setVariantName(variantName);
                unitListItem.setPrice(new DecimalFormat("#.##").format(Double.parseDouble(variantPrice)));
                unitListItem.setQuantitys(quantity);
                unitListItem.setIscustqty(food.getIscustqty());
                ////////////////////////////////////
                unitListItem.setProductvat(food.getProductvat());
                unitListItem.setOfferIsavailable(food.getOfferIsavailable());
                unitListItem.setOfferstartdate(food.getOfferstartdate());
                unitListItem.setOfferendate(food.getOfferendate());
                unitListItem.setOffersRate(food.getOffersRate());
                /////////////////////////////////////
                unitListItem.setAddons(addOnsChecker);
                unitListItem.setAddOnsTotal(addonsTotal);
                unitListItem.setAddonsinfo(addonsList);

                appDatabase.taskDao().insertFood(unitListItem);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getUnit();
            }
        }
        AddProduct st = new AddProduct();
        st.execute();

    }


    private void updateFood(Foodinfo foodinfo) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                appDatabase.taskDao().updateFood(foodinfo);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                getUnit();
            }
        }
        AddProduct st = new AddProduct();
        st.execute();
    }


    @Override
    protected void onResume() {
        super.onResume();
        getUnit();
        SharedPref.write("addOnslist", new Gson().toJson(new ArrayList<Addonsinfo>()));
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
