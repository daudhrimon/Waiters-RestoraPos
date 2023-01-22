package com.restorapos.waiters.interfaces;

import android.app.Dialog;

import com.restorapos.waiters.model.notificationModel.OrderinfoItem;

public interface NotificationInterface {

    void acceptOrder(String orderId, Dialog dialog);

    void viewOrder(OrderinfoItem orderInfo);
}
