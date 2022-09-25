package com.restorapos.waiters.activities;

import static com.restorapos.waiters.offlineDb.DatabaseClient.getInstance;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.FoodCartsAdapter;
import com.restorapos.waiters.adapters.TableListAdapter;
import com.restorapos.waiters.databinding.ActivityCartBinding;
import com.restorapos.waiters.interfaces.SumInterface;
import com.restorapos.waiters.model.PlaceOrderResponse;
import com.restorapos.waiters.model.customerModel.CustomerFullList;
import com.restorapos.waiters.model.customerModel.CustomerFullListResponse;
import com.restorapos.waiters.model.customerModel.CustomerResponse;
import com.restorapos.waiters.model.foodlistModel.Addonsinfo;
import com.restorapos.waiters.model.foodlistModel.Foodinfo;
import com.restorapos.waiters.model.loginModel.LoginResponse;
import com.restorapos.waiters.model.tableModel.SelectedTableList;
import com.restorapos.waiters.model.tableModel.TableInfo;
import com.restorapos.waiters.model.tableModel.TableResponse;
import com.restorapos.waiters.model.updateOrderModel.IteminfoItem;
import com.restorapos.waiters.model.updateOrderModel.UpdateOrderResponse;
import com.restorapos.waiters.offlineDb.AppDatabase;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
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

public class CartActivity extends AppCompatActivity implements SumInterface {
    private ActivityCartBinding binding;
    private int all_members = 0;
    private WaitersService waitersService;
    private String id,orderId,typeId;
    private double serviceCrg;
    private String serviceType;
    private Double grandTotal;
    private double globalVat;
    private String jsonText;
    private SpotsDialog progressDialog;
    private List<Foodinfo> foodTasks;
    private double sumD = 0.0, vatD = 0.0, crgD = 0.0, disD = 0.0;
    private List<SelectedTableList> selectedTables;
    public static String countedPerson;
    public static String tableID = "";
    private String currency = "";
    private AppDatabase appDatabase;
    //private List<TableBookDetails> tableBookDetails;
    //private int totalPerson = 0;
    //private int available_person;

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCartBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SharedPref.init(this);
        SharedPref.write("TABLE", "");

        //tableBookDetails               = new ArrayList<>();
        selectedTables                   = new ArrayList<>();
        waitersService                   = AppConfig.getRetrofit(this).create(WaitersService.class);
        id                               = SharedPref.read("ID", "");
        orderId                          = getIntent().getStringExtra("ORDERID");
        globalVat                        = Double.parseDouble(SharedPref.read("vat", "0.0"));
        serviceType                      = SharedPref.read("SCT", "0");
        serviceCrg                       = Double.parseDouble(SharedPref.read("SC", "0.0"));
        //binding.tableEt.setText(SharedPref.read("UPDATETABLE", ""));
        currency                         = SharedPref.read("CURRENCY", "");
        appDatabase                      = getInstance(CartActivity.this).getAppDatabase();
        String OREDER_ID                 = SharedPref.read("ORDERID", "");
        progressDialog                   = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();
        binding.customerNameTv.setText(SharedPref.read("MEMBERNAME", ""));



        if (!OREDER_ID.isEmpty()){
            binding.cartHeader.setText("Cart ( Order Id : " + OREDER_ID + " )");
            binding.placeOrderBtn.setText("Update Order");
        }
        if (orderId != null) {
            binding.addNewItem.setVisibility(View.VISIBLE);
            updateOrder();
        }



        getUnit();

        getCustomerList();

        getTableList();



        binding.backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        binding.serviceChargeTv.setText(SharedPref.read("CURRENCY", "") + SharedPref.read("SC", ""));


        /*discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    grandTotal = calculateTotal(Double.valueOf(sumD), Double.valueOf(vatD), crgD);
                    grandTotalTv.setText(SharedPref.read("CURRENCY", "") + grandTotal);
                } catch (Exception e) {*//**//*}
            }
        });*/

        binding.addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Total = 0.0;
                for (int i = 0; i < foodTasks.size(); i++){
                    Total += (Double.parseDouble(foodTasks.get(i).getPrice()) * foodTasks.get(i).quantitys) + foodTasks.get(i).getAddOnsTotal();
                }
                SharedPref.write("CartCount",String.valueOf(foodTasks.size()));
                SharedPref.write("CartTotal",String.valueOf(Double.valueOf(new DecimalFormat("##.##").format(Total))));
                startActivity(new Intent(CartActivity.this, MainActivity.class));
            }
        });

        binding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (orderId != null){
                    onBackPressed();
                } else {
                    cancelButtonAction();
                    onBackPressed();
                }
            }
        });

        binding.customerTypeTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override
            public void afterTextChanged(Editable editable) {
                getCustomerName(binding.customerTypeTv.getText().toString().trim());
            }
        });

        binding.placeOrderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableID.equals("")) {
                    Toasty.error(CartActivity.this, "Please Select table ", Toast.LENGTH_SHORT, true).show();
                } else if (TextUtils.isEmpty(binding.customerNameTv.getText())) {
                    binding.customerTypeTv.requestFocus();
                    Toasty.error(CartActivity.this, "Please Insert valid Member id", Toast.LENGTH_SHORT, true).show();
                } else {
                    progressDialog.show();
                    placeOrder();
                }
            }
        });
    }

    private void cancelButtonAction() {
        SharedPref.write("ORDERID", "");
        SharedPref.write("UPDATETABLE", "");
        SharedPref.write("MEMBERNAME", "");
        SharedPref.write("CartCount","0");
        SharedPref.write("CartTotal","0.0");
        deleteTable();
    }

    private void getCustomerList() {
        waitersService.getCustomerFullList(id).enqueue(new Callback<CustomerFullListResponse>() {
            @Override
            public void onResponse(Call<CustomerFullListResponse> call, Response<CustomerFullListResponse> response) {
                if (response.isSuccessful()){
                    List<CustomerFullList> fullList = response.body().getData();
                    for(int i = 0; i<fullList.size();i++){
                        if(fullList.get(i).getCustomerName().equals("Walkin")){
                            getCustomerName(fullList.get(i).getCustomer_id());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<CustomerFullListResponse> call, Throwable t) {/**/}
        });
    }

    private void tableValidation() {
        waitersService.checktable(binding.tableEt.getText().toString().trim()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if (response.body().getStatusCode() == 1) {
                        placeOrder();
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toasty.error(CartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {/**/}
            }
            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {/**/}
        });
    }

    private void deleteTable() {
        class DeleteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                getInstance(CartActivity.this).getAppDatabase()
                        .taskDao()
                        .deleteFoodTable();
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        DeleteTask dt = new DeleteTask();
        dt.execute();
    }


    private void updateOrder() {
        waitersService.getUpdateOrder(orderId).enqueue(new Callback<UpdateOrderResponse>() {
            @Override
            public void onResponse(Call<UpdateOrderResponse> call, Response<UpdateOrderResponse> response) {
                try {
                    Gson gson = new Gson();
                    jsonText = gson.toJson(response.body().getData());
                    Log.d("TAG", "onResponse: " + jsonText);
                    String addonsName = "";
                    binding.tableEt.setText(response.body().getData().getTable());
                    tableID = response.body().getData().getTable();
                    Log.d("TAG", "TableID: " + tableID);
                    binding.customerNameTv.setText(response.body().getData().getCustomername());
                    for (int i = 0; i < response.body().getData().getIteminfo().size(); i++) {
                        IteminfoItem iteminfoItem = response.body().getData().getIteminfo().get(i);
                        double addOnsTotal = 0;
                        try {
                            for (int t = 0; t < iteminfoItem.getAddonsinfo().size(); t++) {
                                Addonsinfo addonsinfo = iteminfoItem.getAddonsinfo().get(t);
                                addOnsTotal = addOnsTotal + Double.parseDouble(addonsinfo.getAddonsprice()) * Double.parseDouble(String.valueOf(addonsinfo.addonsquantity));
                                if (addonsinfo.addonsquantity > 0) {
                                    addonsName = addonsinfo.getAddOnName();
                                }
                            }
                        } catch (Exception e) {/**/}
                        insertFood(iteminfoItem, addOnsTotal, addonsName);
                    }
                    binding.serviceChargeTv.setText(response.body().getData().getServicecharge());
                    //discount.setText(response.body().getData().getDiscount());
                    SharedPref.write("UPDATETABLE", response.body().getData().getTable());
                    SharedPref.write("MEMBERNAME", response.body().getData().getCustomername());
                    getTableList();
                    Log.d("sfdfgfdgdfsdf", "onResponse: " + response.body().getData().getTable());

                } catch (Exception ignored) {/**/}
            }
            @Override
            public void onFailure(Call<UpdateOrderResponse> call, Throwable t) {
                Log.d("TAG", "onResponse: " + t.getLocalizedMessage());
            }
        });
    }

    private void insertFood(IteminfoItem foodinfo, double addOnstotal, String addOnsname) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {
                Foodinfo unitListItem = new Foodinfo();
                unitListItem.setAddonsinfo(foodinfo.getAddonsinfo());
                unitListItem.setProductName(foodinfo.getProductName());
                unitListItem.setProductId(foodinfo.getProductsID());
                unitListItem.setVariantid(foodinfo.getVarientid());
                unitListItem.setAddOnsTotal(addOnstotal);
                unitListItem.setVariantName(foodinfo.getVarientname());
                unitListItem.setPrice(foodinfo.getPrice());
                unitListItem.setProductvat(foodinfo.getProductvat());
                unitListItem.setAddOnsName(addOnsname);
                unitListItem.setOfferIsavailable(foodinfo.getOfferIsavailable());
                unitListItem.setOfferstartdate(foodinfo.getOfferstartdate());
                unitListItem.setOfferendate(foodinfo.getOfferendate());
                unitListItem.setOffersRate(foodinfo.getOffersRate());
                unitListItem.quantitys = Integer.parseInt(foodinfo.getItemqty());


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

    private void getUnit() {
        class GetProduct extends AsyncTask<Void, Void, List<Foodinfo>> {
            @Override
            protected List<Foodinfo> doInBackground(Void... voids) {

                List<Foodinfo> productList = appDatabase.taskDao().getAllUnit();

                return productList;
            }
            @Override
            protected void onPostExecute(List<Foodinfo> tasks) {
                super.onPostExecute(tasks);

                foodTasks = tasks;

                getFoodCart();
            }
        }
        GetProduct gt = new GetProduct();
        gt.execute();
    }

    private void getTableList() {
        waitersService.getTableList(id).enqueue(new Callback<TableResponse>() {
            @Override
            public void onResponse(Call<TableResponse> call, Response<TableResponse> response) {
                try {
                    List<TableInfo> items = response.body().getData().getTableinfo();
                    progressDialog.dismiss();
                    if (items.size() > 0){
                        binding.tableRecyclerview.setAdapter(new TableListAdapter(CartActivity.this, items,
                                CartActivity.this));
                    } else {
                        Toasty.warning(CartActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toasty.warning(CartActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TableResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.warning(CartActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }

    private void getFoodCart() {
        try {
            sumD = 0.0;
            vatD = 0.0;
            crgD = 0.0;
            disD = 0.0;

            for (int i = 0; i < foodTasks.size(); i++) {
                sumD += ((Double.parseDouble(foodTasks.get(i).getPrice()) * foodTasks.get(i).quantitys) + foodTasks.get(i).getAddOnsTotal());
                vatD += ((Double.parseDouble(foodTasks.get(i).getPrice()) * foodTasks.get(i).quantitys) * Double.parseDouble(foodTasks.get(i).getProductvat())) / 100;
                //disD = disD + (((Double.parseDouble(foodtasks.get(i).getPrice()) * foodtasks.get(i).quantitys) + foodtasks.get(i).getAddOnsTotal())*//////////////////)

                if (foodTasks.get(i).getOfferIsavailable() != null){
                    if (foodTasks.get(i).getOfferIsavailable().equals("1") && isOfferAvailable(foodTasks.get(i).getOfferstartdate(), foodTasks.get(i).getOfferendate())){
                        disD += ((Double.parseDouble(foodTasks.get(i).getPrice())* foodTasks.get(i).quantitys)*Double.parseDouble(foodTasks.get(i).getOffersRate()))/100;
                    }
                }
            }

            if (globalVat > 0.0) {
                vatD += (globalVat * sumD);
            }

            if (foodTasks.size() > 0){
                if (serviceCrg > 0.0 && serviceType.equals("1")){
                    crgD = serviceCrg * sumD;
                } else {
                    crgD = serviceCrg;
                }
            }

            setResults();

            binding.recyclerView.setAdapter(new FoodCartsAdapter(CartActivity.this, foodTasks, this));

        } catch (Exception ignored) {/**/}

        setCartCount();
    }

    private void setResults() {

        grandTotal = (sumD + vatD + crgD)-disD;

        binding.vatTv.setText(String.format("%s%s", currency, Double.valueOf(new DecimalFormat("##.##").format(vatD))));
        binding.serviceChargeTv.setText(String.format("%s%s", currency, Double.valueOf(new DecimalFormat("##.##").format(crgD))));
        binding.grandTotalTv.setText(String.format("%s%s", currency, Double.valueOf(new DecimalFormat("##.##").format(grandTotal))));

        if (disD > 0.0){
            binding.disLay.setVisibility(View.VISIBLE);
            binding.disTv.setText(String.format("%s%s", currency, Double.valueOf(new DecimalFormat("##.##").format(disD))));
        } else {
            binding.disLay.setVisibility(View.GONE);
        }
    }

    private void setCartCount() {
        SharedPref.write("CartCount",String.valueOf(foodTasks.size()));
        SharedPref.write("CartTotal",String.valueOf(sumD));
    }

    private boolean isOfferAvailable(String offerstartdate, String offerendate) {
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


    private void getCustomerName(String id) {
        waitersService.getCustomerName(id).enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                try {
                    Log.d("ppp", "customer name: " + new Gson().toJson(response.body()));
                    if (response.body().getStatusCode() == 1) {
                        binding.customerNameTv.setText(response.body().getData().getCustomerName());
                    } else {
                        Toasty.error(CartActivity.this, response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    binding.customerNameTv.setText("");
                    Log.d("ppp", "onResponse: " + e.getLocalizedMessage());
                }
            }
            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                binding.customerNameTv.setText("");
            }
        });
    }

    public void placeOrder() {
        StringBuilder tablemultiple = new StringBuilder("");
        StringBuilder multipleperson = new StringBuilder("");
        String tableMultipleall = "";
        String multipersonall = "";
        Gson gson = new Gson();

        List<Foodinfo> orderedItems = new ArrayList<>();
        try {
            for (Foodinfo food : foodTasks) {
                if (food.quantitys > 0) {
                    orderedItems.add(food);
                }

            }
        } catch (Exception e) {
            Toasty.error(CartActivity.this, "No items add", Toasty.LENGTH_SHORT).show();
            progressDialog.dismiss();
        }
        String totalsperson;
        if (selectedTables.size() == 0) {
            totalsperson = "";
        } else {

            for (int p = 0; p < selectedTables.size(); p++) {
                all_members += Integer.parseInt(selectedTables.get(p).getPersons());
                if (p == selectedTables.size() - 1) {
                    tablemultiple.append(String.valueOf(selectedTables.get(p).getTableId()));
                    multipleperson.append(String.valueOf(selectedTables.get(p).getPersons()));
                } else {
                    tablemultiple.append(String.valueOf(selectedTables.get(p).getTableId() + ","));
                    multipleperson.append(String.valueOf(selectedTables.get(p).getPersons() + ","));
                }

            }
        }
        totalsperson = String.valueOf(all_members);
        tableMultipleall = tablemultiple.toString();
        multipersonall = multipleperson.toString();
        String datas = gson.toJson(foodTasks);

        Log.e("CheckMulti", new Gson().toJson(foodTasks));
        if (datas.length() < 3) {
            Toasty.error(CartActivity.this, "No Item Added", Toasty.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            if (SharedPref.read("ORDERID", "").isEmpty()) {
                waitersService.postFoodCart(id, String.valueOf(vatD), tableID,
                        binding.customerTypeTv.getText().toString(), typeId,
                        String.valueOf(crgD), /*discount.getText().toString()*/String.valueOf(disD), String.valueOf(sumD),
                        String.valueOf(grandTotal),
                        datas, binding.orderNoteEt.getText().toString(), tableMultipleall, multipersonall,
                        countedPerson).enqueue(new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        try {
                            progressDialog.dismiss();
                            Toasty.success(CartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                            tableID = "";
                            finishAffinity();
                        } catch (Exception e) {/**/}
                    }

                    @Override
                    public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {/**/}
                });
            } else {
                waitersService.modifyFoodCart(id, String.valueOf(vatD), tableID, SharedPref.read("ORDERID", ""), String.valueOf(crgD)
                        , /*discount.getText().toString()*/String.valueOf(disD), String.valueOf(sumD), String.valueOf(grandTotal), datas).enqueue(new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        try {
                            Toasty.success(CartActivity.this, "Update Order Successfully", Toast.LENGTH_SHORT, true).show();
                            tableID = "";
                            startActivity(new Intent(CartActivity.this, MainActivity.class));
                            finishAffinity();
                        } catch (Exception e) {/**/}
                    }
                    @Override
                    public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {
                    }
                });
                SharedPref.write("ORDERID", "");
                SharedPref.write("UPDATETABLE", "");
            }
        }
        SharedPref.write("CartCount","0");
        SharedPref.write("CartTotal","0.0");

        deleteTable();
    }

    @Override
    public void addedSum(Foodinfo foodinfo) {

        double sSum = (Double.parseDouble(foodinfo.getPrice()) + foodinfo.getAddOnsTotal());

        if (foodinfo.quantitys == 1) {
            sumD += sSum;
        } else {
            sumD += Double.parseDouble(foodinfo.getPrice());
        }

        vatD += (Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getProductvat())) / 100;

        if (foodinfo.getOfferIsavailable() != null) {
            if (foodinfo.getOfferIsavailable().equals("1") && isOfferAvailable(foodinfo.getOfferstartdate(), foodinfo.getOfferendate())) {
                disD += (Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getOffersRate())) / 100;
            }
        }

        if (globalVat > 0.0) {
            vatD += (globalVat * sSum);
        }

        if (serviceCrg > 0.0 && serviceType.equals("1")){
            crgD += (serviceCrg * sSum);
        } else {
            crgD = serviceCrg;
        }

        setResults();

        UpdateFood(foodinfo);

        setCartCount();
    }

    @Override
    public void divideSum(Foodinfo foodinfo) {

        double sSum = (Double.parseDouble(foodinfo.getPrice()) + foodinfo.getAddOnsTotal());

        if (foodinfo.quantitys == 0) {
            sumD -= sSum;
        } else {
            sumD -= Double.parseDouble(foodinfo.getPrice());
        }

        vatD -= ((Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getProductvat())) / 100);

        if (foodinfo.getOfferIsavailable() != null) {
            if (foodinfo.getOfferIsavailable().equals("1") && isOfferAvailable(foodinfo.getOfferstartdate(), foodinfo.getOfferendate())) {
                disD -= (Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getOffersRate())) / 100;
            }
        }

        if (globalVat > 0.0) {
            vatD -= (globalVat * sSum);
        }

        if (serviceCrg > 0.0 && serviceType.equals("1")){
            crgD -= serviceCrg * sSum;
        } else {
            crgD = serviceCrg;
        }

        setResults();

        UpdateFood(foodinfo);

        setCartCount();
    }

    @Override
    public void deleteSum(Foodinfo foodinfo,int pos) {

        if (foodinfo.getOfferIsavailable() != null) {
            if (foodinfo.getOfferIsavailable().equals("1") && isOfferAvailable(foodinfo.getOfferstartdate(), foodinfo.getOfferendate())) {
                disD -= (Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getOffersRate())) / 100;
            }
        }

        foodTasks.remove(pos);

        getFoodCart();

        deleteFood(foodinfo);
    }

    private void deleteFood(Foodinfo foodinfo) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                appDatabase.taskDao().delete(foodinfo);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        AddProduct st = new AddProduct();
        st.execute();

    }

    private void UpdateFood(Foodinfo foodinfo) {
        class AddProduct extends AsyncTask<Void, Void, Void> {
            @Override
            protected Void doInBackground(Void... voids) {

                appDatabase.taskDao().updateFood(foodinfo);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        AddProduct st = new AddProduct();
        st.execute();

    }

    /*public void setup_person(String tableno, String availableperson) {

        int tablenum;
        tablenum = Integer.parseInt(tableno);

        available_person = Integer.valueOf(availableperson);
        AlertDialog.Builder builder = new AlertDialog.Builder(CartActivity.this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.customdialogforperson, null);
        builder.setView(view2);
        ImageView plusbuttonInperson = view2.findViewById(R.id.plusbuttonInperson);
        TextView personsetupinTV = view2.findViewById(R.id.personsetupinTV);
        ImageView minusbuttonInperson = view2.findViewById(R.id.minusbuttonInperson);
        personsetupinTV.setText(String.valueOf(totalPerson));
        TextView textViewconfirmationForPerson = view2.findViewById(R.id.textViewconfirmationForPerson);
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        plusbuttonInperson.setOnClickListener(view -> {
            if ((available_person <= totalPerson)) {
                Toasty.error(CartActivity.this, "No More Seat Available", Toast.LENGTH_SHORT, true).show();

            } else {
                totalPerson += 1;
                personsetupinTV.setText(String.valueOf(totalPerson));
            }
        });
        minusbuttonInperson.setOnClickListener(view -> {
            if (totalPerson != 0) {
                totalPerson -= 1;
                personsetupinTV.setText(String.valueOf(totalPerson));
            } else {
                Toast.makeText(CartActivity.this, "0 Seat Can't be Booked", Toast.LENGTH_SHORT).show();
            }
        });
        personsetupinTV.setText(String.valueOf(totalPerson));

        textViewconfirmationForPerson.setOnClickListener(view -> {
            TableBookDetails tableBookDetails1 = new TableBookDetails(tablenum, totalPerson);
            if (tableBookDetails.size() == 0) {
                tableBookDetails.add(tableBookDetails1);
            } else {
                tableBookDetails.add(tableBookDetails1);
            }
            totalPerson = 0;
            alert.dismiss();
        });
        alert.show();
    }*/

    public int checkTableExistence(String tableid) {
        Log.d("table_details", "" + new Gson().toJson(selectedTables));
        for (int i = 0; i < selectedTables.size(); i++) {
            if (selectedTables.get(i).getTableId().contains(tableid)) {
                selectedTables.remove(i);
                return 1;
            }
        }
        return 0;
    }

    public void setTableData(String tableId, String person) {
        SelectedTableList tableDetails1 = new SelectedTableList(tableId, person);
        selectedTables.add(tableDetails1);
        if (selectedTables.size() == 1) {
            tableID = tableId;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (orderId != null){
            cancelButtonAction();
        }
        finish();
    }
}
