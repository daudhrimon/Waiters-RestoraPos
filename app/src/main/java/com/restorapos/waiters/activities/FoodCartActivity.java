package com.restorapos.waiters.activities;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.restorapos.waiters.MainActivity;
import com.restorapos.waiters.R;
import com.restorapos.waiters.adapters.FoodCartsAdaptersNew;
import com.restorapos.waiters.adapters.TableListAdapter;
import com.restorapos.waiters.interfaces.SumInterface;
import com.restorapos.waiters.model.PlaceOrderResponse;
import com.restorapos.waiters.model.TableBookDetails;
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
import com.restorapos.waiters.offlineDb.DatabaseClient;
import com.restorapos.waiters.retrofit.AppConfig;
import com.restorapos.waiters.retrofit.WaitersService;
import com.restorapos.waiters.utils.SharedPref;
import com.google.gson.Gson;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import dmax.dialog.SpotsDialog;
import es.dmoral.toasty.Toasty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FoodCartActivity extends AppCompatActivity implements SumInterface {
    private int all_members = 0;
    private RecyclerView recyclerView, tableRecyclerview;
    private WaitersService waitersService;
    private String id, typeId, orderId;
    private TextView grandTotalTv, vatTv, customerNameTv, serviceCharge;
    private EditText customerTypeTv, tableId;
    private EditText discount;
    private double serviceCrg;
    private EditText notes;
    private Double grandTotal;
    private double restaurantVat;
    private String jsonText;
    private TextView addNewItem, place, cancel;
    private SpotsDialog progressDialog;
    private List<Foodinfo> foodtasks;
    private double sumD = 0, vatD = 0;
    private SumInterface sumInterface;
    private LinearLayout memberLayout;
    private ImageView backbtn;
    private TextView personsetupinTV,cartHeader;
    private int totalPerson = 0;
    private int available_person;
    private List<TableBookDetails> tableBookDetails;
    private List<SelectedTableList> selectedTables;
    public static String countedPerson;
    public static String tableID = "";

    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_cart);

        initial();
        /*if (orderId != null && !orderId.isEmpty() && memberLayout.getVisibility() == View.VISIBLE) {
            memberLayout.setVisibility(View.GONE);

        }*/
        String OREDERID = SharedPref.read("ORDERID", "");
        if (!OREDERID.isEmpty()){
            cartHeader.setText("Cart ( Order Id : " + OREDERID + " )");
            place.setText("Update Order");
        }

        if (orderId != null) {
            addNewItem.setVisibility(View.VISIBLE);
            updateOrder();
        }

        getUnit();

        getCustomerList();

        getTableList();

        backbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                finish();
            }
        });

        serviceCharge.setText(SharedPref.read("CURRENCY", "") + SharedPref.read("SC", ""));
        serviceCrg = Double.parseDouble(SharedPref.read("SC", ""));

        discount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    grandTotal = calculateTotal(Double.valueOf(sumD), Double.valueOf(vatD), serviceCharge.getText().toString());
                    grandTotalTv.setText(SharedPref.read("CURRENCY", "") + grandTotal);
                } catch (Exception e) {/**/}
            }
        });

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double Total = 0.0;
                for (int i = 0; i < foodtasks.size(); i++){
                    Total += (Double.parseDouble(foodtasks.get(i).getPrice()) * foodtasks.get(i).quantitys) + foodtasks.get(i).getAddOnsTotal();
                }
                SharedPref.write("CartCount",String.valueOf(foodtasks.size()));
                SharedPref.write("CartTotal",String.valueOf(Double.valueOf(new DecimalFormat("##.##").format(Total))));
                startActivity(new Intent(FoodCartActivity.this, MainActivity.class));
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
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

        customerTypeTv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {/**/}
            @Override
            public void afterTextChanged(Editable editable) {
                getCustomerName(customerTypeTv.getText().toString().trim());
            }
        });

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tableID.equals("")) {
                    Toasty.error(FoodCartActivity.this, "Please Select table ", Toast.LENGTH_SHORT, true).show();
                } else if (TextUtils.isEmpty(customerNameTv.getText())) {
                    customerTypeTv.requestFocus();
                    Toasty.error(FoodCartActivity.this, "Please Insert valid Member id", Toast.LENGTH_SHORT, true).show();
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
        waitersService.checktable(tableId.getText().toString().trim()).enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                try {
                    if (response.body().getStatusCode() == 1) {
                        placeOrder();
                    } else {
                        if (progressDialog.isShowing()) {
                            progressDialog.dismiss();
                        }
                        Toasty.error(FoodCartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
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
                DatabaseClient.getInstance(FoodCartActivity.this).getAppDatabase()
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

    private double calculateTotal(Double sum, Double vat, String serviceCharge) {
        int discount = 0;
        double sc = 0;
        if (!TextUtils.isEmpty(serviceCharge)) {
            sc = Double.parseDouble(serviceCharge);
        }
        Double totalPrice = sum + vat + sc - discount;
        return totalPrice;
    }

    private void initial() {
        sumInterface = this;
        SharedPref.init(this);
        SharedPref.write("TABLE", "");
        tableBookDetails = new ArrayList<>();
        grandTotalTv = findViewById(R.id.grandTotalId);
        recyclerView = findViewById(R.id.foodCartRecyclerViewId);
        tableRecyclerview = findViewById(R.id.tableRecyclerviewId);
        vatTv = findViewById(R.id.vatId);
        cartHeader = findViewById(R.id.cartHeader);
        customerNameTv = findViewById(R.id.customerNameId);
        customerTypeTv = findViewById(R.id.customerTypeId);
        serviceCharge = findViewById(R.id.serviceChargeId);
        discount = findViewById(R.id.discountId);
        place = findViewById(R.id.orderPlaceId);
        notes = findViewById(R.id.notesId);
        addNewItem = findViewById(R.id.addId);
        cancel = findViewById(R.id.cancelId);
        tableId = findViewById(R.id.tableId);
        memberLayout = findViewById(R.id.memberLayoutId);
        backbtn = findViewById(R.id.backId);
        selectedTables = new ArrayList<>();
        waitersService = AppConfig.getRetrofit(this).create(WaitersService.class);
        try {restaurantVat = Double.parseDouble(SharedPref.read("RESTAURANT_VAT", ""));
            tableId.setText(SharedPref.read("UPDATETABLE", ""));
            customerNameTv.setText(SharedPref.read("MEMBERNAME", ""));
        } catch (Exception e) {/**/}
        id = SharedPref.read("ID", "");
        orderId = getIntent().getStringExtra("ORDERID");
        progressDialog = new SpotsDialog(this, R.style.Custom);
        progressDialog.show();
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
                    tableId.setText(response.body().getData().getTable());
                    tableID = response.body().getData().getTable();
                    Log.d("TAG", "TableID: " + tableID);
                    customerNameTv.setText(response.body().getData().getCustomername());
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
                    serviceCharge.setText(response.body().getData().getServicecharge());
                    discount.setText(response.body().getData().getDiscount());
                    SharedPref.write("UPDATETABLE", response.body().getData().getTable());
                    SharedPref.write("MEMBERNAME", response.body().getData().getCustomername());
                    getTableList();
                    Log.d("sfdfgfdgdfsdf", "onResponse: " + response.body().getData().getTable());
                    //updateInsert();
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
                unitListItem.quantitys = Integer.parseInt(foodinfo.getItemqty());
                DatabaseClient.getInstance(FoodCartActivity.this).getAppDatabase()
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

    private void getUnit() {
        class GetProduct extends AsyncTask<Void, Void, List<Foodinfo>> {
            @Override
            protected List<Foodinfo> doInBackground(Void... voids) {
                List<Foodinfo> productList = DatabaseClient
                        .getInstance(FoodCartActivity.this)
                        .getAppDatabase()
                        .taskDao()
                        .getAllUnit();
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

    private void getTableList() {
        waitersService.getTableList(id).enqueue(new Callback<TableResponse>() {
            @Override
            public void onResponse(Call<TableResponse> call, Response<TableResponse> response) {
                try {
                    List<TableInfo> items = response.body().getData().getTableinfo();
                    progressDialog.dismiss();
                    if (items.size() > 0){
                        tableRecyclerview.setAdapter(new TableListAdapter(FoodCartActivity.this, items,
                                FoodCartActivity.this));
                    } else {
                        Toasty.warning(FoodCartActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Toasty.warning(FoodCartActivity.this,"Something went Wrong",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TableResponse> call, Throwable t) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Toasty.warning(FoodCartActivity.this,"Please check your internet connection",Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }, 269);
            }
        });
    }

    private void getFoodCart() {
        try {
            Log.d("asasas", "getFoodCart: " + new Gson().toJson(foodtasks));
            sumD = 0;
            for (int i = 0; i < foodtasks.size(); i++) {

                sumD = sumD + Double.parseDouble(foodtasks.get(i).getPrice()) * foodtasks.get(i).quantitys + Double.valueOf(foodtasks.get(i).getAddOnsTotal());
                vatD = vatD + (Double.parseDouble(foodtasks.get(i).getPrice()) * Double.parseDouble(foodtasks.get(i).getProductvat())) / 100;
            }
            Log.d("SUM", "getFoodCart: " + sumD);

            if (restaurantVat > 0) {
                vatD = (restaurantVat * Double.valueOf(sumD)) / 100;

                grandTotal = Double.valueOf(sumD) + vatD;
                Log.d("SUM", "onReceive: " + grandTotal + "\t" + vatD);
                vatTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(vatD)));
                grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
            } else {
                vatTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(vatD)));
                grandTotal = Double.valueOf(sumD) + vatD;
                grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
            }
            try {
                grandTotal = calculateTotal(sumD, vatD, String.valueOf(serviceCrg));
                grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
            } catch (Exception ignored) {
                Log.d("dsafdsad", "getFoodCart: " + ignored.getLocalizedMessage());
            }
            Log.d("SUM33", new Gson().toJson(foodtasks));
            FoodCartsAdaptersNew foodCartsAdapters = new FoodCartsAdaptersNew(FoodCartActivity.this, foodtasks, sumInterface);
            recyclerView.setAdapter(foodCartsAdapters);
        } catch (Exception ignored) {
            Log.d("SUM", "getFoodCart: " + ignored.getLocalizedMessage());
        }
    }


    private void getCustomerName(String id) {
        waitersService.getCustomerName(id).enqueue(new Callback<CustomerResponse>() {
            @Override
            public void onResponse(Call<CustomerResponse> call, Response<CustomerResponse> response) {
                try {
                    Log.d("ppp", "customer name: " + new Gson().toJson(response.body()));
                    if (response.body().getStatusCode() == 1) {
                        customerNameTv.setText(response.body().getData().getCustomerName());
                    } else {
                        Toasty.error(FoodCartActivity.this, response.body().getMessage(), Toasty.LENGTH_SHORT).show();
                    }

                } catch (Exception e) {
                    customerNameTv.setText("");
                    Log.d("ppp", "onResponse: " + e.getLocalizedMessage());
                }
            }
            @Override
            public void onFailure(Call<CustomerResponse> call, Throwable t) {
                customerNameTv.setText("");
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
            for (Foodinfo food : foodtasks) {
                if (food.quantitys > 0) {
                    orderedItems.add(food);
                }

            }
        } catch (Exception e) {
            Toasty.error(FoodCartActivity.this, "No items add", Toasty.LENGTH_SHORT).show();
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
        String datas = gson.toJson(foodtasks);

        Log.e("CheckMulti", new Gson().toJson(foodtasks));
        if (datas.length() < 3) {
            Toasty.error(FoodCartActivity.this, "No Item Added", Toasty.LENGTH_SHORT).show();
            progressDialog.dismiss();
        } else {
            if (SharedPref.read("ORDERID", "").isEmpty()) {
                waitersService.postFoodCart(id, String.valueOf(vatD), tableID,
                        customerTypeTv.getText().toString(),
                        typeId,
                        String.valueOf(serviceCrg), discount.getText().toString(), String.valueOf(sumD),
                        String.valueOf(grandTotal),
                        datas, notes.getText().toString(), tableMultipleall, multipersonall,
                        countedPerson).enqueue(new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        try {
                            progressDialog.dismiss();
                            Toasty.success(FoodCartActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT, true).show();
                            startActivity(new Intent(FoodCartActivity.this, MainActivity.class));
                            tableID = "";
                            finishAffinity();
                        } catch (Exception e) {/**/}
                    }

                    @Override
                    public void onFailure(Call<PlaceOrderResponse> call, Throwable t) {/**/}
                });
            } else {
                waitersService.modifyFoodCart(id, String.valueOf(vatD), tableID, SharedPref.read("ORDERID", ""), String.valueOf(serviceCrg)
                        , discount.getText().toString(), String.valueOf(sumD), String.valueOf(grandTotal), datas).enqueue(new Callback<PlaceOrderResponse>() {
                    @Override
                    public void onResponse(Call<PlaceOrderResponse> call, Response<PlaceOrderResponse> response) {
                        try {
                            Toasty.success(FoodCartActivity.this, "Update Order Successfully", Toast.LENGTH_SHORT, true).show();
                            tableID = "";
                            startActivity(new Intent(FoodCartActivity.this, MainActivity.class));
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
        if (foodinfo.quantitys == 1) {
            sumD = sumD + Double.parseDouble(foodinfo.getPrice()) + foodinfo.getAddOnsTotal();
        } else {
            sumD = sumD + Double.parseDouble(foodinfo.getPrice());
        }
        vatD = vatD + ((Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getProductvat())) / 100);
        if (restaurantVat > 0) {
            vatD = (restaurantVat * sumD) / 100;

            grandTotal = sumD + vatD;
            Log.d("SUM", "onReceive: " + grandTotal + "\t" + vatD);
            vatTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(vatD)));
            grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
        } else {
            vatTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(vatD)));
            grandTotal = sumD + vatD;
            grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
        }
        try {
            grandTotal = calculateTotal(sumD, vatD, String.valueOf(serviceCrg));
            grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
        } catch (Exception ignored) {
            Log.d("dsafdsad", "getFoodCart: " + ignored.getLocalizedMessage());
        }
        Log.d("SUMS", "addedSum: " + sumD);
    }

    @Override
    public void divideSum(Foodinfo foodinfo) {
        Log.d("SUMMS", "divideSum: " + foodinfo.quantitys);
        if (foodinfo.quantitys == 0) {
            sumD = sumD - Double.parseDouble(foodinfo.getPrice()) - foodinfo.getAddOnsTotal();
        } else {
            sumD = sumD - Double.parseDouble(foodinfo.getPrice());
        }
        vatD = vatD - ((Double.parseDouble(foodinfo.getPrice()) * Double.parseDouble(foodinfo.getProductvat())) / 100);
        if (restaurantVat > 0) {
            vatD = (restaurantVat * sumD) / 100;

            grandTotal = sumD + vatD;
            Log.d("SUM", "onReceive: " + grandTotal + "\t" + vatD);
            vatTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(vatD)));
            grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
        } else {
            vatTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(vatD)));
            grandTotal = sumD + vatD;
            grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
        }
        try {
            grandTotal = calculateTotal(sumD, vatD, String.valueOf(serviceCrg));
            grandTotalTv.setText(SharedPref.read("CURRENCY", "") + Double.valueOf(new DecimalFormat("##.##").format(grandTotal)));
        } catch (Exception ignored) {
            Log.d("dsafdsad", "getFoodCart: " + ignored.getLocalizedMessage());
        }
    }

    public void setup_person(String tableno, String availableperson) {

        int tablenum;
        tablenum = Integer.parseInt(tableno);

        available_person = Integer.valueOf(availableperson);
        AlertDialog.Builder builder = new AlertDialog.Builder(FoodCartActivity.this);
        LayoutInflater inflater = (LayoutInflater) getApplicationContext().
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view2 = inflater.inflate(R.layout.customdialogforperson, null);
        builder.setView(view2);
        ImageView plusbuttonInperson = view2.findViewById(R.id.plusbuttonInperson);
        personsetupinTV = view2.findViewById(R.id.personsetupinTV);
        ImageView minusbuttonInperson = view2.findViewById(R.id.minusbuttonInperson);
        personsetupinTV.setText(String.valueOf(totalPerson));
        TextView textViewconfirmationForPerson = view2.findViewById(R.id.textViewconfirmationForPerson);
        AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        plusbuttonInperson.setOnClickListener(view -> {
            if ((available_person <= totalPerson)) {
                Toasty.error(FoodCartActivity.this, "No More Seat Available", Toast.LENGTH_SHORT, true).show();

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
                Toast.makeText(FoodCartActivity.this, "0 Seat Can't be Booked", Toast.LENGTH_SHORT).show();
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
    }

    public int checktableExistence(String tableid) {
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
