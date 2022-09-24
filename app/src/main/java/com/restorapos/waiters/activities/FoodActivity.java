package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
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

import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.AddOnsItemAdapter;
import com.restorapos.waiters.adapters.FoodAdapter;
import com.restorapos.waiters.databinding.ActivityFoodBinding;
import com.restorapos.waiters.databinding.DesignSubCategoryItemBinding;
import com.restorapos.waiters.databinding.FoodCartDialogBinding;
import com.restorapos.waiters.interfaces.FoodDialogInterface;
import com.restorapos.waiters.model.foodlistModel.Categoryinfo;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.model.foodlistModel.FoodlistResponse;
import com.restorapos.waiters.model.foodlistModel.Varientlist;
import com.restorapos.waiters.model.foodlistModel2.Foodinfo2;
import com.restorapos.waiters.offlineDb.AppDatabase;
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity implements FoodDialogInterface {
    private ActivityFoodBinding binding;
    private int orderQuantity = 0;
    private String pCategoryId;
    private String userId;
    private WaitersService waitersService;
    private List<Foodinfo> foodtasks;
    private final String TAG = "FoodActivity";
    private SpotsDialog progressDialog;
    private List<Foodinfo> foodItems;
    //private List<Foodinfo2> itemss2;

    public static int addOnsChecker = 0;
    //private String selecTedVariants;
    private List<Varientlist> varientlist;
    private String variantPrice = "";
    private String variantid = "";
    private String variantName = "";
    private final List<Foodinfo> orderedItems = new ArrayList<>();
    private int quantity = 0;
    private int countNow= 1;

    private FoodDialogInterface foodDialogInterface;
    private FoodAdapter foodAdapter;
    private AppDatabase appDatabase;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPref.init(this);


        //itemss2                             = new ArrayList<>();
        waitersService                      = AppConfig.getRetrofit(this).create(WaitersService.class);
        pCategoryId                         = getIntent().getStringExtra("CATEGORYID");
        userId                              = SharedPref.read("ID", "");
        appDatabase                         = DatabaseClient.getInstance(this).getAppDatabase();
        foodDialogInterface                 = this;
        progressDialog                      = new SpotsDialog(this, R.style.Custom);
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
        binding.countTv.setText(SharedPref.read("CartCount","0"));
        binding.totalTv.setText(SharedPref.read("CURRENCY", "")+" "+SharedPref.read("CartTotal","0.0"));
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
            binding.foodRecycler.setAdapter(new FoodAdapter(FoodActivity.this, newList,foodDialogInterface));
        } else {
            binding.foodRecycler.setAdapter(new FoodAdapter(FoodActivity.this, foodItems,foodDialogInterface));
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
                        foodAdapter = new FoodAdapter(FoodActivity.this, foodItems,foodDialogInterface);
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
                        binding.foodRecycler.setAdapter(new FoodAdapter(FoodActivity.this, foodItems,foodDialogInterface));
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
            for (int i = 0; i < foodtasks.size(); i++) {
                Total += (Double.parseDouble(foodtasks.get(i).getPrice()) * foodtasks.get(i).quantitys) + foodtasks.get(i).getAddOnsTotal();
            }

            String cartTotal = String.valueOf(Double.valueOf(new DecimalFormat("##.##").format(Total)));

            SharedPref.write("CartCount", String.valueOf(foodtasks.size()));
            SharedPref.write("CartTotal",cartTotal);

            setFoodCartHeaders();

        } catch (Exception ignored) {/**/}
    }


    public class FoodSubCategoryAdapter extends RecyclerView.Adapter<FoodSubCategoryAdapter.ViewHolder> implements Filterable {

        private List<Categoryinfo> items;
        private Context context;
        int row_index = 0;

        public FoodSubCategoryAdapter(Context applicationContext, List<Categoryinfo> itemArrayList) {
            SharedPref.init(applicationContext);
            this.context = applicationContext;
            this.items = itemArrayList;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_sub_category_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(ViewHolder holder, int i) {
            holder.sBinding.subCategory.setText(items.get(i).getName());
            holder.sBinding.subCategory.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    row_index = i;
                    notifyDataSetChanged();
                    progressDialog.show();
                    if (items.get(i).getName().equals("All")) {
                        getAllFoodItem();
                    } else {
                        Log.d("hahahaah",items.get(i).getCategoryID());
                        getFoodItem(items.get(i).getCategoryID());
                    }
                }
            });
            if (row_index == i) {
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
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                List<Foodinfo> filteredList = new ArrayList<>();
                FilterResults results = new FilterResults();
                results.values = filteredList;
                return results;
            }
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {/**/}
        };

        public class ViewHolder extends RecyclerView.ViewHolder {
            private DesignSubCategoryItemBinding sBinding;

            public ViewHolder(View view) {
                super(view);
                sBinding = DesignSubCategoryItemBinding.bind(view);
            }
        }
    }

    private void insertFood(Foodinfo foodinfo) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Foodinfo unitListItem = new Foodinfo();
                unitListItem.setProductName(foodinfo.getProductName());
                unitListItem.setProductId(foodinfo.getProductId());

                unitListItem.setOfferIsavailable(foodinfo.getOfferIsavailable());
                unitListItem.setOfferstartdate(foodinfo.getOfferstartdate());
                unitListItem.setOfferendate(foodinfo.getOfferendate());
                unitListItem.setOffersRate(foodinfo.getOffersRate());

                unitListItem.setVariantid(foodinfo.getVariantid());
                unitListItem.setVariantName(foodinfo.getVariantName());
                unitListItem.setAddOnsTotal(foodinfo.getAddOnsTotal());
                unitListItem.setPrice(foodinfo.getPrice());

                unitListItem.setProductvat(foodinfo.getProductvat());
                unitListItem.setAddOnsName(foodinfo.getAddOnsName());
                unitListItem.setAddons(foodinfo.getAddons());

                if (addOnsChecker==1){
                    if (foodinfo.getAddons()==1) {
                        unitListItem.setAddonsinfo(foodinfo.getAddonsinfo());
                    }
                }
                unitListItem.quantitys = quantity;


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

    private void UpdateFood(Foodinfo foodinfo, int quantity) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Foodinfo unitListItem = new Foodinfo();
                unitListItem.setAddonsinfo(foodinfo.getAddonsinfo());
                unitListItem.setProductName(foodinfo.getProductName());
                unitListItem.setProductId(foodinfo.getProductId());
                unitListItem.setVariantid(foodinfo.getVariantid());
                unitListItem.setAddOnsTotal(foodinfo.getAddOnsTotal());
                unitListItem.setPrice(foodinfo.getPrice());
                unitListItem.setProductvat(foodinfo.getProductvat());
                unitListItem.setAddOnsName(foodinfo.getAddOnsName());
                unitListItem.setId(foodinfo.getId());

                unitListItem.setOfferIsavailable(foodinfo.getOfferIsavailable());
                unitListItem.setOfferstartdate(foodinfo.getOfferstartdate());
                unitListItem.setOfferendate(foodinfo.getOfferendate());
                unitListItem.setOffersRate(foodinfo.getOffersRate());

                unitListItem.setAddons(foodinfo.getAddons());
                unitListItem.quantitys = quantity;


                appDatabase.taskDao().updateFood(unitListItem);

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


    private void getUnit() {
        class GetProduct extends AsyncTask<Void, Void, List<Foodinfo>> {

            @Override
            protected List<Foodinfo> doInBackground(Void... voids) {

                List<Foodinfo> productList = appDatabase.taskDao().getAllUnit();

                foodtasks = productList;

                return productList;
            }

            @Override
            protected void onPostExecute(List<Foodinfo> tasks) {
                super.onPostExecute(tasks);

                foodtasks = tasks;

                getFoodCart();
            }
        }
        GetProduct gt = new GetProduct();
        gt.execute();
    }

    private void deleteFood(Foodinfo foodinfo) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {

                appDatabase.taskDao().delete(foodinfo);

                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                getUnit();
            }
        }
    }










    /*public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.ViewHolder> {
        private List<Foodinfo> items;
        private Context context;
        List<Addonsinfo> addOnsitems;


        public FoodAdapter(Context applicationContext, List<Foodinfo> itemArrayList, List<AppFoodInfo> itemss) {
            SharedPref.init(context);
            this.context = applicationContext;
            this.items = itemArrayList;
            addOnsitems = new ArrayList<>();
            Log.d("checklistfood",new Gson().toJson(itemArrayList));
        }


        @Override
        public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
            View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_foods_item, viewGroup, false);
            return new ViewHolder(view);
        }

        @SuppressLint("RecyclerView")
        @Override
        public void onBindViewHolder(final ViewHolder holder, int i) {

            String url = items.get(i).getProductImage();
            if (url != null) {
                Glide.with(context).load(url).into(holder.fBinding.categoryImage);
            }

            holder.fBinding.categoryName.setText(items.get(i).getProductName());
            holder.fBinding.price.setText(SharedPref.read("CURRENCY", "") + " " + items.get(i).getPrice());
            holder.fBinding.notes.setText(items.get(i).getDestcription());
            holder.fBinding.varient.setText(items.get(i).getVariantName());


            if (items.get(i).getOfferIsavailable() != null) {
                if (items.get(i).getOfferIsavailable().equals("1") && isOfferAvailable2(items.get(i).getOfferstartdate(), items.get(i).getOfferendate())) {
                    holder.fBinding.offerLay.setVisibility(View.VISIBLE);
                    holder.fBinding.offerRate.setText(items.get(i).getOffersRate());
                }
            }

           *//* holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toasty.success(context,items.get(i).getProductId(),Toasty.LENGTH_SHORT).show();
                }
            });*//*




            holder.itemView.setOnClickListener(v -> {

                if (holder.fBinding.qtyShowTv.getVisibility()==View.VISIBLE){
                    holder.fBinding.qtyShowTv.setVisibility(View.GONE);
                }else{
                    holder.fBinding.qtyShowTv.setVisibility(View.VISIBLE);

                    if (foodtasks.size() == 0) {
                        if (items.get(i).getAddons().equals(1) || itemss2.size() > 0) {
                            List<Addonsinfo> addons = items.get(i).getAddonsinfo();

                            FoodCartDialog(addons, i, items.get(i).getVariantid(), items.get(i).getVariantName(),holder.fBinding.qtyShowTv,items);

                        } else {

                            insertFood(items.get(i));
                            Gson g = new Gson();
                            int count = items.get(i).quantity;
                            count++;
                            items.get(i).quantity = count;

                            //holder.qtyShowTv.setText(String.valueOf(items.get(i).quantity));
                            List<Foodinfo> orderedItems = new ArrayList<>();
                            for (Foodinfo food : items) {
                                if (food.quantity > 0) {
                                    orderedItems.add(food);
                                }
                            }
                            items.get(i).setDestcription("");
                            Data data = new Data();
                            data.setFoodinfo(orderedItems);
                            String foods = g.toJson(data);

                            items.get(i).quantity = 0;
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    getFoodCart();
                                }
                            }, 1000);

                        }
                    }
                    else if (foodtasks.size() > 0 && itemss2.size() > 0) {

                        if (items.get(i).getAddons().equals(1) || itemss2.size() > 0) {
                            List<Addonsinfo> addons = items.get(i).getAddonsinfo();
                            *//*Gson gson = new Gson();
                            String string = gson.toJson(addons);*//*

                            FoodCartDialog(addons, i, items.get(i).getVariantid(), items.get(0).getVariantName(), holder.fBinding.qtyShowTv, items);

                        } else {

                            insertFood(items.get(i));
                            Gson g = new Gson();
                            int count = items.get(i).quantity;
                            count++;
                            items.get(i).quantity = count;
                            List<Foodinfo> orderedItems = new ArrayList<>();
                            for (Foodinfo food : items) {
                                if (food.quantity > 0) {
                                    orderedItems.add(food);
                                }
                            }
                            items.get(i).setDestcription("");
                            Data data = new Data();
                            data.setFoodinfo(orderedItems);
                            String foods = g.toJson(data);

                            items.get(i).quantity = 0;
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
                            String list = items.get(i).getProductId() + items.get(i).getVariantid();
                            for (int t = 0; t < foodtasks.size(); t++) {
                                String list1 = foodtasks.get(t).getProductId() + foodtasks.get(t).getVariantid();

                                if (list.equals(list1)) {

                                    deleteFood(foodtasks.get(t));
                                }
                            }
                        } else {
                            Log.d("ooo", "onClick: ");
                            if (items.get(i).getAddons().equals(1)) {
                                List<Addonsinfo> addons = items.get(i).getAddonsinfo();
                                Gson gson = new Gson();
                                String string = gson.toJson(addons);

                                FoodCartDialog(addons, i, items.get(i).getVariantid(), items.get(0).getVariantName(), holder.fBinding.qtyShowTv, items);
                            } else {

                                insertFood(items.get(i));
                                holder.fBinding.qtyShowTv.setVisibility(View.VISIBLE);
                                Gson g = new Gson();
                                int count = items.get(i).quantity;
                                count++;
                                items.get(i).quantity = count;
                                List<Foodinfo> orderedItems = new ArrayList<>();
                                for (Foodinfo food : items) {
                                    if (food.quantity > 0) {
                                        orderedItems.add(food);
                                    }
                                }
                                items.get(i).setDestcription("");
                                Data data = new Data();
                                data.setFoodinfo(orderedItems);
                                String foods = g.toJson(data);


                                items.get(i).quantity = 0;
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        getFoodCart();
                                    }
                                }, 1000);

                            }
                        }
                    }
                    catch (Exception ignored) {*//**//*}
                }
            });
        }

        @Override
        public int getItemCount() {
            return items.size();
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






        *//*public void FoodCartDialog(List<Addonsinfo> addonsinfoList, final int pos, String variantId, String variantName,
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

                *//**//*this.items.get(pos).setPrice(variantPric);
                this.items.get(pos).setVariantName(variantname);
                this.items.get(pos).setVariantid(variantid);
                this.items.get(pos).quantitys = orderQuantity;
                this.items.get(pos).quantity = orderQuantity;
                this.items.get(pos).setQuantity(Integer.parseInt(quantity));
                this.items.get(pos).setVariantName(selecTedVariants);
                this.items.get(pos).setAddons(list.get(pos).getAddons());
                Log.d("addOnsChecker",""+addOnsChecker);*//**//*

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
        }*//*
    }*/





    @Override
    public void onFoodItemClick(Context context, Foodinfo food, ImageView selectedMark) {

        varientlist = food.getVarientlist();
        boolean haveToInsert = false;
        List<String> variantNameList = new ArrayList<>();

        if (food.getTotalvariant().equals("1")){
            variantNameList.add(food.getVariantName());
        } else {
            for (int i =0; i < varientlist.size(); i++){
                variantNameList.add(varientlist.get(i).getMultivariantName());
            }
        }


        final Dialog dialog = new Dialog(context);
        View view = LayoutInflater.from(context).inflate(R.layout.food_cart_dialog,null);
        FoodCartDialogBinding dBinding = FoodCartDialogBinding.bind(view);
        dBinding.addonsHeader.setVisibility(View.GONE);
        dialog.setContentView(view);


        dBinding.productName.setText(food.getProductName());


        dBinding.variantSpinner.setAdapter(new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, variantNameList));


        Log.wtf("VARIENTNAME",new Gson().toJson(food));


        dBinding.variantSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long l) {

                variantName = dBinding.variantSpinner.getSelectedItem().toString();

                if (food.getTotalvariant().equals("1")){
                    variantid = food.getVariantid();
                    variantPrice = food.getPrice();
                } else {
                    variantid = varientlist.get(pos).getMultivariantid();
                    variantPrice = varientlist.get(pos).getMultivariantPrice();
                }
                dBinding.variantPriceTV.setText(variantPrice);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {/**/}
        });



        dBinding.closeBtn.setOnClickListener(view1 -> {
            dialog.dismiss();
        });


        if (food.getAddons() == 1){
            dBinding.dialogAddonsRv.setAdapter(new AddOnsItemAdapter(context,food.getAddonsinfo(),variantPrice,dBinding.variantPriceTV));
        }



        dBinding.plusBtn.setOnClickListener(view1 -> {
            double oldPrice = Double.parseDouble(dBinding.variantPriceTV.getText().toString());
            dBinding.variantPriceTV.setText(String.valueOf(oldPrice+Double.parseDouble(variantPrice)));
        });


        dBinding.minusBtn.setOnClickListener(view1 ->{
            countNow = Integer.parseInt(dBinding.quantityFromUser.getText().toString());
            if (countNow>1){
                countNow --;
                dBinding.quantityFromUser.setText(countNow);
                double oldPrice = Double.parseDouble(dBinding.variantPriceTV.getText().toString());
                dBinding.variantPriceTV.setText(String.valueOf(oldPrice-Double.parseDouble(variantPrice)));
            }

        });



        dialog.show();
        int width = getResources().getDisplayMetrics().widthPixels;
        Window win = dialog.getWindow();
        win.setLayout((9*width)/10, WindowManager.LayoutParams.WRAP_CONTENT);
        win.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }





    @Override
    protected void onResume() {
        super.onResume();
        //binding.searchEt.setFocusable(false);
        //parentLay.requestFocus();
        setFoodCartHeaders();
        //Utils.hideKeyboard(this);
        //binding.searchEt.setFocusable(true);
        //binding.searchEt.setFocusableInTouchMode(true);
    }





    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
