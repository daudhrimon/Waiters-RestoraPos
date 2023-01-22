package com.restorapos.waiters.retrofit;

import com.restorapos.waiters.model.PlaceOrderResponse;
import com.restorapos.waiters.model.Readyorder.ReadyOrderResponse;
import com.restorapos.waiters.model.completeCancelOrder.CompleteCancelResponse;
import com.restorapos.waiters.model.customerModel.CustomerFullListResponse;
import com.restorapos.waiters.model.customerModel.CustomerResponse;
import com.restorapos.waiters.model.dashboardModel.DashboardResponse;
import com.restorapos.waiters.model.foodlistModel.FoodlistResponse;
import com.restorapos.waiters.model.foodlistModel2.FoodlistResponse2;
import com.restorapos.waiters.model.loginModel.LoginResponse;
import com.restorapos.waiters.model.notificationModel.NotificationResponse;
import com.restorapos.waiters.model.notificationModel.OrderAcceptResponse;
import com.restorapos.waiters.model.orderHistoryModel.OrderHistoryResponse;
import com.restorapos.waiters.model.pendingOrderModel.PendingOrderResponse;
import com.restorapos.waiters.model.tableModel.TableResponse;
import com.restorapos.waiters.model.updateOrderModel.UpdateOrderResponse;
import com.restorapos.waiters.model.viewOrderModel.ViewOrderResponse;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface WaitersService {
    @FormUrlEncoded
    @POST("sign_in")
    Call<LoginResponse> doSignIn(@Field("email") String email, @Field("password") String password, @Field("token") String token);

    @FormUrlEncoded
    @POST("categorylist")
    Call<DashboardResponse> getDashboardItem(@Field("id") String id);

    @FormUrlEncoded
    @POST("foodlist")
    Call<FoodlistResponse> foodSubCategory(@Field("id") String id, @Field("CategoryID") String categoryId);

    @FormUrlEncoded
    @POST("foodlist")
    Call<FoodlistResponse2> getallfoodwithMultipleVariants(@Field("id") String id, @Field("CategoryID") String categoryId);

    @FormUrlEncoded
    @POST("foodsearchbycategory")
    Call<FoodlistResponse> foodItem(@Field("id") String id, @Field("CategoryID") String categoryId, @Field("PcategoryID") String pcategoryId);

    @FormUrlEncoded
    @POST("foodlist")
    Call<ResponseBody> foodSubCategoryResponse(@Field("id") String id, @Field("CategoryID") String CategoryID);

    @FormUrlEncoded
    @POST("fulltablelist")
    Call<TableResponse> getTableList(@Field("id") String id);

    @FormUrlEncoded
    @POST("customertype")
    Call<CustomerResponse> getCustomerType(@Field("id") String id);

    @FormUrlEncoded
    @POST("customerlist")
    Call<CustomerResponse> getCustomerName(@Field("id") String id);

    @FormUrlEncoded
    @POST("customerfullist")
    Call<CustomerFullListResponse> getCustomerFullList(@Field("id") String id);

    @FormUrlEncoded
    @POST("pendingorder")
    Call<PendingOrderResponse> getPendingOrder(@Field("id") String id);

    @FormUrlEncoded
    @POST("processingorder")
    Call<PendingOrderResponse> getProcessingOrder(@Field("id") String id);

    @FormUrlEncoded
    @POST("completeorder")
    Call<CompleteCancelResponse> getCompleteOrder(@Field("id") String id, @Field("start") int start);

    @FormUrlEncoded
    @POST("readyorder")
    Call<ReadyOrderResponse> getAllReadyOrder(@Field("id") String id, @Field("start") int start);

    @FormUrlEncoded
    @POST("cancelorder")
    Call<CompleteCancelResponse> getCancelOrder(@Field("id") String id, @Field("start") int start);

    @FormUrlEncoded
    @POST("foodcart")
    Call<PlaceOrderResponse> postFoodCart(@Field("id") String id,
                                          @Field("VatAmount") String VAT,
                                          @Field("TableId") String TableId,
                                          @Field("CustomerID") String CustomerID,
                                          @Field("TypeID") String TypeID,
                                          @Field("ServiceCharge") String ServiceCharge,
                                          @Field("Discount") String Discount,
                                          @Field("Total") String Total,
                                          @Field("Grandtotal") String Grandtotal,
                                          @Field("foodinfo") String foodinfo,
                                          @Field("CustomerNote") String CustomerNote,
                                          @Field("tablemulti") String tablemulti,
                                          @Field("multiperson") String multiperson,
                                          @Field("totalperson") String totalperson);

    @FormUrlEncoded
    @POST("modifyfoodcart")
    Call<PlaceOrderResponse> modifyFoodCart(@Field("id") String id, @Field("VatAmount") String VAT, @Field("TableId") String TableId,
                                            @Field("Orderid") String Orderid, @Field("ServiceCharge") String ServiceCharge,
                                            @Field("Discount") String Discount, @Field("Total") String Total,
                                            @Field("Grandtotal") String Grandtotal, @Field("foodinfo") String foodinfo);

    @FormUrlEncoded
    @POST("orderhistory")
    Call<OrderHistoryResponse> getorderHistory(@Field("waiterid") String id);

    @FormUrlEncoded
    @POST("completeorcancel")
    Call<ViewOrderResponse> viewOrder(@Field("waiterid") String id, @Field("Orderstatus") String Orderstatus, @Field("Orderid") String Orderid);


    @FormUrlEncoded
    @POST("updateorder")
    Call<UpdateOrderResponse> getUpdateOrder(@Field("Orderid") String Orderid);

    @FormUrlEncoded
    @POST("allonlineorder")
    Call<NotificationResponse> allOnlineOrder(@Field("id") String id);

    @FormUrlEncoded
    @POST("checktable")
    Call<LoginResponse> checktable(@Field("tableid") String tableid);

    @FormUrlEncoded
    @POST("acceptorder")
    Call<OrderAcceptResponse> acceptOrder(@Field("id") String id, @Field("order_id") String order_id);
}
