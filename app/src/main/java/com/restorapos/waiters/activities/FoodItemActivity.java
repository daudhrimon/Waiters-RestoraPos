package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.AddOnsItemAdapter;
import com.restorapos.waiters.model.appModel.AppFoodInfo;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Categoryinfo;
import com.restorapos.waiters.model.foodlistModel.Data;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.model.foodlistModel.FoodlistResponse;
import com.restorapos.waiters.model.foodlistModel.Varientlist;
import com.restorapos.waiters.model.foodlistModel2.Foodinfo2;
import com.restorapos.waiters.model.foodlistModel2.FoodlistResponse2;
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.restorapos.waiters.utils.Utils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodItemActivity extends AppCompatActivity {
    private int orderQuantity = 0;
    private EditText quantityFromUser;
    private ImageView backImage;
    private TextView count, total;
    private LinearLayout viewCartTv;
    private String pCategoryId;
    private String userId;
    private RecyclerView subCategoryRecyclerView, foodRecyclerView;
    private WaitersService waitersService;
    private List<Foodinfo> foodtasks;
    private final String TAG = "FoodItemActivity";
    private LinearLayout layout;
    private SpotsDialog progressDialog;
    private EditText searchviewId;
    private List<Foodinfo> itemss;
    private List<Foodinfo2> itemss2;
    public static TextView variantPriceTV;
    public static int addOnsChecker = 0;
    private FoodssAdapter foodssAdapter;
    private String selecTedVariants;
    private String variantPric = "";
    private String variantid = "";
    private String variantname = "";
    private final List<Foodinfo> orderedItems = new ArrayList<>();
    private String quantity = "";
    private int countNow= 1;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_item);

        initial();

        getAllFooditemWithMultipleVariants();

        getUnit();

        getSubCategoryItem();

        getAllFoodItem();

        viewCartTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FoodItemActivity.this, FoodCartActivity.class));
            }
        });

        backImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        searchviewId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                customfilterList(charSequence.toString());
            }
            @Override
            public void afterTextChanged(Editable editable) {/**/}
        });


    }

    private void setFoodCartHeaders() {
        count.setText(SharedPref.read("CartCount","0"));
        total.setText(SharedPref.read("CURRENCY", "")+" "+SharedPref.read("CartTotal","0.0"));
    }

    private void customfilterList(String value) {
        List<Foodinfo> newList = new ArrayList<>();
        if (value != null && !value.isEmpty()) {
            newList.clear();
            for (int i = 0; i < itemss.size(); i++) {
                if (itemss.get(i).getProductName().toLowerCase().contains(value.toLowerCase())) {
                    newList.add(itemss.get(i));
                }
            }
            foodRecyclerView.setAdapter(new FoodssAdapter(FoodItemActivity.this, newList, null));
        } else {
            foodRecyclerView.setAdapter(new FoodssAdapter(FoodItemActivity.this, itemss, null));
        }
    }

    private void initial() {
        SharedPref.init(this);
        subCategoryRecyclerView = findViewById(R.id.subCategoryRecyclerViewId);
        foodRecyclerView = findViewById(R.id.foodRecyclerViewID);
        viewCartTv = findViewById(R.id.viewCartId);
        backImage = findViewById(R.id.backImageViewId);
        count = findViewById(R.id.countTv);
        total = findViewById(R.id.totalTv);
        layout = findViewById(R.id.layoutId);
        searchviewId = findViewById(R.id.searchviewId);
        itemss2 = new ArrayList<>();
        progressDialog = new SpotsDialog(this, R.style.Custom);
        waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        pCategoryId = getIntent().getStringExtra("CATEGORYID");
        userId = SharedPref.read("ID", "");
        progressDialog.show();
        MainActivity.appBarDefault();
    }

    private void getAllFooditemWithMultipleVariants() {
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
                    subCategoryRecyclerView.setAdapter(new FoodSubCategoryAdapter(FoodItemActivity.this, items));
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
                        if (foodRecyclerView.getVisibility() == View.GONE) {
                            foodRecyclerView.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.GONE);
                        }
                        foodssAdapter = new FoodssAdapter(FoodItemActivity.this, itemss, null);
                        foodRecyclerView.setAdapter(foodssAdapter);
                        progressDialog.dismiss();
                    } else {
                        foodRecyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                        progressDialog.dismiss();
                    }
                } catch (Exception e) {
                    foodRecyclerView.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<FoodlistResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        foodRecyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
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
                        if (foodRecyclerView.getVisibility() == View.GONE) {
                            foodRecyclerView.setVisibility(View.VISIBLE);
                            layout.setVisibility(View.GONE);
                        }
                        foodRecyclerView.setAdapter(new FoodssAdapter(FoodItemActivity.this, itemss, null));
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }


                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        foodRecyclerView.setVisibility(View.GONE);
                        layout.setVisibility(View.VISIBLE);
                    }
                } catch (Exception e) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                    foodRecyclerView.setVisibility(View.GONE);
                    layout.setVisibility(View.VISIBLE);
                    Log.d("ppp", "Exception: " + e.getLocalizedMessage());
                }
            }

            @Override
            public void onFailure(Call<FoodlistResponse> call, Throwable t) {
                Log.d("ppp", "onFailure: " + t.getLocalizedMessage());
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
                foodRecyclerView.setVisibility(View.GONE);
                layout.setVisibility(View.VISIBLE);
            }
        });
    }

    private void getFoodCart() {
        try {
            double Total = 0.0;
            //double vatTotal = 0.0;
            for (int i = 0; i < foodtasks.size(); i++) {
                Total += (Double.parseDouble(foodtasks.get(i).getPrice()) * foodtasks.get(i).quantitys) + foodtasks.get(i).getAddOnsTotal();
                //vatTotal += (Double.parseDouble(foodtasks.get(i).getPrice()) * Double.parseDouble(foodtasks.get(i).getProductvat())) / 100;
            }
            count.setText(String.valueOf(foodtasks.size()));
            String cartTotal = String.valueOf(Double.valueOf(new DecimalFormat("##.##").format(Total)));
            total.setText(SharedPref.read("CURRENCY", "")+" "+cartTotal);
            SharedPref.write("CartCount", count.getText().toString());
            SharedPref.write("CartTotal",cartTotal);
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
        public void onBindViewHolder(ViewHolder viewHolder, int i) {
            viewHolder.subCategoryName.setText(items.get(i).getName());
            viewHolder.subCategoryName.setOnClickListener(new View.OnClickListener() {
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
                viewHolder.textView.setVisibility(View.VISIBLE);

            } else {
                viewHolder.textView.setVisibility(View.GONE);
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
            TextView subCategoryName, textView;

            public ViewHolder(View view) {
                super(view);
                subCategoryName = view.findViewById(R.id.subcatergoryNameId);
                textView = view.findViewById(R.id.tvId);
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
                DatabaseClient.getInstance(FoodItemActivity.this).getAppDatabase()
                        .taskDao()
                        .insertFood(unitListItem);
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
                unitListItem.setAddons(foodinfo.getAddons());
                unitListItem.quantitys = quantity;
                DatabaseClient.getInstance(FoodItemActivity.this).getAppDatabase()
                        .taskDao()
                        .updateFood(unitListItem);
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
                List<Foodinfo> productList = DatabaseClient
                        .getInstance(FoodItemActivity.this)
                        .getAppDatabase()
                        .taskDao()
                        .getAllUnit();
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

    private void delete(Foodinfo foodinfo) {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                DatabaseClient.getInstance(FoodItemActivity.this).getAppDatabase()
                        .taskDao()
                        .delete(foodinfo);
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
            viewHolder.categoryName.setText(items.get(i).getProductName());
            viewHolder.price.setText(SharedPref.read("CURRENCY", "") + " " + items.get(i).getPrice());
            viewHolder.notes.setText(items.get(i).getDestcription());
            viewHolder.varient.setText(items.get(i).getVariantName());

            viewHolder.mview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toasty.success(context,items.get(i).getProductId(),Toasty.LENGTH_SHORT).show();
                }
            });
            String url = items.get(i).getProductImage();
            if (url != null) {
                Glide.with(context).load(url).into(viewHolder.categoryImage);
            }

            viewHolder.layoutDesignFoodItem.setOnClickListener(v -> {
                if (viewHolder.qtyShowTv.getVisibility()==View.VISIBLE){
                    viewHolder.qtyShowTv.setVisibility(View.GONE);
                }else{
                    viewHolder.qtyShowTv.setVisibility(View.VISIBLE);
                    Log.d("foodtasks_size",""+itemss2.size());
                    if (foodtasks.size() == 0) {
                        if (items.get(i).getAddons().equals(1) || itemss2.size() > 0) {
                            List<Addonsinfo> addons = items.get(i).getAddonsinfo();

                            Log.d("pppppawaw", new Gson().toJson(items));
                            CUstomAlertDialog(addons, i, items.get(i).getVariantid(), items.get(i).getVariantName(),viewHolder.qtyShowTv,items);
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
                            CUstomAlertDialog(addons, i, items.get(i).getVariantid(), items.get(0).getVariantName(), viewHolder.qtyShowTv, items);
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
                        if (viewHolder.qtyShowTv.getVisibility() == View.VISIBLE) {
                            String list = items.get(i).getProductId() + items.get(i).getVariantid();
                            for (int t = 0; t < foodtasks.size(); t++) {
                                String list1 = foodtasks.get(t).getProductId() + foodtasks.get(t).getVariantid();
                                Log.d("pasodasd", "onBindViewHolder: " + list + " " + list1);
                                if (list.equals(list1)) {
                                    Log.d("000", "onBindViewHolder: " + list + " " + list1);
                                    Log.d("ooo", "onBind: " + t);
                                    delete(foodtasks.get(t));
                                }
                            }
                        } else {
                            Log.d("ooo", "onClick: ");
                            if (items.get(i).getAddons().equals(1)) {
                                List<Addonsinfo> addons = items.get(i).getAddonsinfo();
                                Gson gson = new Gson();
                                String string = gson.toJson(addons);
                                Log.d("ppppp", "onClick: " + string);
                                CUstomAlertDialog(addons, i, items.get(i).getVariantid(), items.get(0).getVariantName(), viewHolder.qtyShowTv, items);
                            } else {
                                Log.d("fsdfdsf", "onBindViewHolder: " + new Gson().toJson(items.get(i)));
                                insertFood(items.get(i));
                                viewHolder.qtyShowTv.setVisibility(View.VISIBLE);
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
            TextView categoryName, price, notes, qunatity, varient;
            ImageView qtyShowTv;
            ImageView plus, minus;
            ImageView categoryImage;
            CardView layoutDesignFoodItem;
            View mview;
            public ViewHolder(View view) {
                super(view);
                mview = view;
                categoryImage = view.findViewById(R.id.categoryImageId);
                categoryName = view.findViewById(R.id.categoryNameId);
                varient = view.findViewById(R.id.varientId);
                price = view.findViewById(R.id.categoryPriceId);
                notes = view.findViewById(R.id.categoryNotesId);
                qtyShowTv = view.findViewById(R.id.quantityShowId);
                layoutDesignFoodItem = view.findViewById(R.id.layoutDesignFoodItemId);
                plus = view.findViewById(R.id.plusId);
                minus = view.findViewById(R.id.minusId);
                qunatity = view.findViewById(R.id.quantityId);

            }
        }

        public void CUstomAlertDialog(List<Addonsinfo> addonsinfoList, final int pos, String variantId, String variantName,
                                      ImageView qtyShowTv, List<Foodinfo> list) {
            SharedPref.init(context);

            if (addonsinfoList == null) {
                addonsinfoList = new ArrayList<>();
            }
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);

            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view2 = inflater.inflate(R.layout.food_cart_dialog, null);
            ImageView addItems = view2.findViewById(R.id.addItems);
            ImageView minusItems = view2.findViewById(R.id.minusItems);
            LinearLayout addonsUpper = view2.findViewById(R.id.addonsUpper);
            addonsUpper.setVisibility(View.GONE);
            builder.setView(view2);
            String productName, variantddd;
            List<Varientlist> varientlists = new ArrayList<>();
            List<String> variantnames = new ArrayList<>();
            TextView variantNameTV = view2.findViewById(R.id.variantName);
            variantPriceTV = view2.findViewById(R.id.variantPrice);
            TextView closepaymentpageIV = view2.findViewById(R.id.closepaymentpageIV);
            Spinner spinnerVariants = view2.findViewById(R.id.spinnerVariants);
            quantityFromUser = view2.findViewById(R.id.quantityFromUser);
            quantity = quantityFromUser.getText().toString();
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
                variantPriceTV.setText(variantPric);
                variantNameTV.setText(productName);
                for (int j = 0; j < varientlists.size(); j++) {
                    variantnames.add(varientlists.get(j).getMultivariantName());

                }
                if (variantnames.size()==1){
                    variantnames.clear();
                    variantnames.add(variantName);
                }
                Log.d("variantCheck", String.valueOf(variantnames));


                spinnerVariants.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item,
                        variantnames));
            }
            else {
                productName = list.get(pos).getProductName();
                variantPric = list.get(pos).getPrice();
                varientlists = list.get(pos).getVarientlist();
                variantPriceTV.setText(variantPric);
                variantNameTV.setText(productName);
                Log.d("detailsCheck", "pname: " + productName + "pPrice " + variantPric + "list " + new Gson().toJson(varientlists));
            }

            addItems.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view) {
                    countNow++;
                    quantityFromUser.setText(String.valueOf(countNow));
                    double price =  Double.parseDouble(variantPric) * countNow;
                    variantPriceTV.setText(""+price);
                }
            });

            minusItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (countNow == 1) {
                        return;
                    } else {
                        countNow--;
                        quantityFromUser.setText(String.valueOf(countNow));
                        double priceReduce = Double.parseDouble(variantPric);
                        double price =  Double.parseDouble(variantPriceTV.getText().toString()) - priceReduce;
                        variantPriceTV.setText(""+price);
                    }

                }
            });

            List<Varientlist> finalVarientlists = varientlists;
            Log.d("checkvarientId",new Gson().toJson(finalVarientlists));

            spinnerVariants.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    selecTedVariants = spinnerVariants.getSelectedItem().toString();
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
                                variantPriceTV.setText(finalVarientlists.get(k).getMultivariantPrice());
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

            RecyclerView rv = view2.findViewById(R.id.recyclerDialogId);
            Button ok = view2.findViewById(R.id.okId);
            Button addmultipleVariant = view2.findViewById(R.id.addmultipleVariant);
            rv.setLayoutManager(new LinearLayoutManager(context));

            try {
                Log.d("addonslisize", "" + addonsinfoList.size());
                addOnsitems = addonsinfoList;

               
                if (addOnsitems.size() == 0) {
                    rv.setVisibility(View.GONE);
                }
                else {
                    addonsUpper.setVisibility(View.VISIBLE);
                    rv.setAdapter(new AddOnsItemAdapter(context, addOnsitems,variantPriceTV.getText().toString()));
                }
            }
            catch (Exception e) {
            }

            final AlertDialog alert = builder.create();
            alert.setCancelable(false);
            SharedPref.write("name", "");
            SharedPref.write("SUM", "");

            ok.setOnClickListener(v -> {
                List<Addonsinfo> addonsinfos = new ArrayList<>();
                Gson g = new Gson();
                int counts = this.items.get(pos).quantity;
                counts++;
                quantity = quantityFromUser.getText().toString();
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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodCart();
                    }
                }, 1000);

                insertFood(this.items.get(pos));
                alert.dismiss();
                Toasty.success(FoodItemActivity.this,"Item added to cart").show();
                getAllFoodItem();
            });

            List<Addonsinfo> finalAddonsinfoList1 = addonsinfoList;

            closepaymentpageIV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                    qtyShowTv.setVisibility(View.GONE);
                }
            });

            addmultipleVariant.setOnClickListener(v -> {
                List<Addonsinfo> addonsinfos = new ArrayList<>();
                Gson g = new Gson();
                int counts = this.items.get(pos).quantity;
                counts++;
                quantity = quantityFromUser.getText().toString();
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

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getFoodCart();
                    }
                }, 1000);

                insertFood(this.items.get(pos));
                
                Toasty.success(FoodItemActivity.this,"Item added to cart").show();
                getAllFoodItem();
            });
            alert.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setFoodCartHeaders();
        Utils.hideKeyboard(this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
