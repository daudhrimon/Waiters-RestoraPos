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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.restorapos.waiters.databinding.ActivityFoodBinding;
import com.restorapos.waiters.databinding.DesignFoodsItemBinding;
import com.restorapos.waiters.databinding.DesignSubCategoryItemBinding;
import com.restorapos.waiters.databinding.FoodCartDialogBinding;
import com.restorapos.waiters.model.appModel.AppFoodInfo;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Categoryinfo;
import com.restorapos.waiters.model.foodlistModel.Data;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.model.foodlistModel.FoodlistResponse;
import com.restorapos.waiters.model.foodlistModel.Varientlist;
import com.restorapos.waiters.model.foodlistModel2.Foodinfo2;
import com.restorapos.waiters.model.foodlistModel2.FoodlistResponse2;
import com.restorapos.waiters.offlineDb.AppDatabase;
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodActivity extends AppCompatActivity {
    private ActivityFoodBinding binding;
    private int orderQuantity = 0;
    private String pCategoryId;
    private String userId;
    private WaitersService waitersService;
    private List<Foodinfo> foodtasks;
    private final String TAG = "FoodActivity";
    private SpotsDialog progressDialog;
    private List<Foodinfo> itemss;
    private List<Foodinfo2> itemss2;
    public static int addOnsChecker = 0;
    private String selecTedVariants;
    private String variantPric = "";
    private String variantid = "";
    private String variantname = "";
    private final List<Foodinfo> orderedItems = new ArrayList<>();
    private String quantity = "";
    private int countNow= 1;
    private FoodssAdapter foodssAdapter;
    private AppDatabase appDatabase;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFoodBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



        initial();

        getAllFoodItemWithMultipleVariant();

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
            for (int i = 0; i < itemss.size(); i++) {
                if (itemss.get(i).getProductName().toLowerCase().contains(value.toLowerCase())) {
                    newList.add(itemss.get(i));
                }
            }
            binding.foodRecycler.setAdapter(new FoodssAdapter(FoodActivity.this, newList, null));
        } else {
            binding.foodRecycler.setAdapter(new FoodssAdapter(FoodActivity.this, itemss, null));
        }
    }





    private void initial() {
        SharedPref.init(this);

        itemss2 = new ArrayList<>();
        waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        pCategoryId = getIntent().getStringExtra("CATEGORYID");
        userId = SharedPref.read("ID", "");

        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();

        MainActivity.appBarDefault();
        appDatabase = DatabaseClient.getInstance(this).getAppDatabase();
    }





    private void getAllFoodItemWithMultipleVariant() {
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
    }





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
                        itemss = response.body().getData().getFoodinfo();
                        if (binding.foodRecycler.getVisibility() == View.GONE) {
                            binding.foodRecycler.setVisibility(View.VISIBLE);
                            binding.emptyLay.setVisibility(View.GONE);
                        }
                        foodssAdapter = new FoodssAdapter(FoodActivity.this, itemss, null);
                        binding.foodRecycler.setAdapter(foodssAdapter);
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
                        itemss = response.body().getData().getFoodinfo();

                        Log.d("ppp33", "onResponse: ppp" + new Gson().toJson(itemss));
                        if (binding.foodRecycler.getVisibility() == View.GONE) {
                            binding.foodRecycler.setVisibility(View.VISIBLE);
                            binding.emptyLay.setVisibility(View.GONE);
                        }
                        binding.foodRecycler.setAdapter(new FoodssAdapter(FoodActivity.this, itemss, null));
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
                unitListItem.quantitys = Integer.parseInt(quantity);


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

    public class FoodssAdapter extends RecyclerView.Adapter<FoodssAdapter.ViewHolder> {
        private List<Foodinfo> items;
        private Context context;
        List<Addonsinfo> addOnsitems;

        public FoodssAdapter(Context applicationContext, List<Foodinfo> itemArrayList, List<AppFoodInfo> itemss) {
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
        public void onBindViewHolder(final ViewHolder viewHolder, int i) {
            Log.d("sdasasd222",items.get(i).getProductName());
            viewHolder.fBinding.categoryName.setText(items.get(i).getProductName());
            viewHolder.fBinding.price.setText(SharedPref.read("CURRENCY", "") + " " + items.get(i).getPrice());
            viewHolder.fBinding.notes.setText(items.get(i).getDestcription());
            viewHolder.fBinding.varient.setText(items.get(i).getVariantName());

            if (items.get(i).getOfferIsavailable() != null) {
                if (items.get(i).getOfferIsavailable().equals("1") && isOfferAvailable2(items.get(i).getOfferstartdate(), items.get(i).getOfferendate())) {
                    viewHolder.fBinding.offerLay.setVisibility(View.VISIBLE);
                    viewHolder.fBinding.offerRate.setText(items.get(i).getOffersRate());
                }
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toasty.success(context,items.get(i).getProductId(),Toasty.LENGTH_SHORT).show();
                }
            });
            String url = items.get(i).getProductImage();
            if (url != null) {
                Glide.with(context).load(url).into(viewHolder.fBinding.categoryImage);
            }

            viewHolder.fBinding.layoutDesignFoodItem.setOnClickListener(v -> {
                if (viewHolder.fBinding.qtyShowTv.getVisibility()==View.VISIBLE){
                    viewHolder.fBinding.qtyShowTv.setVisibility(View.GONE);
                }else{
                    viewHolder.fBinding.qtyShowTv.setVisibility(View.VISIBLE);
                    Log.d("foodtasks_size",""+itemss2.size());
                    if (foodtasks.size() == 0) {
                        if (items.get(i).getAddons().equals(1) || itemss2.size() > 0) {
                            List<Addonsinfo> addons = items.get(i).getAddonsinfo();

                            Log.d("pppppawaw", new Gson().toJson(items));
                            FoodCartDialog(addons, i, items.get(i).getVariantid(), items.get(i).getVariantName(),viewHolder.fBinding.qtyShowTv,items);
                        } else {
                            Log.d("pppppawaw", "onBindViewHolder: " + new Gson().toJson(items.get(i)));
                            insertFood(items.get(i));
                            Gson g = new Gson();
                            int count = items.get(i).quantity;
                            count++;
                            items.get(i).quantity = count;
                            Log.d("qqqquentity", "" + count);
                            //viewHolder.qtyShowTv.setText(String.valueOf(items.get(i).quantity));
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
                            Log.d("pok", "ArrayMAde: " + foods);
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
                            Gson gson = new Gson();
                            String string = gson.toJson(addons);
                            Log.d("ppppp", "onClick: " + string);
                            FoodCartDialog(addons, i, items.get(i).getVariantid(), items.get(0).getVariantName(), viewHolder.fBinding.qtyShowTv, items);
                        } else {
                            Log.d("fsdfdsf", "onBindViewHolder: " + new Gson().toJson(items.get(i)));
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
                            Log.d("pok", "ArrayMAde: " + foods);
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
                        if (viewHolder.fBinding.qtyShowTv.getVisibility() == View.VISIBLE) {
                            String list = items.get(i).getProductId() + items.get(i).getVariantid();
                            for (int t = 0; t < foodtasks.size(); t++) {
                                String list1 = foodtasks.get(t).getProductId() + foodtasks.get(t).getVariantid();
                                Log.d("pasodasd", "onBindViewHolder: " + list + " " + list1);
                                if (list.equals(list1)) {
                                    Log.d("000", "onBindViewHolder: " + list + " " + list1);
                                    Log.d("ooo", "onBind: " + t);
                                    deleteFood(foodtasks.get(t));
                                }
                            }
                        } else {
                            Log.d("ooo", "onClick: ");
                            if (items.get(i).getAddons().equals(1)) {
                                List<Addonsinfo> addons = items.get(i).getAddonsinfo();
                                Gson gson = new Gson();
                                String string = gson.toJson(addons);
                                Log.d("ppppp", "onClick: " + string);
                                FoodCartDialog(addons, i, items.get(i).getVariantid(), items.get(0).getVariantName(), viewHolder.fBinding.qtyShowTv, items);
                            } else {
                                Log.d("fsdfdsf", "onBindViewHolder: " + new Gson().toJson(items.get(i)));
                                insertFood(items.get(i));
                                viewHolder.fBinding.qtyShowTv.setVisibility(View.VISIBLE);
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
                                Log.d("pok", "ArrayMAde: " + foods);

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
                    catch (Exception ignored) {/**/}
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






        public void FoodCartDialog(List<Addonsinfo> addonsinfoList, final int pos, String variantId, String variantName,
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

            String productName, variantddd;
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

                this.items.get(pos).setPrice(variantPric);
                this.items.get(pos).setVariantName(variantname);
                this.items.get(pos).setVariantid(variantid);
                this.items.get(pos).quantitys = orderQuantity;
                this.items.get(pos).quantity = orderQuantity;
                this.items.get(pos).setQuantity(Integer.parseInt(quantity));
                this.items.get(pos).setVariantName(selecTedVariants);
                this.items.get(pos).setAddons(list.get(pos).getAddons());
                Log.d("addOnsChecker",""+addOnsChecker);

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
        }
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
